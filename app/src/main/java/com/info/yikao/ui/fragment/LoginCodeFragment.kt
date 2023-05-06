package com.info.yikao.ui.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentLoginInputCodeBinding
import com.info.yikao.viewmodel.LoginCodeViewModel

class LoginCodeFragment : BaseFragment<LoginCodeViewModel, FragmentLoginInputCodeBinding>() {

    private var retry = false

    val codeTimer: CountDownTimer by lazy {
        object : CountDownTimer(60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mDatabind.timerTv.text = "${millisUntilFinished / 1000}秒后重新获取"
            }

            override fun onFinish() {
                mDatabind.timerTv.text = "获取验证码"
                retry = true
            }
        }
    }

    override fun layoutId(): Int = R.layout.fragment_login_input_code

    override fun initView(savedInstanceState: Bundle?) {

        retry = false
        codeTimer.start()

        mDatabind.timerTv.setOnClickListener {
            if (retry) {
                retry = false
                codeTimer.start()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (codeTimer != null){
            codeTimer.cancel()
        }
    }



}