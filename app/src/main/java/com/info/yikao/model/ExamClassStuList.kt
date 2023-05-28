package com.info.yikao.model

/**
 * 线下考试考生的信息列表
 *
 * TestSutdentListDto {
TotalNum (integer, optional): 应到 ,
WaitNum (integer, optional): 候场 ,
TestingNum (integer, optional): 考试中 ,
FinishNum (integer, optional): 已结束 ,
NotArrivalNum (integer, optional): 未到 ,
List (Array[TestSutdentItemDto], optional): 考生列表
}
 */
data class ExamClassStuList(
    val TotalNum: Int,
    var WaitNum: Int,
    val TestingNum: Int,
    val FinishNum: Int,
    val NotArrivalNum: Int,
    val List: ArrayList<StudentBean>
)