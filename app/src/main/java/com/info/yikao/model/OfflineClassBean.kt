package com.info.yikao.model

/**
 * 考场信息
 */
data class OfflineClassBean(
    val BeginStr:String,
    val List: ArrayList<ExamClassBean>
)