package com.info.yikao.ui.activity

import android.content.Intent
import android.os.Bundle
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivitySchoolDetailBinding
import com.info.yikao.databinding.ActivitySignPayBinding
import com.info.yikao.databinding.ActivitySignUpDetailBinding
import com.info.yikao.databinding.ActivityTopNewsDetailBinding
import com.info.yikao.viewmodel.FastNewsDetailViewModel
import com.info.yikao.viewmodel.SignUpDetailViewModel
import com.info.yikao.viewmodel.SignUpPayViewModel

/**
 * 公告快讯的详情页
 */
class FastNewsDetailActivity :
    BaseActivity<FastNewsDetailViewModel, ActivityTopNewsDetailBinding>() {

    override fun layoutId(): Int = R.layout.activity_top_news_detail

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.titleBackBtn.setOnClickListener {
            finish()
        }

        mDatabind.titleTv.text = "公告详情"

        mDatabind.newsDetailTitle.text = "【统考快讯】2023年各省统考招生简章信息汇总"
        mDatabind.detailSource.text = "艺快报"
        mDatabind.detailReadCount.text = "已阅读：86512"
        mDatabind.detailPublishTime.text = "2023-04-01 10:30"

        mDatabind.detailContentTv.text =
            "考生登录 \" 阳光高考特殊类型招生报名平台 \" 进行报名。报名时间：即日起至 2023 年 5 月 10 日。\n" +
                    "\n" +
                    "报名时，考生根据自己的首选科目填报历史类或物理类。物理类考生还需要根据自己的选考科目和专业兴趣填报 1 个至 3 个院校专业组及其专业，报考院校专业组 4 时须填满 6 个不同专业（类）。报名结束后志愿不得修改，作为高考录取及安排专业依据。未按要求填写或填写不规范的视作申请无效。\n" +
                    "\n" +
                    "东南大学对完成报名的考生材料进行初审，择优确定初审合格名单。5 月 25 日起，考生通过报名系统和 \" 东南大学本科招生网 \" 查询初审结果。\n" +
                    "\n" +
                    "测试采用笔试的形式，测试时间为 6 月 11 日。首选科目为物理的考生测试科目为数学和物理；首选科目为历史的考生测试科目为语文和数学。\n" +
                    "\n" +
                    "6 月 20 日前，东南大学根据考生的学校测试成绩和院校专业组志愿，分别以不超过各院校专业组招生计划数的 5 倍（同分均入选）按照 \" 分数优先 \" 的原则确定各院校专业组的入选考生名单，并按照要求进行公示。\n" +
                    "\n" +
                    "考生高考成绩须达到 2023 年江苏省划定的相应特殊类型招生控制线。学校按照 \" 高考成绩（折算成 100 分）×85%+ 学校测试成绩（折算成 100 分）×15%\" 计算达线考生综合评价成绩。\n" +
                    "\n" +
                    "依据综合评价成绩对各院校专业组考生分别进行排序，综合评价成绩相同时，依次以高考成绩、投档同分位次进行排序。江苏省教育考试院将在强基计划之后，本科提前批次之前，根据 A 类各高校提供的入选考生排名和考生确认的高校志愿及顺序，按照招生计划进行投档。\n" +
                    "\n" +
                    "东大综合评价在 \" 强基计划 \" 之后，提前批和专项计划之前完成投档录取，投档录取的考生不再参加其他任何批次、招生类型的录取。"

    }


}