package com.info.yikao.viewmodel

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.info.yikao.model.*
import com.info.yikao.network.apiService
import com.lljjcoder.Constant
import com.lljjcoder.bean.ProvinceBean
import com.lljjcoder.utils.utils
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 */
class PostIdInfoViewModel : BaseViewModel() {

    var inputUserInfo = UserDetailInfo()

    var postResult = UnPeekLiveData<ResultState<Any?>>()

    fun postMemberInfo(){
        val postBean = PostMemberInfo(inputUserInfo)
        request({
                apiService.postMemberInfo(postBean)
        },postResult)
    }

}