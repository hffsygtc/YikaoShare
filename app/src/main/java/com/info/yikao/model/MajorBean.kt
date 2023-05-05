package com.info.yikao.model

data class MajorBean(
    val id: Int,
    val name: String
)

data class MajorSubTypeBean(
    val name: String,
    val majors: ArrayList<MajorBean>
)

data class MajorTypeBean(
    val name: String,
    val majors: ArrayList<MajorSubTypeBean>,
    var open: Boolean = false
)