package com.info.yikao.ui.activity

import android.content.Intent
import android.os.Bundle
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivitySchoolDetailBinding
import com.info.yikao.databinding.ActivitySignPayBinding
import com.info.yikao.databinding.ActivitySignUpDetailBinding
import com.info.yikao.databinding.ActivityTopNewsDetailBinding
import com.info.yikao.ext.loadServiceInit
import com.info.yikao.ext.showError
import com.info.yikao.ext.showLoading
import com.info.yikao.viewmodel.FastNewsDetailViewModel
import com.info.yikao.viewmodel.SignUpDetailViewModel
import com.info.yikao.viewmodel.SignUpPayViewModel
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 公告快讯的详情页
 */
class FastNewsDetailActivity :
    BaseActivity<FastNewsDetailViewModel, ActivityTopNewsDetailBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private var articleId = -1

    override fun layoutId(): Int = R.layout.activity_top_news_detail

    override fun initView(savedInstanceState: Bundle?) {

        articleId = intent.getIntExtra("article_id", -1)

        mDatabind.titleBackBtn.setOnClickListener {
            finish()
        }

        //状态页配置
        loadsir = loadServiceInit(mDatabind.mainLayout) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getArticleDetail(articleId)
        }

        mDatabind.titleTv.text = "公告详情"

        loadsir.showLoading()
        mViewModel.getArticleDetail(articleId)

    }

    override fun createObserver() {
        mViewModel.articleDetail.observe(this) { result ->
            parseState(result, {
                loadsir.showSuccess()

                mDatabind.newsDetailTitle.text = it.Title
                mDatabind.detailSource.text = it.Writer
                mDatabind.detailReadCount.text = "已阅读：${it.LookNum}"
                mDatabind.detailPublishTime.text = it.AddTime

                mDatabind.detailContentTv.text = it.ContentStr

            }, {
                loadsir.showError()
            })

        }
    }


}