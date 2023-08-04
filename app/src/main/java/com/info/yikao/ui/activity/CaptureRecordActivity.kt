package com.info.yikao.ui.activity

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.MediaController
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.aliyun.common.Common
import com.aliyun.facebody20191230.Client
import com.aliyun.facebody20191230.models.DetectBodyCountAdvanceRequest
import com.aliyun.ossutil.models.RuntimeOptions
import com.aliyun.tea.TeaException
import com.aliyun.tea.TeaModel
import com.aliyun.teaopenapi.models.Config
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityCaptureRecordBinding
import com.info.yikao.ext.Constant
import com.info.yikao.ext.canShow
import com.info.yikao.ext.getNameString
import com.info.yikao.ui.fragment.video.CaptureFragment
import com.info.yikao.util.AudioPlayer
import com.info.yikao.viewmodel.VideoRecordViewModel
import com.permissionx.guolindev.PermissionX
import me.hgj.jetpackmvvm.ext.util.loge
import me.hgj.jetpackmvvm.ext.util.logw
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CaptureRecordActivity : BaseActivity<VideoRecordViewModel, ActivityCaptureRecordBinding>() {

    private var isBack = true

    private val cameraCapabilities = mutableListOf<CaptureFragment.CameraCapability>()
    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private lateinit var recordingState: VideoRecordEvent
    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

    private var recordState = 0

    private var outVideoPath: Uri? = null
    private var maxTime = 0f

    private var orderNum = ""


    private lateinit var cameraExecutor: ExecutorService
    private var mediaPlayer: MediaPlayer? = null
    private val audioPlayer: AudioPlayer by lazy { AudioPlayer(this) }
    private var underRecord = false //是否开始录制是否，如果在录制视频，则开始分析人脸
    private var emptyPersonCount = 0
    private var returnOkCount = 0
    private var errorNoticeCount = 0
    private var autoStopRecord = false

    override fun showMajorStatusBar() {
        showTransStatusBar()
    }

    override fun layoutId(): Int = R.layout.activity_capture_record

    override fun initView(savedInstanceState: Bundle?) {

        orderNum = intent.getStringExtra("orderNum") ?: ""
        //申请权限
        checkPermission()
        maxTime = if (Constant.RecordingDuration > 0) {
            Constant.RecordingDuration * 60.toFloat()
        } else {
            360f
        }

        mDatabind.cameraButton.setOnClickListener {
            //切换前后镜头
            isBack = !isBack
            startCamera()
        }

        mDatabind.captureButton.setOnClickListener {
            when (recordState) {
                0 -> {
                    //当前还未开始录制，点击了开始录制
                    recordState = 1
                    //录制的时候右上角的切换镜头隐藏
                    mDatabind.cameraButton.visibility = View.GONE
                    mDatabind.cameraIcon.visibility = View.GONE
                    mDatabind.recordTimeTv.visibility = View.VISIBLE
                    mDatabind.recordTimeTv.text = "0秒"
                    underRecord = true
                    mDatabind.recordStateBg.setBackgroundResource(R.drawable.record_normal_stroke)
                    mDatabind.recordStateBg.visibility = View.VISIBLE
                    //开始录像
                    captureVideo()
                    mDatabind.captureButton.setBackgroundResource(R.mipmap.icon_under_record_vide)
                }
                1 -> {
                    //录制的情况下，点击停止录制
                    stopRecord()
                }
            }
        }

        mDatabind.nextBtn.setOnClickListener {
            "to next page $outVideoPath".logw()
            val intent = Intent(this, UploadVideoActivity::class.java)
            intent.putExtra("path", outVideoPath.toString())
            intent.putExtra("orderNum", orderNum)
            startActivity(intent)

        }

        mDatabind.backButton.setOnClickListener {
            recording?.close()
            recording = null
            finish()
        }

        mDatabind.redoBtn.setOnClickListener {
            //重新录制
            recordState = 0
            mDatabind.cameraButton.visibility = View.VISIBLE
            mDatabind.cameraIcon.visibility = View.VISIBLE
            mDatabind.captureButton.visibility = View.VISIBLE
            mDatabind.processCircle.SetCurrent(0f)
            mDatabind.processCircle.visibility = View.VISIBLE
            mDatabind.redoBtn.visibility = View.GONE
            mDatabind.nextBtn.visibility = View.GONE
            mDatabind.videoViewer.visibility = View.GONE
            mDatabind.previewView.visibility = View.VISIBLE

            mDatabind.captureButton.setBackgroundResource(R.mipmap.icon_under_record_vide)
        }

        if (Constant.OnLineVideoRequire.canShow()) {
            mDatabind.recordNoticeTv.text = Constant.OnLineVideoRequire
            mDatabind.recordNoticeTv.visibility = View.VISIBLE

            mDatabind.recordNoticeTv.postDelayed({
                mDatabind.recordNoticeTv.visibility = View.GONE
            }, 3000)
        }
    }

    /**
     * 停止录制
     */
    private fun stopRecord() {
        underRecord = false
        mDatabind.recordStateBg.visibility = View.GONE
        recordState = 2
        //如果是在录像情况下，停止录像
        if (recording == null || recordingState is VideoRecordEvent.Finalize) {
            return
        }

        if (recording != null) {
            recording?.stop()
            recording = null
        }

        mDatabind.captureButton.visibility = View.GONE
        mDatabind.processCircle.visibility = View.GONE
        mDatabind.recordTimeTv.visibility = View.GONE
        mDatabind.redoBtn.visibility = View.VISIBLE
        mDatabind.nextBtn.visibility = View.VISIBLE
    }

    /**
     * 预览播放
     */
    private fun showVideo(uri: Uri) {
        val fileSize = getFileSizeFromUri(uri)
        if (fileSize == null || fileSize <= 0) {
            return
        }

        val filePath = getAbsolutePathFromUri(uri) ?: return
        val fileInfo = "FileSize: $fileSize\n $filePath"
        fileInfo.logw()

        val mc = MediaController(this)
        mDatabind.videoViewer.apply {
            setVideoURI(uri)
            setMediaController(mc)
            requestFocus()
        }.start()
        mc.show(0)
    }

    private fun getAbsolutePathFromUri(contentUri: Uri): String? {
        var cursor: Cursor? = null
        return try {
            cursor = this
                .contentResolver
                .query(contentUri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
            if (cursor == null) {
                return null
            }
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(columnIndex)
        } catch (e: RuntimeException) {
            Log.e(
                "VideoViewerFragment", String.format(
                    "Failed in getting absolute path for Uri %s with Exception %s",
                    contentUri.toString(), e.toString()
                )
            )
            null
        } finally {
            cursor?.close()
        }
    }


    private fun getFileSizeFromUri(contentUri: Uri): Long? {
        val cursor = this
            .contentResolver
            .query(contentUri, null, null, null, null)
            ?: return null

        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
        cursor.moveToFirst()

        cursor.use {
            return it.getLong(sizeIndex)
        }
    }

    private fun checkPermission() {
        "申请权限".logw()
        PermissionX.init(WebViewActivity@ this)
            .permissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
            )
            .onExplainRequestReason { scope, deniedList ->
                val message = "上传本地图片需要您同意获取读取权限才能正常使用"
                scope.showRequestReasonDialog(deniedList, message, "同意", "拒绝")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    //用户授权文件读取
                    "用户授权文件读取".logw()
                    startCamera()

                } else {
                    "拒绝了权限申请".logw()

                }
            }
    }

    private fun startCamera() {
        // 用于将相机的生命周期绑定到生命周期所有者(MainActivity)。 这消除了打开和关闭相机的任务，因为 CameraX 具有生命周期感知能力。
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        // 向 cameraProviderFuture 添加监听器。添加 Runnable 作为一个参数。我们会在稍后填写它。添加 ContextCompat.getMainExecutor() 作为第二个参数。这将返回一个在主线程上运行的 Executor。
        cameraProviderFuture.addListener({
            // 将相机的生命周期绑定到应用进程中的 LifecycleOwner。
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build()
                .also { it.setSurfaceProvider(mDatabind.previewView.surfaceProvider) } // preview 作为 usecase
            imageCapture = ImageCapture.Builder().build()
            val recorder =
                Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.HD)).build()
            videoCapture = VideoCapture.withOutput(recorder)

            val cameraSelector = if (isBack) {
                CameraSelector.DEFAULT_BACK_CAMERA
            } else {
                CameraSelector.DEFAULT_FRONT_CAMERA
            }
            try {
                cameraProvider.unbindAll() // Unbind use cases before rebinding
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    videoCapture
                ) // Bind use cases to camera: 把 cameraSelector 和 preview 绑定

                //添加视频截图的资源
                cameraExecutor = Executors.newSingleThreadExecutor()
                cameraExecutor.execute {
                    while (true) {
                        if (underRecord) {
                            //如果在录像状态下
                            runOnUiThread {
                                var bitmap: Bitmap? = mDatabind.previewView.bitmap
                                bitmap?.let {
                                    judgePeople(bitmap)
                                }
//                            mDatabind.testimg.setImageBitmap(bitmap)
//                            bitmap?.recycle()
                            }
//                        val base64Image = convertBitmapToBase64(bitmap)
                            // Handle or send the base64Image as needed
                            Thread.sleep(1000)
                        }
                    }
                }
            } catch (exc: Exception) {
                // 有多种原因可能会导致此代码失败，例如应用不再获得焦点。在此记录日志。
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun judgePeople(bit: Bitmap) {
        val accessKeyId = "LTAI5tJrRyU153URZhX1BoeK"
        val accessKeySecret = "9QBefdZRWD5NL9n2rArycqntcDkllx"
/*
          初始化配置对象com.aliyun.teaopenapi.models.Config
          Config对象存放 AccessKeyId、AccessKeySecret、endpoint等配置
         */
        /*
          初始化配置对象com.aliyun.teaopenapi.models.Config
          Config对象存放 AccessKeyId、AccessKeySecret、endpoint等配置
         */
        val config = Config()
            .setAccessKeyId(accessKeyId)
            .setAccessKeySecret(accessKeySecret)
        // 访问的域名
        // 访问的域名
        config.endpoint = "facebody.cn-shanghai.aliyuncs.com"
        val client = Client(config)


        Thread {
            try {
                // 使用文件，文件通过inputStream传入接口。这里只是演示了assets下的文件如何转为stream，如果文件来自其他地方，如sdcard或者摄像头，请自行查看android开发文档或教程将文件转为stream之后传入。
                val baos = ByteArrayOutputStream()
                bit.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val inputStream: InputStream = ByteArrayInputStream(baos.toByteArray())
                val detectBodyCountAdvanceRequest = DetectBodyCountAdvanceRequest()
                    .setImageURLObject(inputStream)
                val detectBodyCountResponse = client.detectBodyCountAdvance(
                    detectBodyCountAdvanceRequest,
                    com.aliyun.teautil.models.RuntimeOptions()
                )

//            // 获取整体结果。
//                {headers={access-control-allow-origin=*, date=Fri, 04 Aug 2023 14:31:20 GMT, content-length=78, keep-alive=timeout=25, x-acs-request-id=4C5CEE6F-45EA-5F7B-9385-88B43AB2A3F6, connection=keep-alive, content-type=application/json;charset=utf-8, etag=7+3Mq3WcdpqNZYACmYfGk1Q8, access-control-expose-headers=*, x-acs-trace-id=8ff0f7a6294cad9b280ea678a36b77d5}, body={RequestId=4C5CEE6F-45EA-5F7B-9385-88B43AB2A3F6, Data={PersonNumber=0}}, statusCode=200}

                val tMap = TeaModel.buildMap(detectBodyCountResponse)
                val sMap = Common.query(tMap)

                val personNum = sMap.getOrDefault("body.Data.PersonNumber", "0").toInt()
                if (personNum > 0) {
                    //如果有人，则错误次数归零
                    if (emptyPersonCount > 0) {
                        returnOkCount++
                        if (returnOkCount == 2) {
                            //连续两秒都有人在
                            emptyPersonCount = 0
                            mDatabind.recordStateBg.setBackgroundResource(R.drawable.record_normal_stroke)
                            mediaPlayer?.stop()
                            audioPlayer.stopPlaying()
                        }
                    }
                } else {
                    emptyPersonCount++
                    if (returnOkCount > 0) {
                        returnOkCount = 0
                    }
                    //如果连续3S都没人，则判断没人，播放警告声音
                    when (emptyPersonCount) {
                        2 -> {
                            //连续3秒没人，开始提示错误警告界面，播放声音
                            mDatabind.recordStateBg.setBackgroundResource(R.drawable.record_error_stroke)
                            audioPlayer.startPlaying()
                        }
                        20 -> {
                            //连续20秒没人，则停止录像
                            runOnUiThread {
                                autoStopRecord = true
                                underRecord = false
                                mDatabind.recordStateBg.visibility = View.GONE

                                if (recording != null) {
                                    recording?.stop()
                                    recording = null
                                }

                                mDatabind.recordTimeTv.visibility = View.GONE

                                //重新录制
                                recordState = 0
                                mDatabind.cameraButton.visibility = View.VISIBLE
                                mDatabind.cameraIcon.visibility = View.VISIBLE
                                mDatabind.captureButton.visibility = View.VISIBLE
                                mDatabind.processCircle.SetCurrent(0f)
                                mDatabind.processCircle.visibility = View.VISIBLE
                                mDatabind.redoBtn.visibility = View.GONE
                                mDatabind.nextBtn.visibility = View.GONE
                                mDatabind.videoViewer.visibility = View.GONE
                                mDatabind.previewView.visibility = View.VISIBLE

                                mDatabind.captureButton.setBackgroundResource(R.mipmap.icon_under_record_vide)

                            }
                        }
                    }
//                    if (emptyPersonCount >2){
//                        errorNoticeCount ++ //错误于
//
//                    }
                }


                Log.d("TAG", "result is  " + personNum)


            } catch (teaException: TeaException) {
                Log.d("TAG", "teaException.getCode(): " + teaException.getCode())

            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                Log.e("TAG", e.message!!)
            }
        }.start()

    }


//    private fun judgePeopleCount(bit:Bitmap) {
//        // 创建AccessKey ID和AccessKey Secret，请参考https://help.aliyun.com/document_detail/175144.html
//        // 如果您使用的是RAM用户的AccessKey，还需要为子账号授予权限AliyunVIAPIFullAccess，请参考https://help.aliyun.com/document_detail/145025.html
//        // 从环境变量读取配置的AccessKey ID和AccessKey Secret。运行代码示例前必须先配置环境变量。
//        // 创建AccessKey ID和AccessKey Secret，请参考https://help.aliyun.com/document_detail/175144.html
//        // 如果您使用的是RAM用户的AccessKey，还需要为子账号授予权限AliyunVIAPIFullAccess，请参考https://help.aliyun.com/document_detail/145025.html
//        // 从环境变量读取配置的AccessKey ID和AccessKey Secret。运行代码示例前必须先配置环境变量。
//
//
//        // 场景一，使用本地文件
//        // InputStream inputStream = new FileInputStream(new File("/tmp/DetectBodyCount.jpg"));
//        // 场景二，使用任意可访问的url
//        // 场景一，使用本地文件
//        // InputStream inputStream = new FileInputStream(new File("/tmp/DetectBodyCount.jpg"));
//        // 场景二，使用任意可访问的url
////        val url =
////            URL("https://viapi-test-bj.oss-cn-beijing.aliyuncs.com/viapi-3.0domepic/facebody/DetectBodyCount/DetectBodyCount3.jpg")
//
//        val baos = ByteArrayOutputStream()
//        bit.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//        val inputStream: InputStream = ByteArrayInputStream(baos.toByteArray())
//
//        try {
//            val runtime = RuntimeOptions()
//
//            // 获取整体结果。
//            println(Common.toJSONString(TeaModel.buildMap(detectBodyCountResponse)))
//        } catch (teaException: TeaException) {
//            // 获取整体报错信息
//            println(Common.toJSONString(teaException))
//            // 获取单个字段
//            println(teaException.getCode())
//        }
//    }

//    private fun convertBitmapToBase64(bitmap: Bitmap): String {
//        val outputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//        val byteArray = outputStream.toByteArray()
//        return Base64.encodeToString(byteArray, Base64.DEFAULT)
//    }

    private fun captureVideo() {

        val videoCapture = this.videoCapture ?: return

//        viewBinding.videoCaptureButton.isEnabled = false

        val curRecording = recording
        if (curRecording != null) {
            curRecording.stop()
            recording = null
            return
        }

        val name =
            SimpleDateFormat(FILENAME_FORMAT, Locale.CHINA).format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
        }
        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()

        recording = videoCapture.output
            .prepareRecording(this, mediaStoreOutputOptions)
            .apply {
                if (PermissionChecker.checkSelfPermission(
                        this@CaptureRecordActivity, Manifest.permission.RECORD_AUDIO
                    ) == PermissionChecker.PERMISSION_GRANTED
                ) {
                    withAudioEnabled()
                }
            }
            .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
                //录像的回调
                recordingState = recordEvent
                val state = if (recordEvent is VideoRecordEvent.Status) recordEvent.getNameString()
                else recordEvent.getNameString()

                val stats = recordEvent.recordingStats
                val size = stats.numBytesRecorded / 1000
                val time =
                    java.util.concurrent.TimeUnit.NANOSECONDS.toSeconds(stats.recordedDurationNanos)
                var text = "${state}: recorded ${size}KB, in ${time}second"

                val minut = time / 60
                val second = time % 60
                val minStr = if (minut > 0) {
                    minut.toString() + "分"
                } else {
                    ""
                }

                mDatabind.recordTimeTv.text = "$minStr${second}秒"

                if (recordEvent is VideoRecordEvent.Finalize) {
                    outVideoPath = recordEvent.outputResults.outputUri
                    text = "${text}\nFile saved to: ${recordEvent.outputResults.outputUri}"
                    "设置了path  $outVideoPath".loge()
                    //开始播放预览
                    "开始预览".logw()
                    if (outVideoPath != null) {
                        if (!autoStopRecord){
                            mDatabind.videoViewer.visibility = View.VISIBLE
                            mDatabind.previewView.visibility = View.GONE
                            showVideo(outVideoPath!!)
                        }else{
                            autoStopRecord = false
                        }
                    }
                }

                text.loge()
                //算进度
                val percent = time.toFloat() / maxTime
                mDatabind.processCircle.SetCurrent(percent)
                if (percent >= 1) {
                    //如果进度满了，停止录制
                    stopRecord()
                }

//                if(event is VideoRecordEvent.Finalize)
//                    text = "${text}\nFile saved to: ${event.outputResults.outputUri}"

                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
                        //开始录像
                    }
                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            val msg =
                                "Video capture succeeded: ${recordEvent.outputResults.outputUri}"
                        } else {
                            recording?.close()
                            recording = null
                        }
//                        viewBinding.videoCaptureButton.apply {
//                            text = getString(R.string.start_capture)
//                            isEnabled = true
//                        }
                    }
                }
            }
    }


}