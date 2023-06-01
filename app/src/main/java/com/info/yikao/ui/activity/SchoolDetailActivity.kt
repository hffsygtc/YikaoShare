package com.info.yikao.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivitySchoolDetailBinding
import com.info.yikao.ext.*
import com.info.yikao.ui.adapter.SchoolMajorExpandedAdapter
import com.info.yikao.viewmodel.SchoolDetailViewModel
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.logw

class SchoolDetailActivity : BaseActivity<SchoolDetailViewModel, ActivitySchoolDetailBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private val mAdapter by lazy { SchoolMajorExpandedAdapter(arrayListOf()) }

    private var underSection = 0

    private var schoolId = -1

    override fun layoutId(): Int = R.layout.activity_school_detail

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.titleTv.text = "院校详情"

        schoolId = intent.getIntExtra("id", -1)

        //状态页配置
        loadsir = loadServiceInit(mDatabind.schoolDetailBg) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getSchoolDetail(schoolId)
            mViewModel.getMajorList(schoolId)
        }


        //初始化recycleview
        mDatabind.majorContentRv.init(LinearLayoutManager(this@SchoolDetailActivity), mAdapter)
            .let {

            }

        mAdapter.run {
            clickMajor = { major ->
                "click major $major".logw()
                val intent = Intent(this@SchoolDetailActivity, MajorIntroActivity::class.java)
                intent.putExtra("id", major.SubjectsId)
                startActivity(intent)
            }
        }

        mDatabind.schoolLeftFunc.setOnClickListener {
            if (underSection != 0) {
                mDatabind.schoolLeftFunc.apply {
                    setTextColor(Color.parseColor("#333333"))
                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, 18f.fpx())
                }
                mDatabind.schoolRightFunc.apply {
                    setTextColor(Color.parseColor("#999999"))
                    typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, 15f.fpx())
                }

                mDatabind.leftMajorLayout.visibility = View.VISIBLE
                mDatabind.rightMajorLayout.visibility = View.GONE
                underSection = 0
            }
        }

        mDatabind.schoolRightFunc.setOnClickListener {
            if (underSection != 1) {
                mDatabind.schoolRightFunc.apply {
                    setTextColor(Color.parseColor("#333333"))
                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, 18f.fpx())
                }
                mDatabind.schoolLeftFunc.apply {
                    setTextColor(Color.parseColor("#999999"))
                    typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, 15f.fpx())
                }

                mDatabind.leftMajorLayout.visibility = View.GONE
                mDatabind.rightMajorLayout.visibility = View.VISIBLE
                underSection = 1
            }
        }

        loadsir.showLoading()
        mViewModel.getSchoolDetail(schoolId)
        mViewModel.getMajorList(schoolId)


    }

    override fun createObserver() {
        mViewModel.schoolDetail.observe(this) { result ->
            parseState(result, {
                loadsir.showSuccess()

                mDatabind.leftMajorName.text = it.ApplyNoName

                val schoolIcon = Constant.imgUrlHead + it.Logo
                Glide.with(this).load(schoolIcon)
                    .apply(getGlideRequestOptions(Constant.GLIDE_OPTIONS_SCHOOL_ICON))
                    .into(mDatabind.schoolIcon)

                mDatabind.schoolName.text = it.SchoolName

                mDatabind.schoolTagFlow.removeAllViews()
                if (it.Tags.canShow()) {
                    val tags = it.Tags.split("|")
                    for (tag in tags) {
                        mDatabind.schoolTagFlow.addView(getTagTv(this, tag))
                    }
                }
                mDatabind.schoolDesc.text = it.Intro

                when (it.ApplyStatus) {
                    0 -> {
                        mDatabind.leftMajorStatus.text = "未开始"
                    }
                    0 -> {
                        mDatabind.leftMajorStatus.text = "报名中"
                    }
                    0 -> {
                        mDatabind.leftMajorStatus.text = "已结束"
                    }
                }

                mDatabind.phoneTv.text = it.Tel
                mDatabind.webTv.text = it.Site
                mDatabind.locationTv.text = it.Address
                mDatabind.contentTv.text = it.Intro

            }, {
                loadsir.showError()
            })

        }

        mViewModel.schoolMajors.observe(this) { result ->
            if(result!= null){
                parseState(result, {
                    mAdapter.setList(it)
                }, {

                })
            }
        }

    }

    private fun getTagTv(context: Context, text: String): TextView {
        val tv = TextView(context)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val textsize: Int = 11f.px()
        val tvRightMargin: Int = 5f.px()
        val tvTopMargin: Int = 12f.px()

        val verPadding: Int = 2f.px()
        val horPadding: Int = 6f.px()

        params.setMargins(0, tvTopMargin, tvRightMargin, 0)

        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textsize.toFloat())
        tv.layoutParams = params
        tv.setPadding(horPadding, verPadding, horPadding, verPadding)

        tv.setTextColor(context.resources.getColor(R.color.color_52618B))
        tv.text = text
        tv.setBackgroundResource(R.drawable.light_blue_corner_10)

        return tv

    }
}