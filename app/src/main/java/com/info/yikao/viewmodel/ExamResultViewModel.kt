package com.info.yikao.viewmodel

import com.info.yikao.model.ExamDetailBean
import com.info.yikao.model.NewsBean
import com.info.yikao.model.TeacherResultBean
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class ExamResultViewModel : BaseViewModel() {

    var examDetail = UnPeekLiveData<ResultState<ExamDetailBean>>()

    fun getExamDetail(id:String){
        request({ apiService.getExamDetail(id) }, examDetail)
    }

}