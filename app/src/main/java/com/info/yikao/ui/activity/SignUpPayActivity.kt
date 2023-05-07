package com.info.yikao.ui.activity

import android.os.Bundle
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivitySignPayBinding
import com.info.yikao.viewmodel.SignUpPayViewModel

/**
 * 报名支付
 */
class SignUpPayActivity : BaseActivity<SignUpPayViewModel, ActivitySignPayBinding>() {
    override fun layoutId(): Int = R.layout.activity_sign_pay

    override fun initView(savedInstanceState: Bundle?) {

    }


}