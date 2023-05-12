package com.info.yikao.viewmodel

import com.info.yikao.model.TeacherResultBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class ExamResultViewModel : BaseViewModel() {

    val tests = arrayListOf<TeacherResultBean>(TeacherResultBean(1),TeacherResultBean(2),TeacherResultBean(2))

}