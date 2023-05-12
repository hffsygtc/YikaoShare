package com.info.yikao.model

/**
 * 线上需要评分的列表
 */
data class OnlinePointBean(
    val id: Int,
    val name: String,
    val time: String,
    var open: Boolean,
    val pointedList: ArrayList<OnlineExamBean>,
    val notPointedList: ArrayList<OnlineExamBean>,
)