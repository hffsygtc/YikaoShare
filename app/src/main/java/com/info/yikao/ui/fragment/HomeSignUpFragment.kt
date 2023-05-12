package com.info.yikao.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentMainSignUpBinding
import com.info.yikao.ui.activity.CertifyActivity
import com.info.yikao.ui.activity.SignUpListActivity
import com.info.yikao.viewmodel.HomeSignUpViewModel

class HomeSignUpFragment : BaseFragment<HomeSignUpViewModel, FragmentMainSignUpBinding>() {

    override fun layoutId(): Int = R.layout.fragment_main_sign_up

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.step1Handle.setOnClickListener {
            //去认证
            startActivity(Intent(requireContext(),CertifyActivity::class.java))
        }

        mDatabind.step2Handle.setOnClickListener {
            //去认证
            startActivity(Intent(requireContext(), SignUpListActivity::class.java))
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
            okBtn?.setBackgroundResource(R.drawable.grey_99_empty_corner_15)
            okBtn?.setTextColor(Color.parseColor("#999999"))
        }
    }
}