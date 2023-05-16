package com.info.yikao.viewmodel

import androidx.lifecycle.MutableLiveData
import com.info.yikao.model.ExamClassBean
import com.info.yikao.model.ListDataUiState
import com.info.yikao.model.OrderBean
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.ext.util.loge

class UserOrderViewModel : BaseViewModel() {


    //列表的数据
    var listData: MutableLiveData<ListDataUiState<OrderBean>> = MutableLiveData()

    fun getListData(isRefresh: Boolean) {

        request({ apiService.getApplyOrderList() }, {
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
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    isEmpty = true,
                    listData = arrayListOf<OrderBean>(),
                    hasMore = false
                )
            listData.value = listDataUiState


        })

    }

    fun cancelOrder(id: String) {
        request({ apiService.cancelApplyOrder(id) }, {}, {})

    }
}