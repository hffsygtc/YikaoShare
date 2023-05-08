package com.info.yikao.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.*
import com.info.yikao.ext.Constant
import com.info.yikao.ext.getGlideRequestOptions
import com.info.yikao.viewmodel.FastNewsDetailViewModel
import com.info.yikao.viewmodel.ShowDetailViewModel
import com.info.yikao.viewmodel.SignUpDetailViewModel
import com.info.yikao.viewmodel.SignUpPayViewModel
import me.hgj.jetpackmvvm.ext.util.logw

/**
 * 展演详情的页面
 */
class ShowDetailActivity : BaseActivity<ShowDetailViewModel, ActivityStreetShowDetailBinding>() {

    override fun layoutId(): Int = R.layout.activity_street_show_detail

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.titleBackBtn.setOnClickListener {
            finish()
        }

        mDatabind.titleTv.text = "展演详情"

        val image = "http://image.nbd.com.cn/uploads/articles/thumbnails/1393907/78456848.thumb_hl.jpg"
        val imageView = mDatabind.showTopImg

        image?.let {
            Glide.with(this).asBitmap().load(it)
                .apply(getGlideRequestOptions(Constant.GLIDE_TYPE_DEFAULT,8f))
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        val height = resource.height
                        val width = resource.width

                        val imageLayoutParams: ConstraintLayout.LayoutParams =
                            imageView.layoutParams as ConstraintLayout.LayoutParams

                        if (height > 0 && width > 0) {
                            imageLayoutParams.dimensionRatio = "$width:$height"
                        } else {
                            imageLayoutParams.dimensionRatio = "2:1"
                        }
                        "large image width $width  height  $height".logw()
                        imageView.setImageBitmap(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        imageView.setImageDrawable(placeholder)

                    }
                })

        }

        mDatabind.newsDetailTitle.text = "2023届春季“春季畅演”大型展演火热报名中"
        mDatabind.locationTv.text = "举办地址：成都芝麻开花剧场"
        mDatabind.methodTv.text = "报名方式：线上报名"

        mDatabind.detailContentTv.text = "为普及美育素质教育、提高新一代青少年的艺术文化品位、提升青\n" +
                "少年音乐素养,展示青少年精神风貌,储备一批青少年艺术人才资源,\n" +
                "搭建美育人才网络,根据《关于全面加强和改进新时代学校美育工\n" +
                "作的意见》,特向全国推出“‘这,就是我们的旋律’音乐之旅”展演活\n" +
                "动。\n" +
                "　“‘这,就是我们的旋律’音乐之旅”展演活动由文化和旅游部人才中\n" +
                "心主办,专业学术机构参与,权威评委出席,正规运营团队执行的展演\n" +
                "活动。该系列展演活动详情如下:"

    }


}