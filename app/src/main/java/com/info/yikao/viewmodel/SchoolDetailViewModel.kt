package com.info.yikao.viewmodel

import androidx.lifecycle.MutableLiveData
import com.info.yikao.model.*
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class SchoolDetailViewModel : BaseViewModel() {


    val majorList = arrayListOf(MajorBean(1, "11"), MajorBean(2, "12"), MajorBean(3, "13"))
    val majorList2 =
        arrayListOf(MajorBean(1, "21"), MajorBean(2, "22"), MajorBean(3, "23"), MajorBean(4, "24"))
    val subTypeList = arrayListOf(
        MajorSubTypeBean("左1", majorList),
        MajorSubTypeBean("左2", majorList2),
        MajorSubTypeBean("左3", majorList)
    )
    val testMajorList = arrayListOf(
        MajorTypeBean("乐曲类", subTypeList),
        MajorTypeBean("乐曲类2", subTypeList),
        MajorTypeBean("乐曲类3", subTypeList)
    )


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