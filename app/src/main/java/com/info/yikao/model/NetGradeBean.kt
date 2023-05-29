package com.info.yikao.model

/**
 * by BiTiDaddy on 2023/5/29 15:44
 * 获取的用户评分和快捷评语
 */
data class NetGradeBean(
    var JuryResult: Int,
    var JuryResultStr: String,
    var Remark: String,
    var Template: ArrayList<GradeTemplate>,
)

data class GradeTemplate(
    val RemarkTemplateId: Int,
    val TemplateStr: String
)