package com.info.yikao.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentMainHomeBinding
import com.info.yikao.ext.init
import com.info.yikao.ext.px
import com.info.yikao.model.BannerArticle
import com.info.yikao.ui.activity.*
import com.info.yikao.ui.adapter.MainBannerAdapter
import com.info.yikao.ui.adapter.MainHomeListAdapter
import com.info.yikao.view.BannerItemViewHolder
import com.info.yikao.viewmodel.HomeViewModel
import com.zhpan.bannerview.BannerViewPager
import me.hgj.jetpackmvvm.ext.util.loge

class HomeFragment : BaseFragment<HomeViewModel, FragmentMainHomeBinding>() {

    private val mAdapter: MainHomeListAdapter by lazy {
        MainHomeListAdapter(requireContext(), arrayListOf())
    }

    override fun layoutId(): Int = R.layout.fragment_main_home

    override fun initView(savedInstanceState: Bundle?) {
        initBanner()

        mDatabind.recyclerView.init(LinearLayoutManager(context), mAdapter)

        mAdapter.list = mViewModel.listItems
        mAdapter.notifyDataSetChanged()

        mAdapter.run {
            enterMoreClick = { type ->
                "click type is $type".loge()
                when (type) {
                    "艺考头条" -> {
                        //艺考头条列表
                        startActivity(
                            Intent(
                                requireActivity(),
                                FastNewsListActivity::class.java
                            )
                        )
                    }
                    "艺考报名"->{
                        //艺考报名列表
                        startActivity(
                            Intent(
                                requireActivity(),
                                SchoolListActivity::class.java
                            )
                        )
                    }
                    "展演资讯"->{
                        //艺考报名列表
                        startActivity(
                            Intent(
                                requireActivity(),
                                ShowListActivity::class.java
                            )
                        )
                    }
                }
            }

            itemClickListener = {wrapper->
                when(wrapper.type){
                    1->{
                        //新闻快讯类
                        startActivity(Intent(requireActivity(), FastNewsDetailActivity::class.java))
                    }
                    2->{
                        //艺考报名
                        //跳转到学校的详情
                        val intent = Intent(requireActivity(), SchoolDetailActivity::class.java)
                        intent.putExtra("id","")
                        startActivity(intent)
                    }
                    3->{
                        //艺考报名
                        //跳转到学校的详情
                        val intent = Intent(requireActivity(), ShowDetailActivity::class.java)
                        intent.putExtra("id","")
                        startActivity(intent)
                    }
                }

            }
        }

    }


    /**
     * 轮播部分
     */
    private fun initBanner() {
        //图标的指示器
        val indicator = mDatabind.bannerIndicator.apply {
            setIndicatorGap(resources.getDimensionPixelOffset(R.dimen.globe_dimen_8))
            setIndicatorDrawable(
                R.mipmap.icon_banner_unchecked,
                R.mipmap.icon_banner_checked
            )
            setIndicatorSize(
                4f.px(),
                4f.px(),
                10f.px(),
                4f.px()
            )
        }

        //轮播图的配置
        val bannerView: BannerViewPager<BannerArticle, BannerItemViewHolder> =
            mDatabind.bannerView as BannerViewPager<BannerArticle, BannerItemViewHolder>
        bannerView.apply {
            setIndicatorVisibility(View.GONE)
            setIndicatorView(indicator)
            adapter = MainBannerAdapter()
            setLifecycleRegistry(lifecycle)
            val bannerItems = mViewModel.bannerItems
            setOnPageClickListener { index ->
                val clickItem = bannerItems.getOrNull(index)
            }
            create(bannerItems)
            adapter.notifyDataSetChanged()
        }
    }
}