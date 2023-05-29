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
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.google.android.material.snackbar.Snackbar
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityOfflineExamPointBinding
import com.info.yikao.databinding.ActivityOfflineManagerQrBinding
import com.info.yikao.ext.*
import com.info.yikao.model.ClassAndSortBean
import com.info.yikao.model.StudentBean
import com.info.yikao.ui.adapter.OfflineStudentListAdapter
import com.info.yikao.ui.adapter.OfflineStudentPointListAdapter
import com.info.yikao.ui.adapter.PopPickerAdapter
import com.info.yikao.viewmodel.OfflineJudgePointViewModel
import com.info.yikao.viewmodel.OfflineManagerQrViewModel
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.logw
import me.hgj.jetpackmvvm.util.get

class OfflineJudgePointActivity :
    BaseActivity<OfflineJudgePointViewModel, ActivityOfflineExamPointBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private val mAdapter by lazy { OfflineStudentPointListAdapter(arrayListOf()) }
    private val classAdapter by lazy { PopPickerAdapter(1,arrayListOf()) }
    private val sortAdapter by lazy { PopPickerAdapter(2,arrayListOf()) }

    private var underClassPicker = false
    private var underSortPicker = false

    private var selectPos = 1

    var examRoomId = -1
    private var selectClass: ClassAndSortBean? = null
    private var selectSort: ClassAndSortBean? = null

    var stuId = ""

    val juryResultList = arrayListOf("优秀", "良好", "合格", "不合格")

    var stuJuryResult = "优秀"
    var stuJuryRemark = ""

    private val resultPicker: OptionsPickerBuilder by lazy {
        OptionsPickerBuilder(
            this
        ) { options1, _, _, _ ->
            "picker check $options1".logw()
            stuJuryResult = juryResultList[options1]
            mDatabind.stuScoreTvContent.text = stuJuryResult
        }
    }

    private val quickPicker: OptionsPickerBuilder by lazy {
        OptionsPickerBuilder(
            this
        ) { options1, _, _, _ ->
            "picker check $options1".logw()
            stuJuryRemark = mViewModel.templateContents[options1]
            mDatabind.stuFastScoreTvContent.text = stuJuryRemark
            mDatabind.stuScorePonitTvContent.setText(stuJuryRemark)
        }
    }


    override fun layoutId(): Int = R.layout.activity_offline_exam_point

    override fun initView(savedInstanceState: Bundle?) {
//        mDatabind.titleTv.text = "考试"
        //状态页配置
        intent?.let {
            examRoomId = it.getIntExtra("id", -1)
        }
        mDatabind.titleBackBtn.setOnClickListener {
            finish()
        }

        loadsir = loadServiceInit(mDatabind.mainLayout) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getCurStudent(examRoomId)
            mViewModel.getOfflineClassInfo(examRoomId)
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
                    selectClass = classAdapter.data[classAdapter.selectPos]
                    if (selectClass != null) {
                        mDatabind.classTvContent.text = selectClass?.TestClass
                        mDatabind.sortTvContent.text = selectClass?.TestTimeStart
                        //重新获取考场相关的场次
                        mViewModel.getSortList(selectClass!!.TestClass)
                        //获取对应场次的学生信息
                        mViewModel.getStudentList(selectClass!!.ExamRoomId)
                    }
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
                    selectSort = sortAdapter.data.get(sortAdapter.selectPos)
                    if (selectSort != null) {
                        mDatabind.sortTvContent.text = selectSort?.TestTimeStart
                        //获取对应场次的学生信息
                        mViewModel.getStudentList(selectSort!!.ExamRoomId)
                    }
                }
            } else {
                //弹窗
                underSortPicker = true
                mDatabind.sortPickerRv.visibility = View.VISIBLE
                showDefault(false, mDatabind.sortPickerBtn)
            }
        }

        mAdapter.setOnItemClickListener { adapter, view, position ->
            //点击了项目，跳转到对应的考生评分
            if (selectPos != 1){
                selectPos = 1
                showTabView()
            }

            val student = mAdapter.data[position] as StudentBean
            mViewModel.getStudentGradeInfo(student.TestCardNo)
        }

        mDatabind.refreshBtn.setOnClickListener {
            //当前考生信息，点击刷新
            loadsir.showLoading()
            mViewModel.getCurStudent(examRoomId)
            mViewModel.getOfflineClassInfo(examRoomId)
        }

        loadsir.showLoading()
        mViewModel.getCurStudent(examRoomId)
        mViewModel.getOfflineClassInfo(examRoomId)

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

    override fun createObserver() {
        mViewModel.classDetail.observe(this) { result ->
            parseState(result, {
                loadsir.showSuccess()
                mDatabind.nameTvContent.text = it.JuryName
                mDatabind.dutyTvContent.text = it.Duty
                mDatabind.classTvContent.text = it.TestClass
                mDatabind.sortTvContent.text = it.TestTimeStart
                mDatabind.typeTvContent.text = it.SubjectsStr

                //获取可选的场次列表
                mViewModel.getSortList(it.TestClass)

            }, {
                loadsir.showError()
                Snackbar.make(mDatabind.mainLayout, it.errorMsg, Snackbar.LENGTH_SHORT).show()
            })
        }

        mViewModel.pickClassList.observe(this) {
            if (it != null && it.size > 1) {
                mDatabind.classPickerBtn.visibility = View.VISIBLE
                classAdapter.setList(it)
            }
        }

        mViewModel.pickSortList.observe(this) {
            if (it != null && it.size > 1) {
                mDatabind.sortPickerBtn.visibility = View.VISIBLE
                sortAdapter.setList(it)
            }
        }

        mViewModel.fullStuListBean.observe(this){
            //获取到相应的考生信息
            mDatabind.shouldCome.text = "应到(${it.TotalNum})"
            mDatabind.waitCome.text = "候场(${it.WaitNum})"
            mDatabind.underExam.text = "考试中(${it.TestingNum})"
            mDatabind.finishTv.text = "已结束(${it.FinishNum})"
            mDatabind.notCome.text = "未开始(${it.NotArrivalNum})"

            mAdapter.setList(it.List)
        }

        //当前考生的信息
    }
}