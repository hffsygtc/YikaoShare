package com.info.yikao.model

/**
 * 头条新闻
 */
data class NewsBean(
    val ArticleTypeName: String,
    val ArticleId: Int,
    val Title: String,
    val ContentStr: String,
    val ArticleTypeId:Int,
    val Writer:String,
    val LookNum:Int,
    val AddTime:String,
    val IconUrl:String,
)