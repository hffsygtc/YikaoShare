package com.info.yikao.viewmodel

import com.info.yikao.ext.Constant
import com.info.yikao.ext.canShow
import com.info.yikao.model.UserDetailInfo
import com.info.yikao.network.apiService
import com.info.yikao.util.CacheUtil
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * 报考的页面
 */
class HomeSignUpViewModel : BaseViewModel() {

    var memberInfo = UnPeekLiveData<ResultState<UserDetailInfo>>()

    fun getUserMemberInfo() {
        val token = Constant.userToken
        if (token.canShow()){
            request({
                apiService.getMemberInfo()
            }, memberInfo)
        }

    }

}