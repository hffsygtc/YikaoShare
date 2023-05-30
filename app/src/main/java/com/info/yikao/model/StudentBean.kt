package com.info.yikao.model

/**
 * 线下考试考生的信息列表
 *
 * TestSutdentItemDto {
ItemNum (integer, optional): 序号 ,
RealName (string, optional): 姓名 ,
TestCardNo (string, optional): 准考证号 ,
JuryTotalResult (integer, optional): 综合打分 ,
JuryTotalResultStr (string, optional): 综合打分 ,
JuryResult (integer, optional): 我的打分 ,
JuryResultStr (string, optional): 我的打分
}
 */
data class StudentBean(
    val ItemNum: Int,
    var RealName: String,
    val TestCardNo: String,
    val JuryTotalResult: Int,
    val JuryTotalResultStr: String,
    val JuryResult: Int,
    val JuryResultStr: String,
    val SubjectsStr: String,
    val TestStatus: Int,
    val TestStatusStr: String,
)