package com.info.yikao.model

/**
 *  准考证扫码结果内容
 */
data class QrCardStateBean(
    val time: String,
    val content: String,
    val state: Int
)