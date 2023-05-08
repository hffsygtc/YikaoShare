package com.info.yikao.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivitySchoolListBinding
import com.info.yikao.ext.*
import com.info.yikao.model.NewsBean
import com.info.yikao.model.SchoolBean
import com.info.yikao.ui.adapter.SchoolListAdapter
import com.info.yikao.viewmodel.SchoolListQrViewModel
import com.info.yikao.weight.DefineLoadMoreView
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import me.hgj.jetpackmvvm.ext.util.loge
import me.hgj.jetpackmvvm.ext.util.logw
import me.hgj.jetpackmvvm.util.get


/**
 * 艺考头条
 */
class SchoolListActivity : BaseActivity<SchoolListQrViewModel, ActivitySchoolListBinding>() {

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res: ActivityResult? ->
            "result is $res".logw()
            val resultIntent = res?.data
            val resultStr = resultIntent?.get("result", "")
            "result str is $resultStr".logw()
        }

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //recyclerview的底部加载view
    private lateinit var footView: DefineLoadMoreView

    private val mRecycler by lazy { mDatabind.recyclerView }
    private val mRefresh by lazy { mDatabind.swipeRefresh }

    private val mAdapter by lazy { SchoolListAdapter(arrayListOf()) }

    private var underType = 0

    override fun layoutId(): Int = R.layout.activity_school_list

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadsir = loadServiceInit(mRefresh) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getListData(true)
        }


        //初始化recycleview
        mRecycler.init(LinearLayoutManager(this@SchoolListActivity), mAdapter).let {
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

                "click news data jump $position".loge()
                //点击了对象
                var posData = adapter.data[position] as SchoolBean

                //todo
                //跳转到学校详情
                val intent = Intent(this@SchoolListActivity, SchoolDetailActivity::class.java)
                intent.putExtra("id", "")
                startActivity(intent)
            }

//            addChildClickViewIds(
//            )
//            setOnItemChildClickListener { adapter, view, position ->}
        }

        loadsir.showLoading()
        mViewModel.getListData(true)

        mDatabind.iconTopNotice.setOnClickListener {
            //点击了扫描按钮
            val intent = Intent(this, CustomCaptureActivity::class.java)
            launcher.launch(intent)
        }
    }

    override fun createObserver() {
        mViewModel.listData.observe(this) {
            loadListData(it, mAdapter, loadsir, mRecycler, mRefresh)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                //展示到页面上
            }
        }
    }
}