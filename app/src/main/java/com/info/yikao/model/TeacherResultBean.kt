package com.info.yikao.model

/**
 *
GradeInfo {
GradeInfoId (integer, optional): ,
TestCardNo (string, optional): 准考证号 ,
JuryResult (integer, optional): 评委评级：优秀:3，良好2，合格1，不合格0 ,
JuryResultStr (string, optional): 评委评级：优秀，良好，合格，不合格 ,
MemberId (integer, optional): 评委的id ,
Remark (string, optional): 评委评语 ,
AddTime (string, optional):
}
 */
data class TeacherResultBean(
    var type:Int,
    val GradeInfoId: Int,
    val TestCardNo: String,
    val JuryResult: Int,
    val JuryResultStr: String,
    val MemberId: Int,
    val Remark: String,
    val AddTime: String,
)