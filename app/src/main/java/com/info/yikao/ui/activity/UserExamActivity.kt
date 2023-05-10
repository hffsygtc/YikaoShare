package com.info.yikao.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.info.yikao.MainActivity
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.*
import com.info.yikao.ext.*
import com.info.yikao.model.OrderBean
import com.info.yikao.ui.EmptyFragment
import com.info.yikao.ui.adapter.UserOrderListAdapter
import com.info.yikao.ui.fragment.PostIdUserInfoFragment
import com.info.yikao.ui.fragment.UserExamListFragment
import com.info.yikao.viewmodel.SplashViewModel
import com.info.yikao.viewmodel.UserOrderViewModel
import com.info.yikao.weight.DefineLoadMoreView
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.logw

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