package com.info.yikao.ui.activity

import android.os.Bundle
import android.widget.CompoundButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityLoginBinding
import com.info.yikao.databinding.ActivitySettingBinding
import com.info.yikao.databinding.ActivitySingleListBinding
import com.info.yikao.ext.*
import com.info.yikao.model.OrderBean
import com.info.yikao.model.UserInfo
import com.info.yikao.ui.adapter.UserOrderListAdapter
import com.info.yikao.util.CacheUtil
import com.info.yikao.viewmodel.SettingViewModel
import com.info.yikao.viewmodel.UserOrderViewModel
import com.info.yikao.weight.DefineLoadMoreView
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.logw

class SettingActivity : BaseActivity<SettingViewModel, ActivitySettingBinding>() {

    override fun layoutId(): Int = R.layout.activity_setting

    override fun initView(savedInstanceState: Bundle?) {

        mViewModel.initState()

        mDatabind.loginToggle.isChecked = mViewModel.toggleOpen

        mDatabind.loginToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                mViewModel.toggleOpen = true
                CacheUtil.setLoginDoubleSafeState(true)
            } else {
                //直接关闭
                mViewModel.toggleOpen = false
                CacheUtil.setLoginDoubleSafeState(false)
            }
        }

        mDatabind.proviceLayout.setOnClickListener {
            //修改账号
        }

        mDatabind.idCardLayout.setOnClickListener {
            //修改密码
        }

        mDatabind.headPhotoLayout.setOnClickListener {
            //账号安全
        }

        mDatabind.locationLayout.setOnClickListener {
            //隐私管理
        }

        mDatabind.streetLayout.setOnClickListener {
            //个人信息收集
        }

        mDatabind.emsNameLayout.setOnClickListener {
            //第三方信息收集
        }

        mDatabind.emsPhoneLayout.setOnClickListener {
            //关于我们

        }

        mDatabind.relativeNameLayout.setOnClickListener {
            //权限
        }

        mDatabind.versionLayout.setOnClickListener {
            //版本更新
        }

        mDatabind.exitBtn.setOnClickListener {
            //退出登录
            val userInfo = UserInfo(-1, "", "", "", "", -1)
            appViewModel.userInfo.value = userInfo
            finish()
        }


    }
}