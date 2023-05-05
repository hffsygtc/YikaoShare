package com.info.yikao.ui.activity

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

//            addChildClickViewIds(
//            )
//            setOnItemChildClickListener { adapter, view, position ->}
        }

        mDatabind.schoolLeftFunc.setOnClickListener {
            if (underSection != 0){
                mDatabind.schoolLeftFunc.apply {
                    setTextColor(Color.parseColor("#333333"))
                    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    textSize = 18f.fpx()
                }
                mDatabind.schoolRightFunc.apply {
                    setTextColor(Color.parseColor("#999999"))
                    typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                    textSize = 18f.fpx()
                }

                underSection = 0
            }
        }

        mDatabind.schoolRightFunc.setOnClickListener {
            if (underSection != 1){
                mDatabind.schoolLeftFunc.setTextColor(Color.parseColor("#333333"))
                mDatabind.schoolRightFunc.setTextColor(Color.parseColor("#999999"))
                underSection = 1
            }
        }

//        loadsir.showLoading()
//        mViewModel.getListData(true)
        loadsir.showSuccess()

        mDatabind.leftMajorName.text = "专业名字"

        mAdapter.setList(mViewModel.testMajorList)
    }

    override fun createObserver() {

    }
}