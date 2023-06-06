package com.info.yikao.ui.activity

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivitySignPeopleCertifyBinding
import com.info.yikao.viewmodel.PeopleCertifyViewModel
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.logw

/**
 * 开始录入人像认证
 */
class SignUpCertifyActivity :
    BaseActivity<PeopleCertifyViewModel, ActivitySignPeopleCertifyBinding>() {

    var orderNum = ""

    override fun layoutId(): Int = R.layout.activity_sign_people_certify

    override fun initView(savedInstanceState: Bundle?) {

        orderNum = intent.getStringExtra("orderNum") ?: ""

        mViewModel.getUserMemberInfo()

        mDatabind.nextBtn.setOnClickListener {
            //进入人脸识别的sdk逻辑
            //todo 人脸识别
            val intent = Intent(this, CaptureRecordActivity::class.java)
            intent.putExtra("orderNum", orderNum)
            startActivity(intent)
        }
    }

    override fun createObserver() {
        mViewModel.memberInfo.observe(this) { result ->
            parseState(result, {
                "member info is $it".logw()
                mDatabind.nameTv.text = "考生姓名：${it.RealName}"
                mDatabind.idCardTv.text = "身份证号：${it.IDNumber}"
            }, {
                Snackbar.make(mDatabind.nextBtn, it.errorMsg, Snackbar.LENGTH_SHORT).show()
            })
        }
    }


}