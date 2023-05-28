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
        request({ apiService.getOfflineList() }, {

            val result = arrayListOf<ExamClassBean>()

            for(dayBean in it){
                //每一天的考试数据，先添加头部
                result.add(ExamClassBean(-1,dayBean.BeginStr,"","",""))
                result.addAll(dayBean.List)
            }

            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = result.isEmpty(),
                    listData = result,
                    hasMore = result.isNotEmpty()
                )
            listData.value = listDataUiState

        }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    isRefresh = isRefresh,
                    isEmpty = true,
                    errMessage = it.errorMsg,
                    listData = arrayListOf<ExamClassBean>(),
                    hasMore = false
                )
            listData.value = listDataUiState


        })
    }


}