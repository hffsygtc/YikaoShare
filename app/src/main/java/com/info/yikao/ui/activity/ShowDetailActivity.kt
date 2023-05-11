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
import com.info.yikao.ext.*
import com.info.yikao.viewmodel.FastNewsDetailViewModel
import com.info.yikao.viewmodel.ShowDetailViewModel
import com.info.yikao.viewmodel.SignUpDetailViewModel
import com.info.yikao.viewmodel.SignUpPayViewModel
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.logw

/**
 * 展演详情的页面
 */
class ShowDetailActivity : BaseActivity<ShowDetailViewModel, ActivityStreetShowDetailBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private var showId = -1

    override fun layoutId(): Int = R.layout.activity_street_show_detail

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.titleBackBtn.setOnClickListener {
            finish()
        }

        mDatabind.titleTv.text = "展演详情"

        showId = intent.getIntExtra("id", -1)

        //状态页配置
        loadsir = loadServiceInit(mDatabind.mainLayout) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getShowDetail(showId)
        }
        loadsir.showLoading()
        mViewModel.getShowDetail(showId)
    }

    override fun createObserver() {
        mViewModel.showDetail.observe(this) { result ->
            parseState(result, {
                loadsir.showSuccess()

                val image = Constant.imgUrlHead + it.ImgUrl
                val imageView = mDatabind.showTopImg

                image?.let {
                    Glide.with(this).asBitmap().load(it)
                        .apply(getGlideRequestOptions(Constant.GLIDE_TYPE_DEFAULT, 8f))
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
                                imageView.setImageBitmap(resource)
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {
                                imageView.setImageDrawable(placeholder)

                            }
                        })
                }

                mDatabind.newsDetailTitle.text = it.ShowName
                mDatabind.locationTv.text = "举办地址：${it.Stadium}"
                mDatabind.methodTv.text = "报名方式：${it.ApplyType}报名"

                mDatabind.detailContentTv.text = it.ShowStr

            }, {
                loadsir.showError()
            })
        }
    }


}