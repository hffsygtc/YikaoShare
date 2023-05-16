package com.info.yikao.model

/**
 *
ApplyOrderDto {
OrderNum (string, optional): 订单编号 ,
Amount (string, optional): 订单金额 ,
PayStatus (string, optional): 订单状态，0：未支付 1：已支付 2：已关闭 ,
SubjectsId (integer, optional): 科目ID ,
SubjectsName (string, optional): 科目名称 ,
SchoolId (integer, optional): 学校ID ,
SchoolName (string, optional): 学校名称 ,
Logo (string, optional): 学校logo ,
TestStart (string, optional): 考试开始时间 ,
AddTime (string, optional): 订单生成时间 ,
ExpiresMins (integer, optional): 过期时间，分钟数
}

 */
data class OrderBean(
    val OrderNum: String,
    val Amount: String,
    var PayStatus: String,
    val SubjectsId: Int,
    val SubjectsName: String,
    val SchoolId: Int,
    val SchoolName: String,
    val Logo: String,
    val TestStart: String,
    val AddTime: String,
    val ExpiresMins: Int,
)