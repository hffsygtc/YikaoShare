package com.info.yikao.ui.activity

import android.os.Bundle
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityLoginBinding
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class LoginActivity : BaseActivity<BaseViewModel, ActivityLoginBinding>() {

    override fun layoutId(): Int = R.layout.activity_login

    override fun initView(savedInstanceState: Bundle?) {

    }
}