package com.info.yikao.network

import com.info.yikao.model.ApiResponse
import com.info.yikao.model.BannerArticle
import com.info.yikao.model.InitConfig
import com.info.yikao.model.UserInfo
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {

    companion object {
        const val SERVER_URL = "http://47.108.152.207:8088"
    }

    /**
     * 获取首页的轮播
     */
    @GET("/api/Home/GetBanner")
    suspend fun getBanner(): ApiResponse<ArrayList<BannerArticle>>

    /**
     * 获取基本信息
     */
    @GET("/api/Home/GetBaseInfo")
    suspend fun getBaseInfo(): ApiResponse<InitConfig>
}