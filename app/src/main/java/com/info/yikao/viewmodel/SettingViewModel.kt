package com.info.yikao.viewmodel

import com.info.yikao.util.CacheUtil
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * 设置页面
 */
class SettingViewModel : BaseViewModel() {

    var toggleOpen = false

    fun initState() {
        toggleOpen = CacheUtil.getLoginDoubleSafeState() ?: false
    }

}