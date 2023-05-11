package com.info.yikao.network

import com.info.yikao.model.*
import retrofit2.http.*


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

    /**
     * 获取首页文章
     */
    @GET("/api/Home/GetHomeArticle")
    suspend fun getHomeArticles(): ApiResponse<ArrayList<NewsBean>>

    /**
     * 获取首页学校
     */
    @GET("/api/Home/GetHomeSchool")
    suspend fun getHomeSchools(): ApiResponse<ArrayList<SchoolBean>>

    /**
     * 获取首页路演
     */
    @GET("/api/Home/GetHomeShowInfo")
    suspend fun getHomeShows(): ApiResponse<ArrayList<StreetShowBean>>

    /**
     * 获取登录的验证码图片
     */
    @GET("/api/Login/GetCodeImg")
    suspend fun getLoginCodeImg(): ApiResponse<LoginCodeImg>

    /**
     * 获取短信验证码
     */
    @POST("/api/Login/SendPhoneCode")
    suspend fun getSmsCode(
        @Query("tel") phoneNum: String,
        @Query("ImgId") imgId: String,
        @Query("ImgCode") imgCode: String
    ): ApiResponse<String>

    /**
     * 获取短信验证码
     */
    @POST("/api/Login/LoginPhone")
    suspend fun phoneLogin(
        @Query("Tel") phoneNum: String,
        @Query("VerifyCode") VerifyCode: String
    ): ApiResponse<UserInfo>

    /**
     * 获取头条文章的列表
     */
    @GET("/api/Article/GetArticleList")
    suspend fun getArticleList(): ApiResponse<ArrayList<NewsBean>>

    /**
     * 获取文章详情
     */
    @POST("/api/Article/GetArticleDetail")
    suspend fun getArticleDetail(
        @Query("ArticleId") id: Int,
    ): ApiResponse<NewsBean>


    /**
     * 获取学校的列表
     */
    @POST("/api/Apply/GetSchoolList")
    suspend fun getSchoolList(
        @Query("key") key: String?,
    ): ApiResponse<ArrayList<SchoolBean>>

    /**
     * 获取学校详情
     */
    @POST("/api/Apply/GetSchoolDetail")
    suspend fun getSchoolDetail(
        @Query("SchoolId") id: Int,
    ): ApiResponse<SchoolBean>

    /**
     * 获取学校专业列表
     */
    @POST("/api/Apply/GetSubjects")
    suspend fun getSchoolMajors(
        @Query("SchoolId") id: Int,
    ): ApiResponse<ArrayList<MajorGroupBean>>

    /**
     * 获取专业详情
     */
    @POST("/api/Apply/GetSubjectsDetail")
    suspend fun getMajorDetail(
        @Query("SubjectsId") id: Int,
    ): ApiResponse<MajorBean>

    /**
     * 获取展演的列表
     */
    @GET("/api/ShowInfo/GetShowInfoList")
    suspend fun getShowList(): ApiResponse<ArrayList<StreetShowBean>>

    /**
     * 获取展演详情
     */
    @POST("/api/ShowInfo/GetShowInfoDetail")
    suspend fun getShowDetail(
        @Query("ShowInfoId") id: Int,
    ): ApiResponse<StreetShowBean>


    /**
     * 同步身份信息
     */
    @POST("api/Member/EditMember")
    suspend fun postMemberInfo(
        @Body postGroups: PostMemberInfo
    ): ApiResponse<Any?>


}