package com.info.yikao.network

import com.info.yikao.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {

    companion object {
//        const val SERVER_URL = "http://47.108.152.207:8088"
        const val SERVER_URL = "http://api.yiqibang8.com"
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
    ): ApiResponse<ArrayList<MajorGroupBean>?>

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
        @Body postGroups: UserDetailInfo
    ): ApiResponse<Any?>

    /**
     * 获取会员信息
     */
    @GET("/api/Member/GetMemberInfo")
    suspend fun getMemberInfo(): ApiResponse<UserDetailInfo>

    /**
     * 获取报考订单
     */
    @GET("/api/Member/GetApplyOrderList")
    suspend fun getApplyOrderList(): ApiResponse<ArrayList<OrderBean>>

    /**
     * 取消订单
     */
    @POST("api/Member/CancelApplyOrder")
    suspend fun cancelApplyOrder(
        @Query("OrderNum") id: String
    ): ApiResponse<Any?>

    /**
     * 获取我的考试订单
     */
    @GET("/api/Member/GetMyTestList")
    suspend fun getUserExams(
        @Query("TestTypeId") onlineType: Int,
        @Query("TestResult") tabType: Int
    ): ApiResponse<ArrayList<ExamBean>>


    /**
     * 获取会员考试详情
     */
    @GET("/api/Member/GetMyTestDetail")
    suspend fun getExamDetail(
        @Query("OrderNum") id: String,
    ): ApiResponse<ExamDetailBean>

    /**
     * 获取会员证书信息
     */
    @GET("/api/Member/GetMyCertificateDetailDto")
    suspend fun getCertificateDetail(
        @Query("OrderNum") id: String,
    ): ApiResponse<CertificateBean>

    /**
     * 获取系统消息列表
     */
    @GET("/api/Member/GetMessageList")
    suspend fun getMsgList(): ApiResponse<ArrayList<MessageBean>>


    /**
     * 获取系统消息详情
     */
    @POST("/api/Member/GetMessageDetail")
    suspend fun getSysMsgDetail(
        @Query("MessageId") id: Int,
    ): ApiResponse<MessageBean>

    /**
     * 线下监考--获取评委的线下监考考场列表
     */
    @GET("/api/Jury/GetInvigilationOffLineList")
    suspend fun getOfflineList(): ApiResponse<ArrayList<OfflineClassBean>>

    /**
     * 线下监考--获取指定考场的信息 (评委/签到员）
     */
    @GET("/api/Jury/GetExamRoomDetail")
    suspend fun getOfflineClassDetail(
        @Query("ExamRoomId") id: Int
    ): ApiResponse<ExamClassBean>

    /**
     * 线下监考--获取当前评委(签到员/监考员)可选的考场列表
     */
    @GET("/api/Jury/GetExamRoomList")
    suspend fun getOfflineClassList(): ApiResponse<ArrayList<ClassAndSortBean>>

    /**
     * 线下监考--获取当前评委(签到员/监考员)可选的场次列表
     */
    @GET("/api/Jury/GetExamRoomListByTestClass")
    suspend fun getOfflineSortList(
        @Query("TestClass") id: String
    ): ApiResponse<ArrayList<ClassAndSortBean>>

    /**
     * 线下监考--获取指定考场的当前考生信息
     */
//    @GET("/api/Member/GetCurrentTestStudent")
    @GET("/api/Jury/GetCurrentTestSutdent")
    suspend fun getCurrentTestStu(
        @Query("ExamRoomId") id: Int
    ): ApiResponse<OnlineExamBean>


    /**
     * 线下监考--获取当前考场的考生列表
     */
    @GET("/api/Jury/GetCurrentExamRoomList")
    suspend fun getCurrentExamStuList(
        @Query("ExamRoomId") id: Int
    ): ApiResponse<ExamClassStuList>


    /**
     * 线下监考--获取当前考场的监考列表
     */
    @GET("/api/Jury/GetCurrentExamRoomSignList")
    suspend fun getCurrentExamRoomSignList(
        @Query("ExamRoomId") id: Int
    ): ApiResponse<ExamClassStuList>


    /**
     * 线下监考--获取当前考生的信息
     */
    @GET("/api/Jury/GetTestSutdentByTestCardNo")
    suspend fun getStuInfoById(
        @Query("TestCardNo") id: String
    ): ApiResponse<OnlineExamBean>

    /**
     * 线下监考--获取当前考生的评分信息
     */
    @GET("/api/Jury/GetCurrentTestSutdentGradeInfo")
    suspend fun getStuGradeInfoById(
        @Query("TestCardNo") id: String
    ): ApiResponse<NetGradeBean>

    /**
     * 线上监考--考试列表
     */
    @GET("/api/Jury/GetOnLineTestList")
    suspend fun getOnLineTestList(): ApiResponse<ArrayList<OnlineListBean>>

    /**
     * 提交考生的评分
     */
    @POST("api/Jury/AddTestSutdentGradeInfo")
    suspend fun postStudentExamGrade(
        @Body postBody: ExamGradeBean
    ): ApiResponse<Any?>

    /**
     * 获取报考订单详情信息
     */
    @GET("api/Member/GetApplyOrderDetail")
    suspend fun getOrderDetail(
        @Query("OrderNum") id: String
    ): ApiResponse<ExamBean>

    /**
     * 获取准考证信息
     */
    @GET("api/Member/GetTestCardInfo")
    suspend fun getTestCardInfo(
        @Query("TestCardNo") id: String
    ): ApiResponse<UserCardInfoBean>


    /**
     * 监考员对考生签到
     */
    @POST("/api/Jury/AddStudentSign")
    suspend fun addStudentSign(
        @Query("TestCardNo") TestCardNo: String,
        @Query("ExamRoomId") ExamRoomId: Int,
    ): ApiResponse<SignResult>


    /**
     * 获取上传凭证
     */
    @GET("api/Member/GetQiniuToken")
    suspend fun getUploadToken(): ApiResponse<UploadToken>

    /**
     * 保存考生的身份证图片地址
     */
    @POST("/api/Member/AddStudentIdentityCardUrl")
    suspend fun saveStudentIdCard(
        @Query("IDCard") front: String,
        @Query("IDCardBack") back: String,
    ): ApiResponse<Any?>

    /**
     * 线上考试--保存考生的视频地址
     */
    @POST("/api/Member/AddStudentVideoUrl")
    suspend fun saveStudentOnlineVideo(
        @Query("OrderNum") orderNum: String,
        @Query("videoUrl") url: String,
    ): ApiResponse<Any?>

    /**
     * 创建订单
     */
    @POST("/api/Member/CreateOrder")
    suspend fun createOrder(
        @Query("SubjectsId") orderNum: Int,
        @Query("PayType") PayType: String,
    ): ApiResponse<PayOrderInfo>

    /**
     * 支付订单
     */
    @POST("/api/Member/ToPayOrder")
    suspend fun payOrderResult(
        @Query("OrderNum") orderNum: String,
    ): ApiResponse<Any?>




}