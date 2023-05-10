package com.info.yikao.ui.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentLoginInputCodeBinding
import com.info.yikao.ext.Constant
import com.info.yikao.util.CacheUtil
import com.info.yikao.view.VerificationCodeView
import com.info.yikao.viewmodel.LoginCodeViewModel
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.logw

class LoginCodeFragment : BaseFragment<LoginCodeViewModel, FragmentLoginInputCodeBinding>() {

    private var retry = false

    private var phoneNum = ""
    private var codeNum = ""
    private var codeId = ""

    private var inputCode = ""

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

        arguments?.let {
            phoneNum = it.getString("phone", "")
            codeNum = it.getString("code", "")
            codeId = it.getString("codeId", "")
        }

        "start get info is $phoneNum  $codeNum  $codeId".logw()

        mDatabind.loginDescPhone.text = phoneNum

        mViewModel.getSmsCode(phoneNum, codeNum, codeId)

        retry = false
        codeTimer.start()

        mDatabind.timerTv.setOnClickListener {
            if (retry) {
                retry = false
                codeTimer.start()
            }
        }

        mDatabind.closeBtn.setOnClickListener {
            nav().popBackStack()
        }

        mDatabind.smsCodeInput.onCodeFinishListener =
            object : VerificationCodeView.OnCodeFinishListener {
                override fun onTextChange(view: View?, content: String?) {
                }

                override fun onComplete(view: View?, content: String?) {
                    "输入完成了 onComplete $content ".logw()
                    //没有本地验证，直接提交服务器
                    inputCode = content ?: ""
                    hideSoftKeyboard(activity)
                    mViewModel.phoneLogin(phoneNum, content ?: "")
                }
            }

        mDatabind.loginBtn.setOnClickListener {
            mViewModel.phoneLogin(phoneNum, inputCode)
        }
    }

    override fun createObserver() {
        mViewModel.userInfo.observe(this) { result ->
            parseState(result, {
                //登录成功了
                CacheUtil.setUser(it)
                appViewModel.userInfo.value = it
                activity?.finish()
            }, {
                //登录异常
                Snackbar.make(mDatabind.loginBtn, it.errorMsg, Snackbar.LENGTH_SHORT).show()
            })
        }
    }

    override fun onPause() {
        super.onPause()
        if (codeTimer != null) {
            codeTimer.cancel()
        }
    }


}