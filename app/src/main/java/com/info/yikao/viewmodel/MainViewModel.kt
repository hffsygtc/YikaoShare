package com.info.yikao.viewmodel

import com.info.yikao.model.InitConfig
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class MainViewModel : BaseViewModel() {

    var initConfig = UnPeekLiveData<ResultState<InitConfig>>()

    fun init() {
        request({ apiService.getBaseInfo() }, initConfig)


    }

}