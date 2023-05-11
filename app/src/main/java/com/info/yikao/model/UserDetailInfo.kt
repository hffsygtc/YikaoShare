package com.info.yikao.model

/**
 *
 */
data class UserDetailInfo(
    var Province: String = "",
    var RealName: String = "",
    var IDNumber: String = "",
    var IDCard: String = "",
    var IDCardBack: String = "",
    var StuImg1: String = "",
    var StuHeight: Float = 0f,
    var StuWeight: Float = 0f,
    var PostProvinceId: Int = 0,
    var PostProvince: String = "",
    var PostCityId: Int = 0,
    var PostCity: String = "",
    var PostAreaId: Int = 0,
    var PostArea: String = "",
    var PostDetail: String = "",
    var PostName: String = "",
    var PostTel: String = "",
    var EmergencyContact: String = "",
    var EmergencyContactTel: String = "",
)
