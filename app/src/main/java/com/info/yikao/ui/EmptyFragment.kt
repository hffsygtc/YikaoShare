package com.info.yikao.ui

import android.os.Bundle
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.EmptyFragmentBinding
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class EmptyFragment(val type:String) : BaseFragment<BaseViewModel, EmptyFragmentBinding>() {

    override fun layoutId(): Int = R.layout.empty_fragment

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.text.text = "empty $type"

    }
}