package com.info.yikao.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentMainSignUpBinding
import com.info.yikao.ext.canShow
import com.info.yikao.ui.activity.*
import com.info.yikao.util.CacheUtil
import com.info.yikao.viewmodel.HomeSignUpViewModel
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.logw

class HomeSignUpFragment : BaseFragment<HomeSignUpViewModel, FragmentMainSignUpBinding>() {

    var needLogin = true
    var needCertify = true

    override fun layoutId(): Int = R.layout.fragment_main_sign_up

    override fun initView(savedInstanceState: Bundle?) {

        //点击功能块需要先判断用户的身份认证情况，如果没有登录点击去登录
        val user = CacheUtil.getUser()
        if (user != null && user.Token.canShow()) {
            //如果保存有登录信息，则去请求对应的成员信息
            needLogin = false
            mViewModel.getUserMemberInfo()
        } else {
            //如果没有登录信息，直接标记位未登录
            needLogin = true
        }

        mDatabind.step1Handle.setOnClickListener {
            //去认证
            if (needLogin) {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
            } else if (needCertify) {
                startActivity(Intent(requireContext(), CertifyActivity::class.java))
            }
        }

        mDatabind.bottomStep.setOnClickListener {
            //去认证
            if (needLogin) {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
            } else if (needCertify) {
                startActivity(Intent(requireContext(), CertifyActivity::class.java))
            }
        }

        mDatabind.step2Handle.setOnClickListener {
            //去认证
            if (needCertify) {
                Snackbar.make(mDatabind.step2Handle, "请先进行身份认证", Snackbar.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(requireActivity(), SchoolListActivity::class.java))
            }
        }

        mDatabind.step3Handle.setOnClickListener {
            //报名记录
            if (needCertify) {
                Snackbar.make(mDatabind.step2Handle, "请先进行身份认证", Snackbar.LENGTH_SHORT).show()
            } else {
                if (needLogin){
                    startActivity(Intent(requireActivity(), LoginActivity::class.java))
                }else{
                    startActivity(Intent(requireActivity(), UserOrderActivity::class.java))
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        //刷新状态
        val user = CacheUtil.getUser()
        if (user != null && user.Token.canShow()) {
            //如果保存有登录信息，则去请求对应的成员信息
            needLogin = false
            mViewModel.getUserMemberInfo()
        } else {
            //如果没有登录信息，直接标记位未登录
            needLogin = true
        }
    }
    /**
     * 根据状态显示是否可以点击
     */
    private fun showLightView(pos: Int, enable: Boolean) {
        var bgView: View? = null
        var titleTv: TextView? = null
        var okBtn: TextView? = null
        when (pos) {
            0 -> {
                bgView = mDatabind.signUpStep1
                titleTv = mDatabind.step1Title
                okBtn = mDatabind.step1Handle
            }
            1 -> {
                bgView = mDatabind.signUpStep2
                titleTv = mDatabind.step2Title
                okBtn = mDatabind.step2Handle
            }
            2 -> {
                bgView = mDatabind.signUpStep3
                titleTv = mDatabind.step3Title
                okBtn = mDatabind.step3Handle
            }
        }
        if (enable) {
            bgView?.setBackgroundResource(R.drawable.white_corner_15)
            titleTv?.setTextColor(Color.parseColor("#333333"))
            okBtn?.setBackgroundResource(R.drawable.blue_empty_corner_15)
            okBtn?.setTextColor(Color.parseColor("#52618B"))
        } else {
            bgView?.setBackgroundResource(R.drawable.grey_e6_corner_15)
            titleTv?.setTextColor(Color.parseColor("#999999"))
            if (pos == 0){
                okBtn?.setBackgroundResource(R.drawable.red_empty_corner_15)
                okBtn?.setTextColor(Color.parseColor("#FF3434"))
                okBtn?.text = "已认证"
            }else{
                okBtn?.setBackgroundResource(R.drawable.grey_99_empty_corner_15)
                okBtn?.setTextColor(Color.parseColor("#999999"))
            }
        }
    }

    override fun createObserver() {
        mViewModel.memberInfo.observe(this) { result ->
            parseState(result, {
                "member info is $it".logw()
                //获取到了用户信息
                if (it.IDNumber.canShow()) {
                    //有身份证号码，认证用户
                    needCertify = false
                    showLightView(0, false)
                    showLightView(1, true)
                    showLightView(2, true)
                    mDatabind.bottomNoticeGroup.visibility = View.GONE
                } else {
                    //没有身份证号码，非认证用户
                    needCertify = true
                    showLightView(0, true)
                    showLightView(1, false)
                    showLightView(2, false)
                    mDatabind.bottomNoticeGroup.visibility = View.VISIBLE
                }
            }, {
                //如果没有对应的信息，则需要登录
                if (it.errCode == 401) {
                    //登录信息过期
                    "登录信息过期。。。。".logw()
                    needLogin = true
                }

            })

        }

        appViewModel.userInfo.observeInFragment(this) { user ->
            if (user != null && user.Token.canShow()) {
                //如果保存有登录信息，则去请求对应的成员信息
                needLogin = false
                mViewModel.getUserMemberInfo()
            } else {
                //如果没有登录信息，直接标记位未登录
                needLogin = true
            }
        }
    }
}