package com.info.yikao.model

/**
TestCardInfoDto {
SchoolName (string, optional): 学校名称 ,
Logo (string, optional): 学校logo ,
Title (string, optional): 标题 ,
RealName (string, optional): 姓名 ,
Sex (string, optional): 性别 ,
TestCardNo (string, optional): 准考证号 ,
TestClass (string, optional): 考场 ,
TestClassAddr (string, optional): 考场地点 ,
SubjectsName (string, optional): 等级 ,
SubjectsNameParent (string, optional): 专业 ,
TestTimeStart (string, optional): 考试时间 ,
TestContent (string, optional): 考试内容/演奏曲目 ,
QrUrl (string, optional): 二维码地址
}
 */
data class UserCardInfoBean(
    val SchoolName: String,
    val Logo: String,
    val Title: String,
    val RealName: String,
    val Sex: String,
    val TestCardNo: String,
    val TestClass: String,
    val TestClassAddr: String,
    val SubjectsName: String,
    val SubjectsNameParent: String,
    val TestTimeStart: String,
    val TestContent: String,
    val QrUrl: String,
    val StuImg1: String,
    )