package com.info.yikao.ui.activity

import android.os.Bundle
import android.view.View
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityUserExamBinding
import com.info.yikao.ui.fragment.UserExamListFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class UserExamActivity : BaseActivity<BaseViewModel, ActivityUserExamBinding>() {

    val onlineFragment: UserExamListFragment by lazy {
        UserExamListFragment.newInstance(true)
    }

    val offlineFragment: UserExamListFragment by lazy {
        UserExamListFragment.newInstance(false)
    }

    private var tabIndex = 0

    override fun layoutId(): Int = R.layout.activity_user_exam

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.titleBackBtn.setOnClickListener {
            finish()
        }

        supportFragmentManager.beginTransaction().apply {
            add(R.id.fragment_container_layout, onlineFragment)
            commitAllowingStateLoss()
        }

        mDatabind.typeOnline.setOnClickListener {
            if (tabIndex != 0){
                tabIndex = 0
                mDatabind.onlineLine.visibility = View.VISIBLE
                mDatabind.offlineLine.visibility = View.GONE
                if (onlineFragment.isAdded){
                    supportFragmentManager.beginTransaction().apply {
                        hide(offlineFragment)
                        show(onlineFragment)
                        commitAllowingStateLoss()
                    }
                }else{
                    supportFragmentManager.beginTransaction().apply {
                        hide(offlineFragment)
                        add(R.id.fragment_container_layout, onlineFragment)
                        commitAllowingStateLoss()
                    }
                }
            }
        }

        mDatabind.typeOffline.setOnClickListener {
            if (tabIndex != 1){
                tabIndex = 1
                mDatabind.onlineLine.visibility = View.GONE
                mDatabind.offlineLine.visibility = View.VISIBLE
                if (offlineFragment.isAdded){
                    supportFragmentManager.beginTransaction().apply {
                        hide(onlineFragment)
                        show(offlineFragment)
                        commitAllowingStateLoss()
                    }
                }else{
                    supportFragmentManager.beginTransaction().apply {
                        hide(onlineFragment)
                        add(R.id.fragment_container_layout, offlineFragment)
                        commitAllowingStateLoss()
                    }
                }
            }
        }

    }
}