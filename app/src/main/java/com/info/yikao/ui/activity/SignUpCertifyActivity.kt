package com.info.yikao.ui.activity

import android.os.Bundle
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivitySchoolDetailBinding
import com.info.yikao.databinding.ActivitySignPayBinding
import com.info.yikao.databinding.ActivitySignPeopleCertifyBinding
import com.info.yikao.databinding.ActivitySignUpDetailBinding
import com.info.yikao.viewmodel.PeopleCertifyViewModel
import com.info.yikao.viewmodel.SignUpDetailViewModel
import com.info.yikao.viewmodel.SignUpPayViewModel

/**
 * 开始录入人像认证
 */
class SignUpCertifyActivity : BaseActivity<PeopleCertifyViewModel, ActivitySignPeopleCertifyBinding>() {
    override fun layoutId(): Int = R.layout.activity_sign_people_certify

    override fun initView(savedInstanceState: Bundle?) {

    }


}