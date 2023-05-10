package com.info.yikao.ui.viewholder

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.info.yikao.R
import com.info.yikao.ext.Constant
import com.info.yikao.ext.getGlideRequestOptions
import com.info.yikao.model.NewsBean
import com.info.yikao.ui.activity.FastNewsDetailActivity

class TopNewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val iconImg = itemView.findViewById<ImageView>(R.id.head_icon)
    val titleTv = itemView.findViewById<TextView>(R.id.title)
    val sourceTv = itemView.findViewById<TextView>(R.id.source_tv)
    val readTv = itemView.findViewById<TextView>(R.id.read_tv)

    fun bindData(data: NewsBean, context: Context) {
        titleTv.text = data.Title
        sourceTv.text = data.Writer
        readTv.text = "阅读数：${data.LookNum}"

        val schoolIcon = Constant.imgUrlHead+data.IconUrl
        Glide.with(context).load(schoolIcon)
            .apply(getGlideRequestOptions(Constant.GLIDE_OPTIONS_NEWS))
            .into(iconImg)
    }
}