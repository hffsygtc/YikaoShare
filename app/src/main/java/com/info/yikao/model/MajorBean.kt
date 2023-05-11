package com.info.yikao.model

data class MajorBean(
    val SubjectsId: Int,
    val SubjectsName: String,
    val ParentId: Int,
    val SchoolId: Int,
    val Price: Float,
    val ApplyStart: String,
    val ApplyEnd: String,
    val TestStart: String,
    val TestEnd: String,
    val TestContent: String,
    val TestType: String,
    val TestTypeId: Int,
    val AddTime: String,
)

data class MajorGroupBean(
    val Item: MajorBean,
    val Children: ArrayList<MajorGroupBean>?,
    var open:Boolean = false
)


