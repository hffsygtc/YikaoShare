package com.info.yikao.viewmodel

import com.info.yikao.model.ExamBean
import com.info.yikao.model.NewsBean
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * 报考订单详情的数据类
 */
class SignUpDetailViewModel : BaseViewModel() {

    var orderDetail = UnPeekLiveData<ResultState<ExamBean>>()

    fun getOrderDetail(id: String) {
        request({ apiService.getOrderDetail(id) }, orderDetail)
    }



}