package com.info.yikao.ui.activity

import android.Manifest
import android.content.ContentValues
import android.database.Cursor
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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.lifecycleScope
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityCaptureRecordBinding
import com.info.yikao.databinding.ActivityVideoRecordBinding
import com.info.yikao.databinding.ActivityWebviewBinding
import com.info.yikao.ext.getNameString
import com.info.yikao.ui.fragment.video.CaptureFragment
import com.info.yikao.view.YkWebviewClient
import com.info.yikao.viewmodel.VideoRecordViewModel
import com.info.yikao.viewmodel.WebviewViewModel
import com.permissionx.guolindev.PermissionX
import com.tencent.smtt.sdk.WebView
import com.ycbjie.webviewlib.client.JsX5WebViewClient
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.loge
import me.hgj.jetpackmvvm.ext.util.logw
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import java.util.*

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

    override fun showMajorStatusBar() {
        showTransStatusBar()
    }

    override fun layoutId(): Int = R.layout.activity_capture_record

    override fun initView(savedInstanceState: Bundle?) {
        //申请权限
        checkPermission()

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

                    //开始录像
                    captureVideo()
                    mDatabind.captureButton.setBackgroundResource(R.mipmap.icon_under_record_vide)
                }
                1 -> {
                    //录制的情况下，点击停止录制
                    recordState = 2
                    //如果是在录像情况下，停止录像
                    if (recording == null || recordingState is VideoRecordEvent.Finalize) {
                        return@setOnClickListener
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
            }
        }

        mDatabind.nextBtn.setOnClickListener {
            "to next page $outVideoPath".logw()

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
                Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.HIGHEST)).build()
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
            } catch (exc: Exception) {
                // 有多种原因可能会导致此代码失败，例如应用不再获得焦点。在此记录日志。
            }
        }, ContextCompat.getMainExecutor(this))
    }


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
                        mDatabind.videoViewer.visibility = View.VISIBLE
                        mDatabind.previewView.visibility = View.GONE
                        showVideo(outVideoPath!!)
                    }
                }

                text.loge()
                //算进度
                val percent = time.toFloat() / 6f
                mDatabind.processCircle.SetCurrent(percent)

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