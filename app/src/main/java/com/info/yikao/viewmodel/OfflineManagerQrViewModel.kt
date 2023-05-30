package com.info.yikao.viewmodel

import com.info.yikao.model.*
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class OfflineManagerQrViewModel : BaseViewModel() {

    var classDetail = UnPeekLiveData<ResultState<ExamClassBean>>()

    var pickClassList = UnPeekLiveData<ArrayList<ClassAndSortBean>>()
    var pickSortList = UnPeekLiveData<ArrayList<ClassAndSortBean>>()

    var fullStuListBean = UnPeekLiveData<ExamClassStuList>()


    fun getOfflineClassInfo(id: Int) {
        request({ apiService.getOfflineClassDetail(id) }, classDetail)
        getClassList()
        getStudentList(id)
    }

    fun getClassList() {
        request({ apiService.getOfflineClassList() }, {
            pickClassList.value = it
        }, {})
    }

    fun getSortList(id: String) {
        request({ apiService.getOfflineSortList(id) }, {
            pickSortList.value = it
        }, {})
    }

    fun getStudentList(id: Int) {
        request({ apiService.getCurrentExamRoomSignList(id) }, {
            //获取到当前考场的考生信息
            fullStuListBean.value = it
        }, {})
    }
}