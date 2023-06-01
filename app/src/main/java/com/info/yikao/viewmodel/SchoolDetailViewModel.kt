package com.info.yikao.viewmodel

import com.info.yikao.model.MajorGroupBean
import com.info.yikao.model.SchoolBean
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class SchoolDetailViewModel : BaseViewModel() {


    var schoolDetail = UnPeekLiveData<ResultState<SchoolBean>>()
    var schoolMajors = UnPeekLiveData<ResultState<ArrayList<MajorGroupBean>?>>()

    fun getSchoolDetail(id: Int) {
        request({ apiService.getSchoolDetail(id) }, schoolDetail)
    }

    fun getMajorList(id: Int) {
        request({ apiService.getSchoolMajors(id) }, schoolMajors)
    }
}