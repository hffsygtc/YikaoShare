package com.info.yikao.viewmodel

import com.info.yikao.model.MessageBean
import com.info.yikao.model.NewsBean
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * 新闻快讯详情页
 */
class SysMsgViewModel : BaseViewModel() {


    var detail = UnPeekLiveData<ResultState<MessageBean>>()

    fun getArticleDetail(id: Int) {
        request({ apiService.getSysMsgDetail(id) }, detail)
    }
}