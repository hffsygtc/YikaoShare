package com.info.yikao.model

data class StudentBean(
    val id: Int,
    val sort: Int,
    val name: String,
    val examId: String,
    val examType: String,
    val examState: Int,
)