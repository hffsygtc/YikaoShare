package com.info.yikao.viewmodel

import androidx.lifecycle.MutableLiveData
import com.info.yikao.model.ListDataUiState
import com.info.yikao.model.SchoolBean
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request

/**
 */
class SchoolListQrViewModel : BaseViewModel() {

    //列表的数据
    var listData: MutableLiveData<ListDataUiState<SchoolBean>> = MutableLiveData()

    fun getListData(isRefresh: Boolean, key: String? = null) {
        request({ apiService.getSchoolList(key) }, {
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
                    listData = arrayListOf<SchoolBean>(),
                    hasMore = false
                )
            listData.value = listDataUiState


        })
    }


}