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
    var listData: MutableLiveData<ListDataUiState<OnlinePointBean>> = MutableLiveData()

    fun getListData(isRefresh: Boolean) {
        request({ apiService.getShowList() }, {

            val testList = arrayListOf(
                OnlineExamBean(1, "111", "1111", "1111", "111", "1111"),
                OnlineExamBean(2, "222", "2222", "2222", "2222", "2222"),
                OnlineExamBean(2, "333", "3333", "3333", "3333", "333")
            )

            val it = arrayListOf(
                OnlinePointBean(1, "1111", "8:00",false, testList, testList),
                OnlinePointBean(2, "2222", "8:00",false, testList, testList),
                OnlinePointBean(3, "3333", "8:00",false, testList, testList)
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
                    listData = arrayListOf<OnlinePointBean>(),
                    hasMore = false
                )
            listData.value = listDataUiState


        })
    }


}