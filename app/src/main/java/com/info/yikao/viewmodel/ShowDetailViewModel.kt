package com.info.yikao.viewmodel

import com.info.yikao.model.StreetShowBean
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * 展演资讯详情页
 */
class ShowDetailViewModel : BaseViewModel() {

    var showDetail = UnPeekLiveData<ResultState<StreetShowBean>>()

    fun getShowDetail(id: Int) {
        request({ apiService.getShowDetail(id) }, showDetail)
    }

}