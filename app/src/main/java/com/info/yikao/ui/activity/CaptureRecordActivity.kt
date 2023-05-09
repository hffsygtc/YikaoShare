package com.info.yikao.ui.activity

import android.Manifest
import android.content.ContentValues
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.lifecycleScope
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityCaptureRecordBinding
import com.info.yikao.databinding.ActivityVideoRecordBinding
import com.info.yikao.databinding.ActivityWebviewBinding
import com.info.yikao.ui.fragment.video.CaptureFragment
import com.info.yikao.view.YkWebviewClient
import com.info.yikao.viewmodel.VideoRecordViewModel
import com.info.yikao.viewmodel.WebviewViewModel
import com.permissionx.guolindev.PermissionX
import com.tencent.smtt.sdk.WebView
import com.ycbjie.webviewlib.client.JsX5WebViewClient
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.logw
import java.text.SimpleDateFormat
import java.util.*

class CaptureRecordActivity : BaseActivity<VideoRecordViewModel, ActivityCaptureRecordBinding>() {

    private var isBack = true

    private val cameraCapabilities = mutableListOf<CaptureFragment.CameraCapability>()
    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

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
            captureVideo()
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
            val recorder = Recorder.Builder().setQualitySelector(QualitySelector.from(Quality.HIGHEST)).build()
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

        // create and start a new recording session
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.CHINA).format(System.currentTimeMillis())
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
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
//                        viewBinding.videoCaptureButton.apply {
//                            text = getString(R.string.stop_capture)
//                            isEnabled = true
//                        }
                    }
                    is VideoRecordEvent.Finalize -> {
//                        if (!recordEvent.hasError()) {
//                            val msg = "Video capture succeeded: ${recordEvent.outputResults.outputUri}"
//                            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//                            Log.d(TAG, msg)
//                        } else {
//                            recording?.close()
//                            recording = null
//                            Log.e(TAG, "Video capture ends with error: ${recordEvent.error}")
//                        }
//                        viewBinding.videoCaptureButton.apply {
//                            text = getString(R.string.start_capture)
//                            isEnabled = true
//                        }
                    }
                }
            }
    }




}