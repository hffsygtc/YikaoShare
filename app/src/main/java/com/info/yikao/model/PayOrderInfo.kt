package com.info.yikao.model

data class PayOrderInfo(
    val appid: String?,
    val partnerid: String?,
    val timestamp: String?,
    val noncestr: String?,
    val prepayid: String?,
    val sign: String?,
    val OrderNum: String,
    val Amount: Float,
)