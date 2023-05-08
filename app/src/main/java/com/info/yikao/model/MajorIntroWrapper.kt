package com.info.yikao.model

data class MajorIntroWrapper(
    val type: Int,
    val signInfo: MajorTimeInfo?,
    val examBean: ExamContent?,
    val descInfo: SectionInfo?,
    val isHead: Boolean = false
)

//报考信息
data class MajorTimeInfo(
    val signTime: String,
    val examTime: String,
    val majorName: String,
    val method: String
)

//考试内容
data class ExamContent(
    val sort: String,
    val name: String,
    val must: Boolean,
    val content: String
)

data class SectionInfo(
    val name: String,
    val content: String
)