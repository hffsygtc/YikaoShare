package com.info.yikao.viewmodel

import androidx.lifecycle.MutableLiveData
import com.info.yikao.model.*
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 */
class SchoolListQrViewModel : BaseViewModel() {

    //列表的数据
    var listData: MutableLiveData<ListDataUiState<SchoolBean>> = MutableLiveData()

    fun getListData(isRefresh: Boolean) {

        val datas = arrayListOf(SchoolBean("","头条1","来源1","20W")
            , SchoolBean("","头条2","来源2","20W")
            , SchoolBean("","头条3","来源3","20W"))

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