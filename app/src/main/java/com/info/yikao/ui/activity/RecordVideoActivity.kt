package com.info.yikao.ui.activity

import android.os.Bundle
import android.util.Log
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityVideoRecordBinding
import com.info.yikao.databinding.ActivityWebviewBinding
import com.info.yikao.view.YkWebviewClient
import com.info.yikao.viewmodel.WebviewViewModel
import com.tencent.smtt.sdk.WebView
import com.ycbjie.webviewlib.client.JsX5WebViewClient
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class RecordVideoActivity : BaseActivity<BaseViewModel, ActivityVideoRecordBinding>() {

    override fun layoutId(): Int = R.layout.activity_video_record

    override fun initView(savedInstanceState: Bundle?) {

    }



}