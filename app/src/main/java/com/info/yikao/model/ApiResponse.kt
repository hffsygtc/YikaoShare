package com.info.yikao.model

import me.hgj.jetpackmvvm.network.BaseResponse

data class ApiResponse<T>(val Code: Int, val Success:Boolean,val Message: String, val Data: T) : BaseResponse<T>() {

    override fun isSucces() = Success

    override fun getResponseCode() = Code

    override fun getResponseData() = Data

    override fun getResponseMsg() = Message

}