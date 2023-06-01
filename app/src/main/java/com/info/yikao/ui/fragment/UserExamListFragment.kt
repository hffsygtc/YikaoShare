package com.info.yikao.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentUserExamsBinding
import com.info.yikao.ext.init
import com.info.yikao.ext.loadListData
import com.info.yikao.ext.loadServiceInit
import com.info.yikao.ext.showLoading
import com.info.yikao.model.ExamBean
import com.info.yikao.model.OrderBean
import com.info.yikao.ui.activity.ExamResultActivity
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

    //-1 全部 1 通过 0 等待结果 2未通过
    private var underSelect = -1

    override fun layoutId(): Int = R.layout.fragment_user_exams

    private val mRecycler by lazy { mDatabind.recyclerView }
    private val mRefresh by lazy { mDatabind.swipeRefresh }

    override fun initView(savedInstanceState: Bundle?) {
        //初始化bundle数据
        arguments?.let {
            online = it.getBoolean("online")
        }


        //状态页配置
        loadsir = loadServiceInit(mRefresh) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getListData(true, underSelect, online)
        }

        mRefresh.init {
            loadsir.showLoading()
            mViewModel.getListData(true, underSelect, online)
        }

        mDatabind.recyclerView.init(LinearLayoutManager(context), mAdapter)


        mAdapter.run {

            setOnItemClickListener { adapter, view, position ->
                //点击了具体的考试，跳转到考试详情，就是可以查看评分的那个页面
                val examBean = adapter.data[position] as ExamBean

                val intent = Intent(requireActivity(),ExamResultActivity::class.java)
                intent.putExtra("online",online)
                intent.putExtra("id",examBean.OrderNum)
                intent.putExtra("name",examBean.SubjectsName)
                startActivity(intent)
            }

            addChildClickViewIds(
                R.id.show_cert_btn,
            )
            setOnItemChildClickListener { adapter, view, position ->
                when(view.id){
                    R.id.show_cert_btn->{
                        //跳转领取证书
                        //todo 添加跳转证书页面
                    }
                }


            }
        }

        mDatabind.typeAll.setOnClickListener {
            if (underSelect != -1){
                resetTabBg()
                underSelect = -1
                mDatabind.typeAll.setBackgroundResource(R.drawable.white_corner_10)
                loadsir.showLoading()
                mViewModel.getListData(true, underSelect, online)
            }
        }

        mDatabind.typeUnder.setOnClickListener {
            if (underSelect != 1){
                resetTabBg()
                underSelect = 1
                mDatabind.typeUnder.setBackgroundResource(R.drawable.white_corner_10)
                loadsir.showLoading()
                mViewModel.getListData(true, underSelect, online)
            }
        }

        mDatabind.typeWait.setOnClickListener {
            if (underSelect != 0){
                resetTabBg()
                underSelect = 0
                mDatabind.typeWait.setBackgroundResource(R.drawable.white_corner_10)
                loadsir.showLoading()
                mViewModel.getListData(true, underSelect, online)
            }
        }

        mDatabind.typeFinish.setOnClickListener {
            if (underSelect != 2){
                resetTabBg()
                underSelect = 2
                mDatabind.typeFinish.setBackgroundResource(R.drawable.white_corner_10)
                loadsir.showLoading()
                mViewModel.getListData(true, underSelect, online)
            }
        }

        loadsir.showLoading()
        mViewModel.getListData(true, underSelect, online)
    }

    private fun resetTabBg(){
        when(underSelect){
            -1->{
                mDatabind.typeAll.setBackgroundResource(R.drawable.f5_corner_10)
            }
            1->{
                mDatabind.typeUnder.setBackgroundResource(R.drawable.f5_corner_10)
            }
            0->{
                mDatabind.typeWait.setBackgroundResource(R.drawable.f5_corner_10)
            }
            2->{
                mDatabind.typeFinish.setBackgroundResource(R.drawable.f5_corner_10)
            }
        }
    }

    override fun createObserver() {
        mViewModel.listData.observe(this) {
            loadListData(it, mAdapter, loadsir, mRecycler, mRefresh)
        }
    }
}