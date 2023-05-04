package com.info.yikao.ui.fragment

import android.os.Bundle
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentMainUserBinding
import com.info.yikao.viewmodel.HomeUserViewModel

class HomeUserFragment : BaseFragment<HomeUserViewModel,FragmentMainUserBinding>() {

    override fun layoutId(): Int  = R.layout.fragment_main_user

    override fun initView(savedInstanceState: Bundle?) {

//        if (userViewModel)

    }
}