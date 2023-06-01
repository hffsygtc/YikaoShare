package com.info.yikao.viewmodel

import com.info.yikao.model.UserInfo
import com.info.yikao.util.CacheUtil
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.loge

class HomeUserViewModel : BaseViewModel() {

    var userInfo: UserInfo? = null

    fun initUser() {
        userInfo = CacheUtil.getUser()
        "user page init user is $userInfo".loge()
    }

}