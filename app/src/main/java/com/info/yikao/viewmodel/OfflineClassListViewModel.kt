package com.info.yikao.viewmodel

import androidx.lifecycle.MutableLiveData
import com.info.yikao.model.ExamClassBean
import com.info.yikao.model.ListDataUiState
import com.info.yikao.model.StreetShowBean
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request

/**
 * 线下的考场列表
 */
class OfflineClassListViewModel : BaseViewModel() {

    //列表的数据
    var listData: MutableLiveData<ListDataUiState<ExamClassBean>> = MutableLiveData()

    fun getListData(isRefresh: Boolean) {
        request({ apiService.getShowList() }, {

            val it = arrayListOf(
                ExamClassBean(1, "2022-2-2", "8:00", "1111", "1111"),
                ExamClassBean(1, null, "8:00", "2222", "22222"),
                ExamClassBean(1, null, "8:00", "3333", "3333")
            )

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
                    listData = arrayListOf<ExamClassBean>(),
                    hasMore = false
                )
            listData.value = listDataUiState


        })
    }


}