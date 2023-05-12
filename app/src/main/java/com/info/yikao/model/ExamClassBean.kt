package com.info.yikao.model

/**
 * 考场信息
 */
data class ExamClassBean(
    val id: Int,
    var date: String?,
    val time: String,
    val major: String,
    val classLocation: String,
)