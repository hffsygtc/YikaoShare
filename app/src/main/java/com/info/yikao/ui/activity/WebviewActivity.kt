package com.info.yikao.ui.activity

import android.os.Bundle
import android.util.Log
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityWebviewBinding
import com.info.yikao.view.YkWebviewClient
import com.info.yikao.viewmodel.WebviewViewModel
import com.tencent.smtt.sdk.WebView
import com.ycbjie.webviewlib.client.JsX5WebViewClient

class WebviewActivity : BaseActivity<WebviewViewModel, ActivityWebviewBinding>() {


    private val mWebView by lazy { mDatabind.webView }
    private val progress by lazy { mDatabind.progress }

    override fun layoutId(): Int = R.layout.activity_webview

    override fun initView(savedInstanceState: Bundle?) {

        //初始化页面桥接
        initBridge()

        //初始化webview相关配置
        initWebView()

        val loadUrl = intent.getStringExtra("url")
        mWebView.loadUrl(loadUrl)
    }


    /**
     * 初始化和网页交互的方法
     */
    private fun initBridge() {
        mWebView.registerHandler("handleName") { data, function ->
            Log.e("CrashHandler", "nativeSearch called      $data")
            if (data != null && data != "") {
//                val gson = Gson()
//                val jsPostData: JsPostData = gson.fromJson(data, JsPostData::class.java)
//                val key: String = jsPostData.getParam()
//                searchLocalbyDb(key, function)
            }
        }
    }

    /**
     * 初始化浏览器设置
     */
    private fun initWebView() {
        mWebView.webViewClient = object : JsX5WebViewClient(mWebView, this) {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                //                view.loadUrl(url);
                //                return true;
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
        val webViewClient = YkWebviewClient(mWebView, this)
        mWebView.webViewClient = webViewClient
        mWebView.settings.allowFileAccess = true
    }


}