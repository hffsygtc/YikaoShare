package com.info.yikao.viewmodel

import com.info.yikao.model.*
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class OfflineJudgePointViewModel : BaseViewModel() {

    var classDetail = UnPeekLiveData<ResultState<ExamClassBean>>()

    var currentStu = UnPeekLiveData<ResultState<OnlineExamBean>>()

    var pickClassList = UnPeekLiveData<ArrayList<ClassAndSortBean>>()
    var pickSortList = UnPeekLiveData<ArrayList<ClassAndSortBean>>()

    var fullStuListBean = UnPeekLiveData<ExamClassStuList>()

    val stuGradeBean = UnPeekLiveData<NetGradeBean>()

    val templateContents = arrayListOf<String>()

    val confirmOk = UnPeekLiveData<String>()

    fun getOfflineClassInfo(id: Int) {
        request({ apiService.getOfflineClassDetail(id) }, classDetail)
        getClassList()
        getStudentList(id)
    }

    fun getClassList() {
        request({ apiService.getOfflineClassList() }, {
            pickClassList.value = it
        }, {})
    }

    fun getSortList(id: String) {
        request({ apiService.getOfflineSortList(id) }, {
            pickSortList.value = it
        }, {})
    }

    /**
     * 获取当前考场的学生信息
     */
    fun getCurStudent(id: Int) {
        request({ apiService.getCurrentTestStu(id) }, currentStu)
    }

    fun getStuGrade(id: String){
        request({ apiService.getStuGradeInfoById(id) }, {
            //获取考生的评分信息
            stuGradeBean.value = it
            templateContents.clear()
            for (temp in it.Template) {
                templateContents.add(temp.TemplateStr)
            }
        }, {})
    }

    fun getStudentList(id: Int) {
        request({ apiService.getCurrentExamStuList(id) }, {
            //获取到当前考场的考生信息
            fullStuListBean.value = it
        }, {})
    }

    fun getStudentGradeInfo(id: String) {
        request({ apiService.getStuInfoById(id) }, currentStu)
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
            confirmOk.value = "ok"
        }, {
            confirmOk.value = it.errorMsg
        })
    }

}