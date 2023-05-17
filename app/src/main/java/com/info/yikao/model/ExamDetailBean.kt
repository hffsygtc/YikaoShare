package com.info.yikao.model

/**
 * by BiTiDaddy on 2023/5/17 15:22
 *
 */
data class ExamDetailBean(
    val Detail: ExamBean,
    val GradeInfoList: ArrayList<TeacherResultBean>
)
