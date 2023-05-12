package com.info.yikao.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityExamListBinding
import com.info.yikao.ext.*
import com.info.yikao.model.OrderBean
import com.info.yikao.ui.adapter.SignUpListAdapter
import com.info.yikao.viewmodel.SignUpListViewModel
import com.info.yikao.weight.DefineLoadMoreView
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import me.hgj.jetpackmvvm.ext.util.logw

/**
 * 报名的订单列表
 */
class SignUpListActivity : BaseActivity<SignUpListViewModel, ActivityExamListBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //recyclerview的底部加载view
    private lateinit var footView: DefineLoadMoreView

    private val mRecycler by lazy { mDatabind.recyclerView }
    private val mRefresh by lazy { mDatabind.swipeRefresh }

    private val mAdapter by lazy { SignUpListAdapter(arrayListOf()) }

    private var underType = 0

    override fun layoutId(): Int = R.layout.activity_exam_list

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.titleTv.text = "报考订单"

        //状态页配置
        loadsir = loadServiceInit(mRefresh) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getListData(true)
        }


        //初始化recycleview
        mRecycler.init(LinearLayoutManager(this@SignUpListActivity), mAdapter).let {
            //添加加载更多的底部布局
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                //触发加载更多时请求数据
                "触发列表加载更多  .........".logw()
                mViewModel.getListData(false)
            })
        }

        //初始化swiperefreshlayout
        mRefresh.init {
            mViewModel.getListData(true)
        }

        mAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                //点击了对象
                var posData = adapter.data[position] as OrderBean

                //todo
                when(position){
                    0->{
                        startActivity(Intent(this@SignUpListActivity,SignUpDetailActivity::class.java))
                    }
                    1->{
                        startActivity(Intent(this@SignUpListActivity,SignUpPayActivity::class.java))
                    }
                    0->{}
                }

            }

//            addChildClickViewIds(
//            )
//            setOnItemChildClickListener { adapter, view, position ->}
        }

        mDatabind.typeAll.setOnClickListener {
            checkTypeTab(0)
        }
        mDatabind.typeUnder.setOnClickListener {
            checkTypeTab(1)
        }
        mDatabind.typeWait.setOnClickListener {
            checkTypeTab(2)
        }
        mDatabind.typeFinish.setOnClickListener {
            checkTypeTab(3)
        }

        loadsir.showLoading()
        mViewModel.getListData(true)
    }

    private fun checkTypeTab(toPos: Int) {
        if (toPos != underType) {
            //点击切换新的类型
            val fromView = getTab(underType)
            val toView = getTab(toPos)

            fromView.apply {
                setTextColor(Color.parseColor("#999999"))
                typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
            }
            toView.apply {
                setTextColor(Color.parseColor("#333333"))
                typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            }
            underType = toPos
        }
    }

    private fun getTab(pos: Int): TextView {
        return when (pos) {
            0 -> mDatabind.typeAll
            1 -> mDatabind.typeUnder
            2 -> mDatabind.typeWait
            3 -> mDatabind.typeFinish
            else -> mDatabind.typeAll
        }
    }

    override fun createObserver() {
        mViewModel.listData.observe(this) {
            loadListData(it, mAdapter, loadsir, mRecycler, mRefresh)
        }
    }
}