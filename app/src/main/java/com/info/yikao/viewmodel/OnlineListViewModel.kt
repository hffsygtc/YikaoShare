package com.info.yikao.viewmodel

import androidx.lifecycle.MutableLiveData
import com.info.yikao.model.*
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request

/**
 * 线上监考的评分列表
 */
class OnlineListViewModel : BaseViewModel() {

    //列表的数据
    var listData: MutableLiveData<ListDataUiState<OnlineListBean>> = MutableLiveData()

    fun getListData(isRefresh: Boolean) {
        request({ apiService.getOnLineTestList() }, {

            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    listData = it,
                    hasMore = it.isNotEmpty()
                )
            listData.value = listDataUiState
        }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    isRefresh = isRefresh,
                    isEmpty = true,
                    errMessage = it.errorMsg,
                    listData = arrayListOf<OnlineListBean>(),
                    hasMore = false
                )
            listData.value = listDataUiState


        })
    }


}