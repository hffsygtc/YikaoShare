package com.info.yikao.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivitySysMsgDetailBinding
import com.info.yikao.databinding.ActivityTopNewsDetailBinding
import com.info.yikao.ext.canShow
import com.info.yikao.ext.loadServiceInit
import com.info.yikao.ext.showError
import com.info.yikao.ext.showLoading
import com.info.yikao.viewmodel.FastNewsDetailViewModel
import com.info.yikao.viewmodel.SysMsgViewModel
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 公告快讯的详情页
 */
class SysMsgDetailActivity :
    BaseActivity<SysMsgViewModel, ActivitySysMsgDetailBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private var msgId = -1

    override fun layoutId(): Int = R.layout.activity_sys_msg_detail

    private var detailUrl = ""

    override fun initView(savedInstanceState: Bundle?) {

        msgId = intent.getIntExtra("messageId", -1)

        mDatabind.titleBackBtn.setOnClickListener {
            finish()
        }

        //状态页配置
        loadsir = loadServiceInit(mDatabind.mainLayout) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getArticleDetail(msgId)
        }

        mDatabind.titleTv.text = "站内信"

        mDatabind.nextBtn.setOnClickListener {
            if (detailUrl.canShow()) {
                //有详情页
                val intent = Intent(this@SysMsgDetailActivity, WebviewActivity::class.java)
                intent.putExtra("url", detailUrl)
                startActivity(intent)
            }
        }

        loadsir.showLoading()
        mViewModel.getArticleDetail(msgId)

    }

    override fun createObserver() {
        mViewModel.detail.observe(this) { result ->
            parseState(result, {
                loadsir.showSuccess()

                mDatabind.newsDetailTitle.text = it.Title
                mDatabind.detailPublishTime.text = it.AddTime

                mDatabind.detailContentTv.text = it.MessageStr

                detailUrl = it.GoUrl

                if (detailUrl.canShow()) {
                    mDatabind.nextBtn.visibility = View.VISIBLE
                } else {
                    mDatabind.nextBtn.visibility = View.GONE
                }

            }, {
                loadsir.showError()
                Snackbar.make(mDatabind.nextBtn, it.errorMsg, Snackbar.LENGTH_SHORT).show()
            })

        }
    }


}