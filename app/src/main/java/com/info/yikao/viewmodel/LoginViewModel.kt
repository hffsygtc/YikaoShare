package com.info.yikao.viewmodel

import com.info.yikao.model.*
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 */
class LoginViewModel : BaseViewModel() {


    var codeImg = UnPeekLiveData<LoginCodeImg>()

    fun getCodeImg() {
        request({
            apiService.getLoginCodeImg()
        }, {
            codeImg.value = it
        }, {
            codeImg.value = LoginCodeImg("", "")
        })
    }



}