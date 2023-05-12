package com.info.yikao.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.info.yikao.MainActivity
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivitySplashBinding
import com.info.yikao.viewmodel.SplashViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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