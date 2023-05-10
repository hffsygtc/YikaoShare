package com.info.yikao.viewmodel

import androidx.lifecycle.MutableLiveData
import com.info.yikao.model.*
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

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

    var schoolDetail = UnPeekLiveData<ResultState<SchoolBean>>()

    fun getSchoolDetail(id: Int) {
        request({ apiService.getSchoolDetail(id) }, schoolDetail)
    }
}