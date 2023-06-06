package com.info.yikao.viewmodel

import com.info.yikao.model.UserDetailInfo
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * 活体认证
 */
class PeopleCertifyViewModel : BaseViewModel() {

    var memberInfo = UnPeekLiveData<ResultState<UserDetailInfo>>()

    fun getUserMemberInfo() {
        request({
            apiService.getMemberInfo()
        }, memberInfo)
    }


}