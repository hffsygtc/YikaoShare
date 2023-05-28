package com.info.yikao.model

/**
 * 考场信息
 */
data class ExamClassBean(
    val ExamRoomId: Int,
    var date: String?,
    val TestTimeStart: String,
    val SubjectsStr: String,
    val TestClass: String,
    val Duty: String = "",
    val JuryName: String = ""
)