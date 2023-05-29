package com.info.yikao.viewmodel

import com.info.yikao.model.*
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class OnlineJudgePointViewModel : BaseViewModel() {

    val stuInfoBean = UnPeekLiveData<ResultState<OnlineExamBean>>()

    val stuGradeBean = UnPeekLiveData<NetGradeBean>()

    val templateContents = arrayListOf<String>()

    fun getStudentInfo(id: String) {
        request({ apiService.getStuInfoById(id) }, stuInfoBean)

        request({ apiService.getStuGradeInfoById(id) }, {
            //获取考生的评分信息
            stuGradeBean.value = it
            templateContents.clear()
            for (temp in it.Template) {
                templateContents.add(temp.TemplateStr)
            }
        }, {})
    }

    fun submitUserGrade(juryResult: String, remark: String, cardNum: String) {
        val resultType = when (juryResult) {
            "良好" -> 2
            "合格" -> 1
            "不合格" -> 0
            else -> 3
        }

        val postGradeBean =
            ExamGradeBean(resultType, juryResult, remark, cardNum)
        request({ apiService.postStudentExamGrade(postGradeBean) }, {
            //提交考生的评分
        }, {})
    }

}