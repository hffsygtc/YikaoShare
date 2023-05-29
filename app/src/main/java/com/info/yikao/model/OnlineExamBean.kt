package com.info.yikao.model

/**
 * 线上需要评分的考试
 *
 * OnLineTestItemDto {
        TestCardNo (string, optional): 准考证号 ,
        OnLineVideoUrl (string, optional): 视频地址 ,
        RealName (string, optional): 真实姓名 ,
        SubjectsStr (string, optional): 考试科目 ,
        JuryResult (integer, optional): 评分 ,
        JuryResultStr (string, optional): 评分 ,
        Remark (string, optional): 评语
    }
 */
data class OnlineExamBean(
    val TestCardNo: String,
    val OnLineVideoUrl: String,
    val RealName: String,
    val SubjectsStr: String,
    val JuryResult: Int,
    val JuryResultStr: String,
    val Remark: String,
    val StuImg1: String,
    val Sex: String,
    val Age: Int,
)