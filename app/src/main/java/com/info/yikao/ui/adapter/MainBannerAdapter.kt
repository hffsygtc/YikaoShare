package com.info.yikao.ui.adapter

import android.view.View
import com.info.yikao.R
import com.info.yikao.model.BannerArticle
import com.info.yikao.view.BannerItemViewHolder
import com.zhpan.bannerview.BaseBannerAdapter

class MainBannerAdapter : BaseBannerAdapter<BannerArticle, BannerItemViewHolder>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_main_banner
    }

    override fun createViewHolder(itemView: View, viewType: Int): BannerItemViewHolder {
        return BannerItemViewHolder(itemView);
    }

    override fun onBind(
        holder: BannerItemViewHolder?,
        data: BannerArticle?,
        position: Int,
        pageSize: Int
    ) {
        holder?.bindData(data, position, pageSize)
    }

}