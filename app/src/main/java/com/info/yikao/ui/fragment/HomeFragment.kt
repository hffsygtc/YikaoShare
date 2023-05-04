package com.info.yikao.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentMainHomeBinding
import com.info.yikao.ext.init
import com.info.yikao.ext.px
import com.info.yikao.model.BannerArticle
import com.info.yikao.ui.adapter.MainBannerAdapter
import com.info.yikao.ui.adapter.MainHomeListAdapter
import com.info.yikao.view.BannerItemViewHolder
import com.info.yikao.viewmodel.HomeViewModel
import com.zhpan.bannerview.BannerViewPager

class HomeFragment : BaseFragment<HomeViewModel,FragmentMainHomeBinding>() {

    private val mAdapter : MainHomeListAdapter by lazy {
        MainHomeListAdapter(requireContext(), arrayListOf())
    }

    override fun layoutId(): Int = R.layout.fragment_main_home

    override fun initView(savedInstanceState: Bundle?) {
        initBanner()

        mDatabind.recyclerView.init(LinearLayoutManager(context),mAdapter)

        mAdapter.list = mViewModel.listItems
        mAdapter.notifyDataSetChanged()

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