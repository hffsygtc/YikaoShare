package com.info.yikao.viewmodel

import com.info.yikao.model.PostMemberInfo
import com.info.yikao.model.UserDetailInfo
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 */
class PostIdInfoViewModel : BaseViewModel() {

    var inputUserInfo = UserDetailInfo()

    var postResult = UnPeekLiveData<ResultState<Any?>>()

    var memberInfo = UnPeekLiveData<ResultState<UserDetailInfo>>()

    fun postMemberInfo() {
        val postBean = PostMemberInfo(inputUserInfo)
        request({
            apiService.postMemberInfo(postBean)
        }, postResult)
    }

    fun getUserMemberInfo() {
        request({
            apiService.getMemberInfo()
        }, memberInfo)

    }

}