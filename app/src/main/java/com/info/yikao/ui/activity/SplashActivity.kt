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
import com.info.yikao.databinding.ActivityLoginBinding
import com.info.yikao.databinding.ActivitySingleListBinding
import com.info.yikao.databinding.ActivitySplashBinding
import com.info.yikao.databinding.ActivityVertifyInfoBinding
import com.info.yikao.ext.*
import com.info.yikao.model.OrderBean
import com.info.yikao.ui.adapter.UserOrderListAdapter
import com.info.yikao.viewmodel.SplashViewModel
import com.info.yikao.viewmodel.UserOrderViewModel
import com.info.yikao.weight.DefineLoadMoreView
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.logw

class SplashActivity : BaseActivity<SplashViewModel, ActivitySplashBinding>() {

    override fun layoutId(): Int = R.layout.activity_splash

    override fun initView(savedInstanceState: Bundle?) {

        lifecycleScope.launch {

            delay(1500L)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
            //带点渐变动画
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

    }
}