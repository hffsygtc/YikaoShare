package com.info.yikao.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivitySingleListBinding
import com.info.yikao.ext.*
import com.info.yikao.model.MessageBean
import com.info.yikao.model.StreetShowBean
import com.info.yikao.ui.adapter.MessageListAdapter
import com.info.yikao.ui.adapter.ShowListAdapter
import com.info.yikao.viewmodel.MessageListViewModel
import com.info.yikao.viewmodel.ShowListViewModel
import com.info.yikao.weight.DefineLoadMoreView
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import me.hgj.jetpackmvvm.ext.util.logw

/**
 * 展演资讯
 */
class MessageListActivity : BaseActivity<MessageListViewModel, ActivitySingleListBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //recyclerview的底部加载view
    private lateinit var footView: DefineLoadMoreView

    private val mRecycler by lazy { mDatabind.recyclerView }
    private val mRefresh by lazy { mDatabind.swipeRefresh }

    private val mAdapter by lazy { MessageListAdapter(arrayListOf()) }

    private var underType = 0

    override fun layoutId(): Int = R.layout.activity_single_list

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.titleBackBtn.setOnClickListener {
            finish()
        }

        mDatabind.titleTv.text = "消息"

        //状态页配置
        loadsir = loadServiceInit(mRefresh) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getListData(true)
        }


        //初始化recycleview
        mRecycler.init(LinearLayoutManager(this@MessageListActivity), mAdapter).let {
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
                var posData = adapter.data[position] as MessageBean

//                val intent = Intent(this@MessageListActivity, ShowDetailActivity::class.java)
//                intent.putExtra("id", posData.ShowInfoId)
//                startActivity(intent)


            }
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