package com.info.yikao.ui.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityExamResultBinding
import com.info.yikao.ext.init
import com.info.yikao.ext.loadServiceInit
import com.info.yikao.ext.showLoading
import com.info.yikao.ui.adapter.ExamTeacherResultAdapter
import com.info.yikao.viewmodel.ExamResultViewModel
import com.kingja.loadsir.core.LoadService

class ExamResultActivity : BaseActivity<ExamResultViewModel, ActivityExamResultBinding>() {
    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private val mAdapter by lazy { ExamTeacherResultAdapter(arrayListOf()) }


    override fun layoutId(): Int = R.layout.activity_exam_result

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.titleTv.text = "考试"
        //状态页配置
        loadsir = loadServiceInit(mDatabind.mainLayout) {
            //点击重试时触发操作
            loadsir.showLoading()
        }

        //初始化recycleview
        mDatabind.teacherResultRv.init(LinearLayoutManager(this@ExamResultActivity), mAdapter)
            .let {

            }


        mAdapter.setList(mViewModel.tests)

    }
}