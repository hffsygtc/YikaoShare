package com.info.yikao.viewmodel

import com.info.yikao.model.*
import com.info.yikao.util.CacheUtil
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class HomeUserViewModel : BaseViewModel() {

    var userInfo: UserInfo? = null

    fun initUser() {
        userInfo = CacheUtil.getUser()
    }

}