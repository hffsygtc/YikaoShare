package com.info.yikao.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityOfflineExamPointBinding
import com.info.yikao.databinding.ActivityOfflineManagerQrBinding
import com.info.yikao.databinding.ActivityOnlineExamPointBinding
import com.info.yikao.ext.fpx
import com.info.yikao.ext.init
import com.info.yikao.ext.loadServiceInit
import com.info.yikao.ext.showLoading
import com.info.yikao.ui.adapter.OfflineStudentListAdapter
import com.info.yikao.ui.adapter.OfflineStudentPointListAdapter
import com.info.yikao.ui.adapter.PopPickerAdapter
import com.info.yikao.viewmodel.OfflineJudgePointViewModel
import com.info.yikao.viewmodel.OfflineManagerQrViewModel
import com.info.yikao.viewmodel.OnlineJudgePointViewModel
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.util.logw
import me.hgj.jetpackmvvm.util.get

class OnlineJudgePointActivity :
    BaseActivity<OnlineJudgePointViewModel, ActivityOnlineExamPointBinding>() {


    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private val mAdapter by lazy { OfflineStudentPointListAdapter(arrayListOf()) }
    private val classAdapter by lazy { PopPickerAdapter(1,arrayListOf()) }
    private val sortAdapter by lazy { PopPickerAdapter(2,arrayListOf()) }

    private var underClassPicker = false
    private var underSortPicker = false

    private var selectPos = 2

    override fun layoutId(): Int = R.layout.activity_online_exam_point

    override fun initView(savedInstanceState: Bundle?) {

//        mDatabind.titleTv.text = "考试"
        //状态页配置

        loadsir = loadServiceInit(mDatabind.mainLayout) {
            //点击重试时触发操作
            loadsir.showLoading()
        }

    }

}