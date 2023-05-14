package com.info.yikao.viewmodel

import com.info.yikao.model.ClassAndSortBean
import com.info.yikao.model.StudentBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class OfflineJudgePointViewModel : BaseViewModel() {

    val tests = arrayListOf<StudentBean>(
        StudentBean(1,1,"赵本山1","111111","乐曲类>流行类>钢琴1级",1),
        StudentBean(2,2,"赵本山2","2222222","乐曲类>流行类>钢琴1级",2),
        StudentBean(2,3,"赵本山3","3333333","乐曲类>流行类>钢琴1级",3)
    )

    val testClass = arrayListOf<ClassAndSortBean>(
        ClassAndSortBean(1,"111111"),
        ClassAndSortBean(2,"222222"),
        ClassAndSortBean(3,"333333"),
    )

}