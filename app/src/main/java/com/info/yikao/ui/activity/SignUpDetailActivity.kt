package com.info.yikao.ui.activity

import android.content.Intent
import android.os.Bundle
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivitySignUpDetailBinding
import com.info.yikao.viewmodel.SignUpDetailViewModel

/**
 * 报名详情
 */
class SignUpDetailActivity : BaseActivity<SignUpDetailViewModel, ActivitySignUpDetailBinding>() {
    override fun layoutId(): Int = R.layout.activity_sign_up_detail

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.enterExamBtn.setOnClickListener {
            startActivity(Intent(this@SignUpDetailActivity,SignUpCertifyActivity::class.java))
        }

    }


}