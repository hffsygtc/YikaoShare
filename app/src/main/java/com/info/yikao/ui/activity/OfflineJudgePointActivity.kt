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
import com.info.yikao.ext.fpx
import com.info.yikao.ext.init
import com.info.yikao.ext.loadServiceInit
import com.info.yikao.ext.showLoading
import com.info.yikao.ui.adapter.OfflineStudentListAdapter
import com.info.yikao.ui.adapter.OfflineStudentPointListAdapter
import com.info.yikao.ui.adapter.PopPickerAdapter
import com.info.yikao.viewmodel.OfflineJudgePointViewModel
import com.info.yikao.viewmodel.OfflineManagerQrViewModel
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.util.logw
import me.hgj.jetpackmvvm.util.get

class OfflineJudgePointActivity :
    BaseActivity<OfflineJudgePointViewModel, ActivityOfflineExamPointBinding>() {


    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private val mAdapter by lazy { OfflineStudentPointListAdapter(arrayListOf()) }
    private val classAdapter by lazy { PopPickerAdapter(arrayListOf()) }
    private val sortAdapter by lazy { PopPickerAdapter(arrayListOf()) }

    private var underClassPicker = false
    private var underSortPicker = false

    private var selectPos = 2

    override fun layoutId(): Int = R.layout.activity_offline_exam_point

    override fun initView(savedInstanceState: Bundle?) {

//        mDatabind.titleTv.text = "考试"
        //状态页配置

        loadsir = loadServiceInit(mDatabind.mainLayout) {
            //点击重试时触发操作
            loadsir.showLoading()
        }

        //初始化recycleview
        mDatabind.teacherResultRv.init(
            LinearLayoutManager(this@OfflineJudgePointActivity),
            mAdapter
        )

        mDatabind.classPickerRv.init(
            LinearLayoutManager(this@OfflineJudgePointActivity),
            classAdapter
        )
        mDatabind.sortPickerRv.init(
            LinearLayoutManager(this@OfflineJudgePointActivity),
            sortAdapter
        )

        showTabView()

        mDatabind.leftTab.setOnClickListener {
            if (selectPos != 1){
                selectPos = 1
                showTabView()
            }
        }
        mDatabind.rightTab.setOnClickListener {
            if (selectPos != 2){
                selectPos = 2
                showTabView()
            }
        }

        mDatabind.nameTvContent.text = "赵本山"
        mDatabind.dutyTvContent.text = "考场监考员/考场签到员"
        mDatabind.classTvContent.text = "本部艺术楼202教室"
        mDatabind.sortTvContent.text = "05月15日 10点场"
        mDatabind.typeTvContent.text = "乐曲类>流行类>钢琴1级"

        mDatabind.shouldCome.text = "应到(25)"
        mDatabind.waitCome.text = "候场(25)"
        mDatabind.underExam.text = "考试中(25)"
        mDatabind.finishTv.text = "已结束(25)"
        mDatabind.notCome.text = "未开始(25)"

        mDatabind.classPickerBtn.setOnClickListener {
            if (underSortPicker) {
                //如果场次弹窗出来了，先关了
                underSortPicker = false
                mDatabind.sortPickerRv.visibility = View.GONE
                showDefault(true, mDatabind.sortPickerBtn)
            }

            //重选考场
            if (underClassPicker) {
                //点击了确认
                underClassPicker = false
                mDatabind.classPickerRv.visibility = View.GONE
                showDefault(true, mDatabind.classPickerBtn)

                //取对应的场次显示
                if (classAdapter.selectPos != -1) {
                    val selectData = classAdapter.data.get(classAdapter.selectPos)
                }
            } else {
                //弹窗
                underClassPicker = true
                mDatabind.classPickerRv.visibility = View.VISIBLE
                showDefault(false, mDatabind.classPickerBtn)
            }

        }

        mDatabind.sortPickerBtn.setOnClickListener {
            //重选场次
            if (underClassPicker) {
                //如果场次弹窗出来了，先关了
                underClassPicker = false
                mDatabind.classPickerRv.visibility = View.GONE
                showDefault(true, mDatabind.classPickerBtn)
            }

            //重选考场
            if (underSortPicker) {
                //点击了确认
                underSortPicker = false
                mDatabind.sortPickerRv.visibility = View.GONE
                showDefault(true, mDatabind.sortPickerBtn)

                //取对应的场次显示
                if (sortAdapter.selectPos != -1) {
                    val selectData = sortAdapter.data.get(sortAdapter.selectPos)
                }
            } else {
                //弹窗
                underSortPicker = true
                mDatabind.sortPickerRv.visibility = View.VISIBLE
                showDefault(false, mDatabind.sortPickerBtn)
            }
        }


        mAdapter.setList(mViewModel.tests)
        classAdapter.setList(mViewModel.testClass)
        sortAdapter.setList(mViewModel.testClass)

    }

    private fun showDefault(default: Boolean, tv: TextView) {
        if (default) {
            tv.apply {
                setBackgroundResource(R.drawable.blue_line_white_10)
                setTextColor(Color.parseColor("#52618B"))
                text = "重选"
            }
        } else {
            tv.apply {
                setBackgroundResource(R.drawable.green_line_white_10)
                setTextColor(Color.parseColor("#7CA861"))
                text = "确认"
            }
        }
    }

    private fun showTabView() {
        when (selectPos) {
            1 -> {
                mDatabind.leftTab.apply {
                    setTextColor(Color.parseColor("#333333"))
                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }
                mDatabind.leftLine.visibility = View.VISIBLE
                mDatabind.rightTab.apply {
                    setTextColor(Color.parseColor("#999999"))
                    typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                }
                mDatabind.rightLine.visibility = View.GONE

                mDatabind.leftContentLayout.visibility = View.VISIBLE
                mDatabind.rightContentLayout.visibility = View.GONE
            }
            2 -> {
                mDatabind.rightTab.apply {
                    setTextColor(Color.parseColor("#333333"))
                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                }
                mDatabind.rightLine.visibility = View.VISIBLE
                mDatabind.leftTab.apply {
                    setTextColor(Color.parseColor("#999999"))
                    typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                }
                mDatabind.leftLine.visibility = View.GONE

                mDatabind.leftContentLayout.visibility = View.GONE
                mDatabind.rightContentLayout.visibility = View.VISIBLE
            }
        }
    }
}