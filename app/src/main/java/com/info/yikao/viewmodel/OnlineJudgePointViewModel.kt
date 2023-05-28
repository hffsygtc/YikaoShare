package com.info.yikao.viewmodel

import com.info.yikao.model.ClassAndSortBean
import com.info.yikao.model.StudentBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class OnlineJudgePointViewModel : BaseViewModel() {

    val tests = arrayListOf<StudentBean>()

    val testClass = arrayListOf<ClassAndSortBean>(
//        ClassAndSortBean(1,"111111"),
//        ClassAndSortBean(2,"222222"),
//        ClassAndSortBean(3,"333333"),
    )

}