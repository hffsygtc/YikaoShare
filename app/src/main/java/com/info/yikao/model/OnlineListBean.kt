package com.info.yikao.model

/**
 * 线上考试的数据
 *
 * OnLineTestListDto {
SubjectsStr (string, optional): 考试科目 ,
TestStartStr (string, optional): 考试场次 ,
GradedList (Array[OnLineTestItemDto], optional): 已评分列表 ,
UnGradedList (Array[OnLineTestItemDto], optional): 未评分列表
}
 */
data class OnlineListBean(
    val SubjectsStr: String,
    val TestStartStr: String,
    val GradedList: ArrayList<OnlineExamBean>,
    val UnGradedList: ArrayList<OnlineExamBean>,
    var open:Boolean = false
)