package com.info.yikao.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityExamResultBinding
import com.info.yikao.ext.*
import com.info.yikao.model.TeacherResultBean
import com.info.yikao.ui.adapter.ExamTeacherResultAdapter
import com.info.yikao.viewmodel.ExamResultViewModel
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.parseState
import java.util.regex.Matcher
import java.util.regex.Pattern

class ExamResultActivity : BaseActivity<ExamResultViewModel, ActivityExamResultBinding>() {
    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private val mAdapter by lazy { ExamTeacherResultAdapter(arrayListOf()) }

    private var examNum = ""
    private var online = false
    private var examName = ""

    private var certNum = ""

    override fun layoutId(): Int = R.layout.activity_exam_result

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.titleBackBtn.setOnClickListener {
            finish()
        }

        intent?.let {
            online = it.getBooleanExtra("online", false)
            examNum = it.getStringExtra("id") ?: ""
            examName = it.getStringExtra("name") ?: ""
        }

        if (online) {
            //线上考试，需要显示考生的视频
            mDatabind.videoView.visibility = View.VISIBLE
            mDatabind.offlineClassTv.visibility = View.GONE
        } else {
            //不显示视频
            mDatabind.videoView.visibility = View.GONE
            mDatabind.offlineClassTv.visibility = View.VISIBLE
        }

        mDatabind.titleTv.text = examName
        //状态页配置
        loadsir = loadServiceInit(mDatabind.mainLayout) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getExamDetail(examNum)
        }

        //初始化recycleview
        mDatabind.teacherResultRv.init(LinearLayoutManager(this@ExamResultActivity), mAdapter)

        mDatabind.saveCertBook.setOnClickListener {
//            if (certNum.canShow()) {
//                //有证书ID才跳转
//            }
            val intent = Intent(this@ExamResultActivity,CertiDetailActivity::class.java)
            intent.putExtra("id",examNum)
            intent.putExtra("name",examName)
            startActivity(intent)
        }

        loadsir.showLoading()
        mViewModel.getExamDetail(examNum)

    }

    override fun createObserver() {
        mViewModel.examDetail.observe(this) { result ->
            parseState(result, {
                loadsir.showSuccess()
                //显示界面
                mDatabind.nameTvContent.text = ""
                mDatabind.idCardTvContent.text = ""
                mDatabind.majorTvContent.text = it.Detail.SubjectsName
                mDatabind.timeTvContent.text = it.Detail.TestTimeStart
                mDatabind.offlineClassTvContent.text = it.Detail.TestClassAddr

                if (it.Detail.CertificateNo.canShow()) {
                    //有证书领取
                    certNum = it.Detail.CertificateNo
                    mDatabind.saveCertBook.setBackgroundResource(R.drawable.blue_corner_15)
                } else {
                    mDatabind.saveCertBook.setBackgroundResource(R.drawable.grey_cc_corner_15)
                }

                //处理评级
                if (it.GradeInfoList != null && it.GradeInfoList.isNotEmpty()) {
                    //如果有数据
                    val headGrade = TeacherResultBean(
                        1,
                        -1,
                        "",
                        it.Detail.TestResult,
                        it.Detail.JuryTotalResultStr,
                        -1,
                        "",
                        ""
                    )

                    it.GradeInfoList.add(0, headGrade)
                    mAdapter.setList(it.GradeInfoList)

                } else {
                    //没有数据
                    val emptyGrade = arrayListOf(TeacherResultBean(3, -1, "", -1, "", -1, "", ""))
                    mAdapter.setList(emptyGrade)
                }
            }, {
                loadsir.showError()
            })

        }
    }
}