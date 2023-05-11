package com.info.yikao.model

data class TeacherResultBean(
    val type: Int,
    val grade: String = "",
    val result: String = "",
    val head: String = "",
    val name: String = "",
    val comment: String = ""
)