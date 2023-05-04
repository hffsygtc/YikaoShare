package com.info.yikao.model

data class MainListWarpper(
    val type:Int,
    val titleName: String?,
    val newsBean: NewsBean?,
    val schoolBean: SchoolBean?,
    val streetBean: StreetShowBean?,
)