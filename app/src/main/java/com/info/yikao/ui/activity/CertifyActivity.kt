package com.info.yikao.ui.activity

import android.os.Bundle
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityVertifyInfoBinding
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class CertifyActivity : BaseActivity<BaseViewModel, ActivityVertifyInfoBinding>() {

    override fun layoutId(): Int = R.layout.activity_vertify_info

    override fun initView(savedInstanceState: Bundle?) {

    }
}