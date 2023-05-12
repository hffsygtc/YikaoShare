package com.info.yikao.viewmodel

import com.info.yikao.model.UserInfo
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 */
class LoginCodeViewModel : BaseViewModel() {

    var smsCode = ""

    var userInfo = UnPeekLiveData<ResultState<UserInfo>>()

    fun getSmsCode(tel: String, code: String, codeId: String) {
        request({
            apiService.getSmsCode(tel, codeId, code)
        }, {
            smsCode = it
        }, {
            smsCode = ""
        })
    }

    fun phoneLogin(tel: String, code: String) {
        request({
            apiService.phoneLogin(tel, code)
        }, userInfo)
    }


}