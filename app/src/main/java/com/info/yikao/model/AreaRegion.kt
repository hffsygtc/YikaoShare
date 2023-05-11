package com.info.yikao.model

/**
 * 省市区地址数据
 */
data class AreaRegion(
    val Id:Int,
    val Code:String,
    val Name:String,
    val ParentId:Int,
    val LevelId:Int,
)