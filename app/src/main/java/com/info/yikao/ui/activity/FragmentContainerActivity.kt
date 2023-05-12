package com.info.yikao.ui.activity

import android.os.Bundle
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityFragmentContainerBinding
import com.info.yikao.ui.EmptyFragment
import com.info.yikao.ui.fragment.PostIdUserInfoFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class FragmentContainerActivity : BaseActivity<BaseViewModel, ActivityFragmentContainerBinding>() {

    override fun layoutId(): Int = R.layout.activity_fragment_container

    override fun initView(savedInstanceState: Bundle?) {

        val type = intent.getIntExtra("type", -1)

        val fragment = when (type) {
            1 -> {
                PostIdUserInfoFragment.newInstance(true)
            }
            else -> {
                EmptyFragment()
            }
        }

        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment_container_layout, fragment!!)
            commitAllowingStateLoss()
        }

    }
}