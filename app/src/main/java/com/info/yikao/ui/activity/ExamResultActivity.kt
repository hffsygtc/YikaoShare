package com.info.yikao.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.info.yikao.MainActivity
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.*
import com.info.yikao.ext.*
import com.info.yikao.model.OrderBean
import com.info.yikao.ui.adapter.ExamTeacherResultAdapter
import com.info.yikao.ui.adapter.SchoolMajorExpandedAdapter
import com.info.yikao.ui.adapter.UserOrderListAdapter
import com.info.yikao.viewmodel.ExamResultViewModel
import com.info.yikao.viewmodel.SplashViewModel
import com.info.yikao.viewmodel.UploadVideoViewModel
import com.info.yikao.viewmodel.UserOrderViewModel
import com.info.yikao.weight.DefineLoadMoreView
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.logw

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