package com.info.yikao.model

/**
 * 登录的验证码
 */
data class LoginCodeImg(
    val ImgBase64Str: String,
    val ImgId: String
)