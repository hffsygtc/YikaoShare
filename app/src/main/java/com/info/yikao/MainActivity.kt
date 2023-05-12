package com.info.yikao

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityMainBinding
import com.info.yikao.ext.Constant
import com.info.yikao.ext.interceptLongClick
import com.info.yikao.ui.EmptyFragment
import com.info.yikao.ui.fragment.HomeFragment
import com.info.yikao.ui.fragment.HomeSignUpFragment
import com.info.yikao.ui.fragment.HomeUserFragment
import com.info.yikao.viewmodel.MainViewModel
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.logw

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    private var underIndex = R.id.menu_home

    override fun layoutId(): Int = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {

        mViewModel.init()

        mDatabind.mainViewPager.initMain(this)

        mDatabind.mainViewPager.setCurrentItem(0, false)

        //初始化 bottomBar
        mDatabind.mainBottom.init {
            if (it.itemId != underIndex) {
                underIndex = it.itemId

                resetToDefaultIcon()

                when (it.itemId) {
                    R.id.menu_home -> {
                        it.setIcon(R.mipmap.tab_home_click)
                        mDatabind.mainViewPager.setCurrentItem(0, false)
                    }
                    R.id.menu_sign_up -> {
                        it.setIcon(R.mipmap.tab_sign_click)
                        mDatabind.mainViewPager.setCurrentItem(1, false)
                    }
                    R.id.menu_message -> {
                        it.setIcon(R.mipmap.tab_msg_click)
                        mDatabind.mainViewPager.setCurrentItem(2, false)
                    }
                    R.id.menu_user -> {
                        it.setIcon(R.mipmap.tab_user_click)
                        mDatabind.mainViewPager.setCurrentItem(3, false)
                    }
                }
            } else {
                //todo refresh
            }
        }

        mDatabind.mainBottom.menu.getItem(0).setIcon(R.mipmap.tab_home_click)

        mDatabind.mainBottom.interceptLongClick(
            R.id.menu_home,
            R.id.menu_sign_up,
            R.id.menu_message,
            R.id.menu_user,
        )
    }


    private fun resetToDefaultIcon() {
        val home = mDatabind.mainBottom.menu.findItem(R.id.menu_home)
        home.setIcon(R.mipmap.tab_home_normal)

        val signUp = mDatabind.mainBottom.menu.findItem(R.id.menu_sign_up)
        signUp.setIcon(R.mipmap.tab_sign_normal)

        val message = mDatabind.mainBottom.menu.findItem(R.id.menu_message)
        message.setIcon(R.mipmap.tab_msg_normal)

        val user = mDatabind.mainBottom.menu.findItem(R.id.menu_user)
        user.setIcon(R.mipmap.tab_user_normal)
    }


    private fun ViewPager2.initMain(fragment: FragmentActivity): ViewPager2 {
        //是否可滑动
        this.isUserInputEnabled = false
        this.offscreenPageLimit = 2
        //设置适配器
        adapter = object : FragmentStateAdapter(fragment) {
            override fun createFragment(position: Int): Fragment {
                when (position) {
                    0 -> {
                        return HomeFragment()
                    }
                    1 -> {
                        return HomeSignUpFragment()
                    }
                    2 -> {
                        return EmptyFragment("2")
                    }
                    3 -> {
                        return HomeUserFragment()
                    }
                    else -> {
                        return EmptyFragment("3")
                    }
                }
                return EmptyFragment("default")
            }

            override fun getItemCount() = 4
        }
        return this
    }

    private fun BottomNavigationViewEx.init(navigationItemSelectedAction: (MenuItem) -> Unit): BottomNavigationViewEx {
        enableAnimation(false)
        enableShiftingMode(false)
        enableItemShiftingMode(false)
//    itemIconTintList = SettingUtil.getColorStateList(SettingUtil.getColor(appContext))
        itemIconTintList = null
        setTextSize(11f)
        setOnNavigationItemSelectedListener {
            navigationItemSelectedAction.invoke(it)
            true
        }
        return this
    }

    override fun createObserver() {
        mViewModel.initConfig.observe(this) { result ->
            //获取到了基本的配置信息
            "get init config is $result".logw()
            parseState(result, {
                Constant.imgUrlHead = it.ImgPrefix
            }, {

            })
        }
    }
}

