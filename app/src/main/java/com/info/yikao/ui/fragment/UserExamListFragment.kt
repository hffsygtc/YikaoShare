package com.info.yikao.ui.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentUserExamsBinding
import com.info.yikao.ext.init
import com.info.yikao.ext.loadServiceInit
import com.info.yikao.ext.showLoading
import com.info.yikao.ui.adapter.UserExamListAdapter
import com.info.yikao.viewmodel.UserExamViewModel
import com.kingja.loadsir.core.LoadService

class UserExamListFragment : BaseFragment<UserExamViewModel, FragmentUserExamsBinding>() {

    companion object {
        fun newInstance(online: Boolean): UserExamListFragment {
            val args = Bundle()
            args.putBoolean("online", online)
            val fragment = UserExamListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var online = false

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private val mAdapter: UserExamListAdapter by lazy {
        UserExamListAdapter(arrayListOf())
    }

    override fun layoutId(): Int = R.layout.fragment_user_exams

    private val mRefresh by lazy { mDatabind.swipeRefresh }

    override fun initView(savedInstanceState: Bundle?) {
        //初始化bundle数据
        arguments?.let {
            online = it.getBoolean("from_user")
        }


        //状态页配置
        loadsir = loadServiceInit(mRefresh) {
            //点击重试时触发操作
            loadsir.showLoading()
        }

        mDatabind.recyclerView.init(LinearLayoutManager(context), mAdapter)


        mAdapter.run {

        }

        loadsir.showLoading()


    }

    override fun createObserver() {

    }
}