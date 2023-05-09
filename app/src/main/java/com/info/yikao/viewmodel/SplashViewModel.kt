package com.info.yikao.viewmodel

import androidx.lifecycle.MutableLiveData
import com.info.yikao.model.InitConfig
import com.info.yikao.model.ListDataUiState
import com.info.yikao.model.OrderBean
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class SplashViewModel : BaseViewModel() {

    var initConfig = UnPeekLiveData<ResultState<InitConfig>>()

    fun init() {
        request({ apiService.getBaseInfo() }, initConfig)
    }

}