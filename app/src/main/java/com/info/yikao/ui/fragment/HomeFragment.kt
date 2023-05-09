package com.info.yikao.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentMainHomeBinding
import com.info.yikao.ext.init
import com.info.yikao.ext.loadServiceInit
import com.info.yikao.ext.px
import com.info.yikao.ext.showLoading
import com.info.yikao.model.BannerArticle
import com.info.yikao.ui.activity.*
import com.info.yikao.ui.adapter.MainBannerAdapter
import com.info.yikao.ui.adapter.MainHomeListAdapter
import com.info.yikao.view.BannerItemViewHolder
import com.info.yikao.viewmodel.HomeViewModel
import com.kingja.loadsir.core.LoadService
import com.zhpan.bannerview.BannerViewPager
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.loge

class HomeFragment : BaseFragment<HomeViewModel, FragmentMainHomeBinding>() {


    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private val mAdapter: MainHomeListAdapter by lazy {
        MainHomeListAdapter(requireContext(), arrayListOf())
    }

    override fun layoutId(): Int = R.layout.fragment_main_home

    private val mRefresh by lazy { mDatabind.swipeRefresh }

    override fun initView(savedInstanceState: Bundle?) {
//        initBanner()

        //状态页配置
        loadsir = loadServiceInit(mRefresh) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getBannerNews()
        }


        mDatabind.recyclerView.init(LinearLayoutManager(context), mAdapter)

        //初始化swiperefreshlayout
        mRefresh.init {
            mViewModel.getBannerNews()
        }


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
                    "艺考报名" -> {
                        //艺考报名列表
                        startActivity(
                            Intent(
                                requireActivity(),
                                SchoolListActivity::class.java
                            )
                        )
                    }
                    "展演资讯" -> {
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

            itemClickListener = { wrapper ->
                when (wrapper.type) {
                    1 -> {
                        //新闻快讯类
                        startActivity(Intent(requireActivity(), FastNewsDetailActivity::class.java))
                    }
                    2 -> {
                        //艺考报名
                        //跳转到学校的详情
                        val intent = Intent(requireActivity(), SchoolDetailActivity::class.java)
                        intent.putExtra("id", "")
                        startActivity(intent)
                    }
                    3 -> {
                        //艺考报名
                        //跳转到学校的详情
                        val intent = Intent(requireActivity(), ShowDetailActivity::class.java)
                        intent.putExtra("id", "")
                        startActivity(intent)
                    }
                }

            }
        }

        loadsir.showLoading()
        mViewModel.getBannerNews()

        mDatabind.searchSectionView.setOnClickListener {
            val intent = Intent(requireActivity(), RecordVideoActivity::class.java)
            intent.putExtra("id", "")
            startActivity(intent)
        }
    }


    /**
     * 轮播部分
     */
//    private fun initBanner() {
//        //图标的指示器
//        val indicator = mDatabind.bannerIndicator.apply {
//            setIndicatorGap(resources.getDimensionPixelOffset(R.dimen.globe_dimen_8))
//            setIndicatorDrawable(
//                R.mipmap.icon_banner_unchecked,
//                R.mipmap.icon_banner_checked
//            )
//            setIndicatorSize(
//                4f.px(),
//                4f.px(),
//                10f.px(),
//                4f.px()
//            )
//        }
//
//        //轮播图的配置
//        val bannerView: BannerViewPager<BannerArticle, BannerItemViewHolder> =
//            mDatabind.bannerView as BannerViewPager<BannerArticle, BannerItemViewHolder>
//        bannerView.apply {
//            setIndicatorVisibility(View.GONE)
//            setIndicatorView(indicator)
//            setLifecycleRegistry(lifecycle)
//            adapter =  MainBannerAdapter()
//            val bannerItems = arrayListOf<BannerArticle>()
//            setOnPageClickListener { index ->
//                val clickItem = bannerItems.getOrNull(index)
//            }
//            create(bannerItems)
//            adapter.notifyDataSetChanged()
//        }
//    }

    override fun createObserver() {
        mViewModel.bannerData.observe(this) { result ->
            loadsir.showSuccess()
            mRefresh.isRefreshing = false
            parseState(result, {
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
                    setLifecycleRegistry(lifecycle)
                    adapter =  MainBannerAdapter()
                    val bannerItems = it
                    setOnPageClickListener { index ->
                        val clickItem = bannerItems.getOrNull(index)
                        val intent = Intent(requireActivity(),WebviewActivity::class.java)
                        intent.putExtra("url","http://www.baidu.com")
                        startActivity(intent)
                    }
                    create(bannerItems)
                    adapter.notifyDataSetChanged()
                }
            }, {})
        }
    }
}