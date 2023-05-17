package com.info.yikao.model

/**
 * 基本配置信息
 */
data class InitConfig(
    val OnLineVideoLocalUpload: Int,
    val OnLineVideoRecordUpload: Int,
    val TestResultDiff: Int,
    val PostCerFee: Float,
    val ImgPrefix: String,
)