package com.info.yikao.viewmodel

import com.info.yikao.model.*
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * 考生准考证信息
 */
class StudentExamCardViewModel : BaseViewModel() {

    var currentStu = UnPeekLiveData<ResultState<UserCardInfoBean>>()

    fun getTestCardInfo(id: String) {
        request({ apiService.getTestCardInfo(id) }, currentStu)
    }

}