package com.info.yikao.viewmodel

import androidx.lifecycle.MutableLiveData
import com.info.yikao.model.*
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 */
class SignUpListViewModel : BaseViewModel() {

    //列表的数据
    var listData: MutableLiveData<ListDataUiState<OrderBean>> = MutableLiveData()

    fun getListData(isRefresh: Boolean) {

        val datas = arrayListOf(OrderBean(1), OrderBean(2), OrderBean(3))

        val listDataUiState =
            ListDataUiState(
                isSuccess = true,
                isRefresh = isRefresh,
                isEmpty = datas.isEmpty(),
                listData = datas,
                hasMore = datas.isNotEmpty()
            )

        listData.value = listDataUiState

    }



}