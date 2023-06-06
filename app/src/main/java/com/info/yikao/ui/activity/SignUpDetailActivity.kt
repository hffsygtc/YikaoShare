package com.info.yikao.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivitySignUpDetailBinding
import com.info.yikao.ext.loadServiceInit
import com.info.yikao.ext.showError
import com.info.yikao.ext.showLoading
import com.info.yikao.viewmodel.SignUpDetailViewModel
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 报名详情
 */
class SignUpDetailActivity : BaseActivity<SignUpDetailViewModel, ActivitySignUpDetailBinding>() {

    var orderNum = ""

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    override fun layoutId(): Int = R.layout.activity_sign_up_detail

    override fun initView(savedInstanceState: Bundle?) {

        orderNum = intent?.getStringExtra("id") ?: ""

        mDatabind.titleBackBtn.setOnClickListener {
            finish()
        }

        //状态页配置
        loadsir = loadServiceInit(mDatabind.mainLayout) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getOrderDetail(orderNum)
        }


        mDatabind.enterExamBtn.setOnClickListener {
            val intent = Intent(this@SignUpDetailActivity, SignUpCertifyActivity::class.java)
            intent.putExtra("orderNum",orderNum)
            startActivity(intent)
        }


        loadsir.showLoading()
        mViewModel.getOrderDetail(orderNum)

    }

    override fun createObserver() {
        mViewModel.orderDetail.observe(this) { result ->
            parseState(result, {
                loadsir.showSuccess()

                mDatabind.schoolName.text = it.SchoolName
                mDatabind.examName.text = it.SubjectsName
                mDatabind.timeTv.text = it.TestStart

                when (it.TestStatus) {
//                    1 -> {
//                        mDatabind.examStateTv.text = "候场中"
//                        mDatabind.examStateTv.setTextColor(Color.parseColor("#7CA861"))
//                    }
                    1 -> {
                        mDatabind.examStateTv.text = "考试中"
                        mDatabind.examStateTv.setTextColor(Color.parseColor("#7CA861"))
                        mDatabind.enterExamGroup.visibility = View.VISIBLE
                        mDatabind.enterExamBtn.text = "进入考试"
                    }
                    2 -> {
                        mDatabind.examStateTv.text = "已结束"
                        mDatabind.examStateTv.setTextColor(Color.parseColor("#FF3434"))
                        mDatabind.enterExamGroup.visibility = View.GONE
                    }
                    else -> {
                        //0
                        mDatabind.examStateTv.text = "报名中"
                        mDatabind.examStateTv.setTextColor(Color.parseColor("#FF8D31"))
                        mDatabind.enterExamGroup.visibility = View.VISIBLE
                        mDatabind.enterExamBtn.text = "查看准考证"
                    }
                }


                mDatabind.studentName.text = it.RealName
                mDatabind.idCardTv.text = it.IDNumber
                mDatabind.examResultTv

                when (it.TestResult) {
                    1 -> {
                        //通过
                        mDatabind.examResultTv.setTextColor(Color.parseColor("#7CA861"))
                        mDatabind.examResultTv.text = "通过"
                    }
                    2 -> {
                        //未通过
                        mDatabind.examResultTv.setTextColor(Color.parseColor("#FF3434"))
                        mDatabind.examResultTv.text = "未通过"
                    }
                    else -> {
                        //等待结果
                        mDatabind.examResultTv.setTextColor(Color.parseColor("#FF8D31"))
                        mDatabind.examResultTv.text = "等待结果"
                    }
                }

                mDatabind.examPayCastTv.text = "￥${it.Amount}"
                mDatabind.orderInfoId.text = it.OrderNum

            }, {
                loadsir.showError()
            })

        }
    }


}