package com.info.yikao.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentMainLoginBinding
import com.info.yikao.ui.activity.UserOrderActivity
import com.info.yikao.viewmodel.HomeSignUpViewModel
import com.info.yikao.viewmodel.HomeUserViewModel
import com.info.yikao.viewmodel.LoginViewModel
import me.hgj.jetpackmvvm.ext.nav

class LoginMainFragment : BaseFragment<LoginViewModel, FragmentMainLoginBinding>() {

    override fun layoutId(): Int = R.layout.fragment_main_login

    override fun initView(savedInstanceState: Bundle?) {

//        mDatabind.captchaImg.setCaptchaLength()

        mDatabind.captchaImg.setShowCodes(arrayListOf("1","2","2","4"))
        mDatabind.captchaImg.invalidate()

        mDatabind.getSmsBtn.setOnClickListener {
            nav().navigate(R.id.action_loginMainFragment_to_loginCodeFragment)
        }


    }


}