package com.info.yikao.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityExamListBinding
import com.info.yikao.databinding.ActivityLoginBinding
import com.info.yikao.databinding.ActivitySingleListBinding
import com.info.yikao.databinding.ActivityVertifyInfoBinding
import com.info.yikao.ext.*
import com.info.yikao.model.OrderBean
import com.info.yikao.ui.adapter.NewsFastAdapter
import com.info.yikao.ui.adapter.SignUpListAdapter
import com.info.yikao.ui.adapter.UserOrderListAdapter
import com.info.yikao.viewmodel.FastNewsListViewModel
import com.info.yikao.viewmodel.SignUpListViewModel
import com.info.yikao.viewmodel.UserOrderViewModel
import com.info.yikao.weight.DefineLoadMoreView
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.logw

/**
 * 艺考头条
 */
class FastNewsListActivity : BaseActivity<FastNewsListViewModel, ActivitySingleListBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //recyclerview的底部加载view
    private lateinit var footView: DefineLoadMoreView

    private val mRecycler by lazy { mDatabind.recyclerView }
    private val mRefresh by lazy { mDatabind.swipeRefresh }

    private val mAdapter by lazy { NewsFastAdapter(arrayListOf()) }

    private var underType = 0

    override fun layoutId(): Int = R.layout.activity_single_list

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.titleTv.text = "艺考头条"

        //状态页配置
        loadsir = loadServiceInit(mRefresh) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getListData(true)
        }


        //初始化recycleview
        mRecycler.init(LinearLayoutManager(this@FastNewsListActivity), mAdapter).let {
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
                        startActivity(Intent(this@FastNewsListActivity,SignUpDetailActivity::class.java))
                    }
                    1->{
                        startActivity(Intent(this@FastNewsListActivity,SignUpPayActivity::class.java))
                    }
                    0->{}
                }

            }

//            addChildClickViewIds(
//            )
//            setOnItemChildClickListener { adapter, view, position ->}
        }

        loadsir.showLoading()
        mViewModel.getListData(true)
    }

    override fun createObserver() {
        mViewModel.listData.observe(this) {
            loadListData(it, mAdapter, loadsir, mRecycler, mRefresh)
        }
    }
}