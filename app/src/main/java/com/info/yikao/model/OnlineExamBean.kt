package com.info.yikao.model

/**
 * 线上需要评分的考试
 */
data class OnlineExamBean(
    val id: Int,
    val name: String,
    val img: String,
    val major: String,
    val point: String?,
    val comment: String?
)