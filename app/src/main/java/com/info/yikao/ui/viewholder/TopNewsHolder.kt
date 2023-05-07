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
        titleTv.text = data.title
        sourceTv.text = data.ori
        readTv.text = "阅读数：${data.read_count}"

        if (data.type == 1){
            iconImg.setBackgroundResource(R.mipmap.icon_fast_news)
        }else{
            iconImg.setBackgroundResource(R.mipmap.icon_notice)
        }

        itemView.setOnClickListener {
            //todo jump to detail

        }

    }
}