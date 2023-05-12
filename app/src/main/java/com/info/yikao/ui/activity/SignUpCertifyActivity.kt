package com.info.yikao.ui.activity

import android.os.Bundle
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivitySignPeopleCertifyBinding
import com.info.yikao.viewmodel.PeopleCertifyViewModel

/**
 * 开始录入人像认证
 */
class SignUpCertifyActivity : BaseActivity<PeopleCertifyViewModel, ActivitySignPeopleCertifyBinding>() {
    override fun layoutId(): Int = R.layout.activity_sign_people_certify

    override fun initView(savedInstanceState: Bundle?) {

    }


}