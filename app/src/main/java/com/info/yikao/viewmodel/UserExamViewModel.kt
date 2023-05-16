package com.info.yikao.viewmodel

import androidx.lifecycle.MutableLiveData
import com.info.yikao.model.ExamBean
import com.info.yikao.model.ListDataUiState
import com.info.yikao.model.OrderBean
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request

class UserExamViewModel : BaseViewModel() {

    //列表的数据
    var listData: MutableLiveData<ListDataUiState<ExamBean>> = MutableLiveData()

    fun getListData(isRefresh: Boolean,type:Int,online:Boolean) {
        val onlineType = if (online) 0 else 1

        request({ apiService.getUserExams(onlineType,type) }, {
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
                    listData = arrayListOf<ExamBean>(),
                    hasMore = false
                )
            listData.value = listDataUiState


        })

    }


}