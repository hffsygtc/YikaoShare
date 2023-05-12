package com.info.yikao.ui.activity

import android.os.Bundle
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityUploadVideoBinding
import com.info.yikao.viewmodel.UploadVideoViewModel

class UploadVideoActivity : BaseActivity<UploadVideoViewModel, ActivityUploadVideoBinding>() {

    override fun layoutId(): Int = R.layout.activity_upload_video

    override fun initView(savedInstanceState: Bundle?) {

       mDatabind.titleTv.text = "考试"

    }
}