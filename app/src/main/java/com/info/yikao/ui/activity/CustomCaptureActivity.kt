package com.info.yikao.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.zxing.Result
import com.info.yikao.MainActivity
import com.info.yikao.R
import com.king.zxing.CaptureActivity
import com.king.zxing.DecodeConfig
import com.king.zxing.DecodeFormatManager
import com.king.zxing.analyze.MultiFormatAnalyzer
import me.hgj.jetpackmvvm.ext.util.loge

/**
 * by BiTiDaddy on 2023/5/8 14:15
 *
 */
class CustomCaptureActivity : CaptureActivity() {

    private var isContinuousScan = false

    private var toast: Toast? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_catupure_page
    }

    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
    }

    override fun initCameraScan() {
        super.initCameraScan()
        //初始化解码配置
        val decodeConfig = DecodeConfig()
        decodeConfig.setHints(DecodeFormatManager.ALL_HINTS) ////设置解码
            .setSupportVerticalCode(true) //设置是否支持扫垂直的条码 （增强识别率，相应的也会增加性能消耗）
            .setSupportLuminanceInvert(true) //设置是否支持识别反色码，黑白颜色反转（增强识别率，相应的也会增加性能消耗）
            .setAreaRectRatio(0.8f).isFullAreaScan = false //设置是否全区域识别，默认false

        //获取CameraScan，里面有扫码相关的配置设置。CameraScan里面包含部分支持链式调用的方法，即调用返回是CameraScan本身的一些配置建议在startCamera之前调用。
        cameraScan.setPlayBeep(true) //设置是否播放音效，默认为false
            .setVibrate(true) //设置是否震动，默认为false
            //                .setCameraConfig(new CameraConfig())//设置相机配置信息，CameraConfig可覆写options方法自定义配置
            //                .setCameraConfig(new ResolutionCameraConfig(this))//设置CameraConfig，可以根据自己的需求去自定义配置
            .setNeedAutoZoom(false) //二维码太小时可自动缩放，默认为false
            .setNeedTouchZoom(true) //支持多指触摸捏合缩放，默认为true
            .setDarkLightLux(45f) //设置光线足够暗的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
            .setBrightLightLux(100f) //设置光线足够明亮的阈值（单位：lux），需要通过{@link #bindFlashlightView(View)}绑定手电筒才有效
            .bindFlashlightView(ivFlashlight) //绑定手电筒，绑定后可根据光线传感器，动态显示或隐藏手电筒按钮
            .setOnScanResultCallback(this) //设置扫码结果回调，需要自己处理或者需要连扫时，可设置回调，自己去处理相关逻辑
            .setAnalyzer(MultiFormatAnalyzer(decodeConfig)) //设置分析器,DecodeConfig可以配置一些解码时的配置信息，如果内置的不满足您的需求，你也可以自定义实现，
            .setAnalyzeImage(true) //设置是否分析图片，默认为true。如果设置为false，相当于关闭了扫码识别功能
    }

    /**
     * 扫码结果回调
     * @param result
     * @return 返回false表示不拦截，将关闭扫码界面并将结果返回给调用界面；
     * 返回true表示拦截，需自己处理逻辑。当isAnalyze为true时，默认会继续分析图像（也就是连扫）。
     * 如果只是想拦截扫码结果回调，并不想继续分析图像（不想连扫），请在拦截扫码逻辑处通过调
     * 用[CameraScan.setAnalyzeImage]，
     * 因为[CameraScan.setAnalyzeImage]方法能动态控制是否继续分析图像。
     */
    override fun onScanResultCallback(result: Result): Boolean {
        if (isContinuousScan) {
            "result is ${result.text}".loge()
        }
        /*
         * 因为setAnalyzeImage方法能动态控制是否继续分析图像。
         *
         * 1. 因为分析图像默认为true，如果想支持连扫，返回true即可。
         * 当连扫的处理逻辑比较复杂时，请在处理逻辑前调用getCameraScan().setAnalyzeImage(false)，
         * 来停止分析图像，等逻辑处理完后再调用getCameraScan().setAnalyzeImage(true)来继续分析图像。
         *
         * 2. 如果只是想拦截扫码结果回调自己处理逻辑，但并不想继续分析图像（即不想连扫），可通过
         * 调用getCameraScan().setAnalyzeImage(false)来停止分析图像。
         */
        cameraScan.setAnalyzeImage(false)

        "result is ${result.text}  $isContinuousScan".loge()
        val intent = Intent()
        intent.putExtra("result", result.text)
        setResult(RESULT_OK, intent)
        finish()

        return isContinuousScan
    }

}