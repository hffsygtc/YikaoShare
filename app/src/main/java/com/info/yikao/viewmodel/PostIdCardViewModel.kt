package com.info.yikao.viewmodel

import com.info.yikao.ext.Constant
import com.info.yikao.model.CertificateBean
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 */
class PostIdCardViewModel : BaseViewModel() {

    var uploadToken = ""

    var result = UnPeekLiveData<String>()

    fun getUploadToken() {
        request({ apiService.getUploadToken() }, {
            uploadToken = it.QiniuToken
        }, {})
    }

    fun saveIdCard(front: String, back: String) {
//        val frontUrl = "http://rvin5iszh.hn-bkt.clouddn.com/Student/$front"
//        val frontUrl = "${Constant.imgUrlHead}/Student/$front"
        val frontUrl = "/Student/$front"
//        val backUrl = "http://rvin5iszh.hn-bkt.clouddn.com/Student/$back"
//        val backUrl = "${Constant.imgUrlHead}/Student/$back"
        val backUrl = "/Student/$back"
        request({ apiService.saveStudentIdCard(frontUrl, backUrl) }, {
            result.value = "ok"
        }, {
            result.value = it.errorMsg
        })

    }


}