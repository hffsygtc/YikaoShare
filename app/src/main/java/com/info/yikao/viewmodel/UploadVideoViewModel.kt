package com.info.yikao.viewmodel

import com.info.yikao.model.UserDetailInfo
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class UploadVideoViewModel : BaseViewModel() {

    var memberInfo = UnPeekLiveData<ResultState<UserDetailInfo>>()

    var token = UnPeekLiveData<String>()

    fun getUserMemberInfo() {
        request({
            apiService.getMemberInfo()
        }, memberInfo)
    }


    var result = UnPeekLiveData<String>()

    fun getUploadToken() {
        request({ apiService.getUploadToken() }, {
            token.value = it.QiniuToken
        }, {})
    }


    fun saveVideo(orderNum: String, path: String) {
        val frontUrl = "http://rvin5iszh.hn-bkt.clouddn.com/Video/$path"
        request({ apiService.saveStudentOnlineVideo(orderNum, frontUrl) }, {
            result.value = "ok"
        }, {
            result.value = it.errorMsg
        })

    }


}