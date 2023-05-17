package com.info.yikao.viewmodel

import com.info.yikao.model.CertificateBean
import com.info.yikao.model.ExamDetailBean
import com.info.yikao.model.NewsBean
import com.info.yikao.model.TeacherResultBean
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class CertiDetailViewModel : BaseViewModel() {

    var certiDetail = UnPeekLiveData<ResultState<CertificateBean>>()

    fun getDetail(id:String){
        request({ apiService.getCertificateDetail(id) }, certiDetail)
    }

}