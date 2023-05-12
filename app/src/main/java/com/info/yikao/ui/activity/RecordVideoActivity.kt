package com.info.yikao.ui.activity

import android.os.Bundle
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityVideoRecordBinding
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class RecordVideoActivity : BaseActivity<BaseViewModel, ActivityVideoRecordBinding>() {

    override fun layoutId(): Int = R.layout.activity_video_record

    override fun initView(savedInstanceState: Bundle?) {

    }



}