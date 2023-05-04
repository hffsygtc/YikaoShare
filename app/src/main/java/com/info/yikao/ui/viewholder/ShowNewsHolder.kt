package com.info.yikao.ui.viewholder

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.info.yikao.R
import com.info.yikao.model.StreetShowBean

class ShowNewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val iconImg = itemView.findViewById<ImageView>(R.id.head_icon)
    val titleTv = itemView.findViewById<TextView>(R.id.title)
    val signUpBtn = itemView.findViewById<TextView>(R.id.sign_up_btn)
    val locationTv = itemView.findViewById<TextView>(R.id.class_content_tv)
    val methodContent = itemView.findViewById<TextView>(R.id.method_content_tv)

    fun bindData(data: StreetShowBean,context:Context){


    }
}
