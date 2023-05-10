package com.info.yikao.viewmodel

import androidx.lifecycle.MutableLiveData
import com.info.yikao.model.*
import com.info.yikao.util.CacheUtil
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.databind.BooleanObservableField

/**
 * 设置页面
 */
class SettingViewModel : BaseViewModel() {

    var toggleOpen = false

    fun initState() {
        toggleOpen = CacheUtil.getLoginDoubleSafeState() ?: false
    }

}