package com.info.yikao.ui.viewholder

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.info.yikao.R
import com.info.yikao.ext.Constant
import com.info.yikao.ext.getGlideRequestOptions
import com.info.yikao.model.NewsBean

class TopNewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val iconImg = itemView.findViewById<ImageView>(R.id.head_icon)
    val titleTv = itemView.findViewById<TextView>(R.id.title)
    val sourceTv = itemView.findViewById<TextView>(R.id.source_tv)
    val readTv = itemView.findViewById<TextView>(R.id.read_tv)

    fun bindData(data: NewsBean, context: Context) {
        Glide.with(context).load(data.img)
            .apply(getGlideRequestOptions(Constant.GLIDE_TYPE_DEFAULT))
            .into(iconImg)
        titleTv.text = data.title
        sourceTv.text = data.ori
        readTv.text = "阅读数：${data.read_count}"

        itemView.setOnClickListener {
            //todo jump to detail

        }

    }
}