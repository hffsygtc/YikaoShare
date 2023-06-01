package com.info.yikao.model

/**
 * MyTestDto {
OrderNum (string, optional): 订单编号 ,
SubjectsId (integer, optional): 科目ID ,
SubjectsName (string, optional): 科目名称 ,
SchoolId (integer, optional): 学校ID ,
SchoolName (string, optional): 学校名称 ,
Logo (string, optional): 学校logo ,
TestStart (string, optional): 考试开始时间,通常指线上考试，而线下考试需要分配考场。 ,
OnLineVideoUrl (string, optional): 线上考试视频地址 ,
ExamRoomId (integer, optional): 线下考场ID ,
TestClass (string, optional): 线下考场名称 ,
TestClassAddr (string, optional): 线下考场地址，如，川音本部-艺术楼-音乐3教室 ,
TestTimeStart (string, optional): 下线考场开考时间 ,
TestResult (integer, optional): 考级结果： 0：无结果 1：考试通过 2：考试未通过 3：结果仲裁中（如果2个评委打分差距大，则后台仲裁结果）显示：等待结果 ,
TestResultStr (string, optional): 考级结果： 0：无结果 1：考试通过 2：考试未通过 3：结果仲裁中（如果2个评委打分差距大，则后台仲裁结果）显示：等待结果 ,
JuryTotalResult (integer, optional): 评委综合评级：优秀:3，良好2，合格1，不合格0 平均规则：向上取整。如，良好+合格=良好，差值1，阀值查询基础表 ,
JuryTotalResultStr (string, optional): 评委综合评级：优秀:3，良好2，合格1，不合格0 平均规则：向上取整。如，良好+合格=良好，差值1，阀值查询基础表 ,
TestTypeId (integer, optional): 考试类型：线上：0 线下：1 ,
CertificateNo (string, optional): 通过证书的编号,通过以后，会集中颁发
}
 */
data class ExamBean(
    val RealName: String,
    val IDNumber: String,
    val OrderNum: String,
    val SubjectsId: Int,
    val SubjectsName: String,
    val SchoolId: Int,
    val SchoolName: String,
    val Logo: String,
    val TestStart: String,
    val OnLineVideoUrl: String,
    val ExamRoomId: Int,
    val TestClass: String,
    val TestClassAddr: String,
    val TestTimeStart: String,
    val TestResult: Int,
    val TestResultStr: String,
    val JuryTotalResult: Int,
    val JuryTotalResultStr: String,
    val TestTypeId: Int,
    val CertificateNo: String,
)