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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivitySchoolDetailBinding
import com.info.yikao.databinding.ActivitySingleListBinding
import com.info.yikao.ext.*
import com.info.yikao.model.OrderBean
import com.info.yikao.ui.adapter.SchoolMajorExpandedAdapter
import com.info.yikao.ui.adapter.UserOrderListAdapter
import com.info.yikao.viewmodel.SchoolDetailViewModel
import com.info.yikao.viewmodel.UserOrderViewModel
import com.info.yikao.weight.DefineLoadMoreView
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import me.hgj.jetpackmvvm.ext.util.logw

class SchoolDetailActivity : BaseActivity<SchoolDetailViewModel, ActivitySchoolDetailBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private val mAdapter by lazy { SchoolMajorExpandedAdapter(arrayListOf()) }

    private var underSection = 0

    override fun layoutId(): Int = R.layout.activity_school_detail

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.titleTv.text = "院校详情"

        //状态页配置
        loadsir = loadServiceInit(mDatabind.schoolDetailBg) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getListData(true)
        }


        //初始化recycleview
        mDatabind.majorContentRv.init(LinearLayoutManager(this@SchoolDetailActivity), mAdapter)
            .let {

            }

        mAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                //点击了对象
//                var posData = adapter.data[position] as OrderBean

            }

            clickMajor = { major ->
                "click major $major".logw()
                val intent = Intent(this@SchoolDetailActivity, MajorIntroActivity::class.java)
                intent.putExtra("id", "")
                startActivity(intent)

            }

//            addChildClickViewIds(
//            )
//            setOnItemChildClickListener { adapter, view, position ->}
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
                    setTextSize(TypedValue.COMPLEX_UNIT_PX,15f.fpx())
                }

                mDatabind.leftMajorLayout.visibility = View.GONE
                mDatabind.rightMajorLayout.visibility = View.VISIBLE
                underSection = 1
            }
        }

//        loadsir.showLoading()
//        mViewModel.getListData(true)
        loadsir.showSuccess()

        mDatabind.leftMajorName.text = "专业名字"

        mAdapter.setList(mViewModel.testMajorList)

        val schoolIcon = ""
        Glide.with(this).load(schoolIcon)
            .apply(getGlideRequestOptions(Constant.GLIDE_OPTIONS_SCHOOL_ICON))
            .into(mDatabind.schoolIcon)

        mDatabind.schoolName.text = "四川音乐学院"

        mDatabind.schoolTagFlow.removeAllViews()
        mDatabind.schoolTagFlow.addView(getTagTv(this, "公办"))
        mDatabind.schoolTagFlow.addView(getTagTv(this, "本科"))
        mDatabind.schoolTagFlow.addView(getTagTv(this, "艺术类"))

        mDatabind.schoolDesc.text =
            "四川音乐学院（Sichuan Conservatory of Music）简称“川音”，位于四川省成都市，是一所以“艺术”为主要办学特色的省属全日制普通本科高等院校，是全国11所独立设置的专业音乐学院之一..."


    }

    override fun createObserver() {

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