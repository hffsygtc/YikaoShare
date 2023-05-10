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
import com.info.yikao.viewmodel.SplashViewModel
import com.info.yikao.viewmodel.UserOrderViewModel
import com.info.yikao.weight.DefineLoadMoreView
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.logw

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