package com.info.yikao.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityExamIntorBinding
import com.info.yikao.ext.*
import com.info.yikao.model.MajorIntroWrapper
import com.info.yikao.ui.adapter.MajorIntroAdapter
import com.info.yikao.util.CacheUtil
import com.info.yikao.viewmodel.MajorIntroViewModel
import com.info.yikao.weight.EmptyLoadMore
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.util.loge

/**
 * 艺考头条
 */
class MajorIntroActivity : BaseActivity<MajorIntroViewModel, ActivityExamIntorBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //recyclerview的底部加载view
    private lateinit var footView: EmptyLoadMore

    private val mRecycler by lazy { mDatabind.recyclerView }
    private val mRefresh by lazy { mDatabind.swipeRefresh }

    private val mAdapter by lazy { MajorIntroAdapter(arrayListOf()) }

    private var underType = 0
    private var majorId = -1


    override fun layoutId(): Int = R.layout.activity_exam_intor

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.titleTv.text = "报考介绍"
        majorId = intent.getIntExtra("id", -1)


        mDatabind.titleBackBtn.setOnClickListener {
            finish()
        }

        //状态页配置
        loadsir = loadServiceInit(mRefresh) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getListData(true, majorId)
        }


        //初始化recycleview
        mRecycler.init(LinearLayoutManager(this@MajorIntroActivity), mAdapter).let {
            //添加加载更多的底部布局
            footView = EmptyLoadMore(this)
            it.setLoadMoreView(footView)

        }

        //初始化swiperefreshlayout
        mRefresh.init {
            mViewModel.getListData(true, majorId)
        }

        mAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                "click news data jump $position".loge()
                //点击了对象
                var posData = adapter.data[position] as MajorIntroWrapper
            }
        }

        mDatabind.nextBtn.setOnClickListener {
            //判断登录
            val user = appViewModel.userInfo.value
            if (user != null && user.Token.canShow()) {
                //有登录信息
                //点击了付款按钮
                val intent = Intent(this@MajorIntroActivity, SignUpPayActivity::class.java)
                intent.putExtra("id", majorId)
                intent.putExtra("subject", mViewModel.subject)
                intent.putExtra("time", mViewModel.time)
                intent.putExtra("price", mViewModel.price)
                startActivity(intent)
            } else {
                startActivity(Intent(this@MajorIntroActivity, LoginActivity::class.java))
            }

        }

        loadsir.showLoading()
        mViewModel.getListData(true, majorId)
    }

    override fun createObserver() {
        mViewModel.listData.observe(this) {
            loadListData(it, mAdapter, loadsir, mRecycler, mRefresh)
            mDatabind.nextBtn.text = mViewModel.price
        }
    }
}