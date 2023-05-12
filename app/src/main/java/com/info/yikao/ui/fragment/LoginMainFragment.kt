package com.info.yikao.ui.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import com.google.android.material.snackbar.Snackbar
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentMainLoginBinding
import com.info.yikao.viewmodel.LoginViewModel
import me.hgj.jetpackmvvm.ext.nav
import java.util.regex.Pattern


class LoginMainFragment : BaseFragment<LoginViewModel, FragmentMainLoginBinding>() {

    private var codeId = ""

    private var isAgree = false

    override fun layoutId(): Int = R.layout.fragment_main_login

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.getSmsBtn.setOnClickListener {
            hideSoftKeyboard(activity)
            //判断内容填写
            val phoneNum = mDatabind.loginPhoneEt.text.toString()
            val codeNum = mDatabind.loginCodeEt.text.toString()


            if (isPhoneNum(phoneNum)) {
                if (codeNum != null && codeNum != "") {
                    if (isAgree){
                        //三层验证通过
                        nav().navigate(
                            R.id.action_loginMainFragment_to_loginCodeFragment,
                            Bundle().apply {
                                putString("phone", phoneNum)
                                putString("code", codeNum)
                                putString("codeId", codeId)
                            })
                    }else{
                        Snackbar.make(mDatabind.getSmsBtn, "请先同意隐私政策", Snackbar.LENGTH_SHORT).show()
                    }
                } else {
                    Snackbar.make(mDatabind.getSmsBtn, "请输入验证码", Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(mDatabind.getSmsBtn, "请输入正确的手机号", Snackbar.LENGTH_SHORT).show()
            }
        }

        mDatabind.closeBtn.setOnClickListener {
            activity?.finish()
        }

        mDatabind.captchaImg.setOnClickListener {
            //点击重试
            mViewModel.getCodeImg()
        }

        mDatabind.agreeTipsImg.setOnClickListener {
            isAgree = !isAgree
            if (isAgree){
                mDatabind.agreeTipsImg.setBackgroundResource(R.mipmap.icon_right_blue)
            }else{
                mDatabind.agreeTipsImg.setBackgroundResource(R.mipmap.icon_right_blue_empty)
            }
        }

        mViewModel.getCodeImg()
    }

    private fun isPhoneNum(phone: String): Boolean {
        val compile = Pattern.compile("^(13|14|15|16|17|18|19)\\d{9}$")
        val matcher = compile.matcher(phone)
        return matcher.matches()
    }

    override fun createObserver() {
        mViewModel.codeImg.observe(this) {
            if (it.ImgBase64Str != ""){
                codeId = it.ImgId

                //将Base64编码字符串解码成Bitmap
                val decodedString =Base64.decode(it.ImgBase64Str, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//                    Base64.decode(it.ImgBase64Str.split(",").get(1), Base64.DEFAULT)

                //设置ImageView图片
                mDatabind.captchaImg.setImageBitmap(bitmap)
            }
        }
    }


}