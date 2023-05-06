package com.info.yikao.ui.fragment

import android.content.Intent
import android.os.Bundle
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentMainUserBinding
import com.info.yikao.ui.activity.LoginActivity
import com.info.yikao.ui.activity.UserOrderActivity
import com.info.yikao.viewmodel.HomeUserViewModel

class HomeUserFragment : BaseFragment<HomeUserViewModel, FragmentMainUserBinding>() {

    override fun layoutId(): Int = R.layout.fragment_main_user

    override fun initView(savedInstanceState: Bundle?) {

//        if (userViewModel)

        mDatabind.userHead.setOnClickListener {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
        }

        mDatabind.userFuncOrder.setOnClickListener {
            startActivity(Intent(requireActivity(), UserOrderActivity::class.java))
        }

    }
}