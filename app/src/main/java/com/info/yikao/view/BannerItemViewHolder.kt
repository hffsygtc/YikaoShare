package com.info.yikao.view

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.info.yikao.R
import com.info.yikao.ext.Constant
import com.info.yikao.ext.getGlideRequestOptions
import com.info.yikao.model.BannerArticle
import com.zhpan.bannerview.BaseViewHolder

class BannerItemViewHolder(view: View) : BaseViewHolder<BannerArticle>(view) {

    override fun bindData(data: BannerArticle?, position: Int, pageSize: Int) {
        val imageView = findView<ImageView>(R.id.banner_img)

        var imageSrc = Constant.imgUrlHead + data?.ImgUrl

        Glide.with(imageView.context).load(imageSrc)
            .apply(getGlideRequestOptions(Constant.GLIDE_OPTIONS_BANNER))
            .into(imageView)
    }
}