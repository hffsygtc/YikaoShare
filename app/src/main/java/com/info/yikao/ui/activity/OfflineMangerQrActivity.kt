package com.info.yikao.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityOfflineManagerQrBinding
import com.info.yikao.ext.init
import com.info.yikao.ext.loadServiceInit
import com.info.yikao.ext.showError
import com.info.yikao.ext.showLoading
import com.info.yikao.model.ClassAndSortBean
import com.info.yikao.ui.adapter.OfflineStudentListAdapter
import com.info.yikao.ui.adapter.PopPickerAdapter
import com.info.yikao.viewmodel.OfflineManagerQrViewModel
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.logw
import me.hgj.jetpackmvvm.util.get

class OfflineMangerQrActivity :
    BaseActivity<OfflineManagerQrViewModel, ActivityOfflineManagerQrBinding>() {

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res: ActivityResult? ->
            "result is $res".logw()
            val resultIntent = res?.data
            val resultStr = resultIntent?.get("result", "")
            "result str is $resultStr".logw()
        }

    var examRoomId = -1

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private val mAdapter by lazy { OfflineStudentListAdapter(arrayListOf()) }
    private val classAdapter by lazy { PopPickerAdapter(1, arrayListOf()) }
    private val sortAdapter by lazy { PopPickerAdapter(2, arrayListOf()) }

    private var underClassPicker = false
    private var underSortPicker = false

    private var selectClass: ClassAndSortBean? = null
    private var selectSort: ClassAndSortBean? = null

    override fun layoutId(): Int = R.layout.activity_offline_manager_qr

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
            mViewModel.getOfflineClassInfo(examRoomId)
        }

        //初始化recycleview
        mDatabind.teacherResultRv.init(LinearLayoutManager(this@OfflineMangerQrActivity), mAdapter)

        mDatabind.classPickerRv.init(
            LinearLayoutManager(this@OfflineMangerQrActivity),
            classAdapter
        )
        mDatabind.sortPickerRv.init(LinearLayoutManager(this@OfflineMangerQrActivity), sortAdapter)


        mDatabind.shouldCome.text = "应到(25)"
        mDatabind.waitCome.text = "候场(25)"
        mDatabind.underExam.text = "考试中(25)"
        mDatabind.finishTv.text = "已结束(25)"
        mDatabind.notCome.text = "未开始(25)"

        mDatabind.qrBtn.setOnClickListener {
            //扫码
            //点击了扫描按钮
            val intent = Intent(this@OfflineMangerQrActivity, CustomCaptureActivity::class.java)
            launcher.launch(intent)
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
//                        mViewModel.getStudentList(selectClass!!.ExamRoomId)
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
//                        mViewModel.getStudentList(selectSort!!.ExamRoomId)
                    }
                }
            } else {
                //弹窗
                underSortPicker = true
                mDatabind.sortPickerRv.visibility = View.VISIBLE
                showDefault(false, mDatabind.sortPickerBtn)
            }
        }

        loadsir.showLoading()
        mViewModel.getOfflineClassInfo(examRoomId)

//        mAdapter.setList(mViewModel.tests)
//        sortAdapter.setList(mViewModel.testClass)

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

    private fun showSuccess(name: String) {
        mDatabind.popLayout.visibility = View.VISIBLE

        mDatabind.popIcon.setBackgroundResource(R.mipmap.icon_ok_green_big)
        mDatabind.popResult.text = "签到成功！"
        mDatabind.popResult.setTextColor(Color.parseColor("#7CA861"))
        mDatabind.popResultMsg.visibility = View.GONE

        mDatabind.popStudent.text = "考生：$name"

        mDatabind.popLayout.postDelayed({
            mDatabind.popLayout.visibility = View.GONE
        }, 1500)
    }

    private fun showError(name: String, msg: String) {
        mDatabind.popLayout.visibility = View.VISIBLE

        mDatabind.popIcon.setBackgroundResource(R.mipmap.icon_x_red)
        mDatabind.popResult.text = "签到失败！"
        mDatabind.popResult.setTextColor(Color.parseColor("#FF3434"))
        mDatabind.popResultMsg.visibility = View.VISIBLE
        mDatabind.popResultMsg.text = msg

        mDatabind.popStudent.text = "考生：$name"

        mDatabind.popLayout.postDelayed({
            mDatabind.popLayout.visibility = View.GONE
        }, 1500)
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
                Snackbar.make(mDatabind.qrBtn, it.errorMsg, Snackbar.LENGTH_SHORT).show()
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
    }
}