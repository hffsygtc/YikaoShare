package com.info.yikao.model

/**
 *
Message {
MessageId (integer, optional): ,
MemberId (integer, optional): ,
Title (string, optional): 标题 ,
MessageStr (string, optional): 内容 ,
GoUrl (string, optional): 跳转链接 ,
AddTime (string, optional):
}
 */
data class MessageBean(
    var MessageId:Int,
    val MemberId: Int,
    val Title: String,
    val MessageStr: Int,
    val GoUrl: String,
    val AddTime: Int
)