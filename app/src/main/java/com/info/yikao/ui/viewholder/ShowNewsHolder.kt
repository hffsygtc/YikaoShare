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
import com.info.yikao.model.StreetShowBean

class ShowNewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val iconImg = itemView.findViewById<ImageView>(R.id.head_icon)
    val titleTv = itemView.findViewById<TextView>(R.id.title)
    val signUpBtn = itemView.findViewById<TextView>(R.id.sign_up_btn)
    val locationTv = itemView.findViewById<TextView>(R.id.class_content_tv)
    val methodContent = itemView.findViewById<TextView>(R.id.method_content_tv)

    fun bindData(data: StreetShowBean,context:Context){
        val schoolIcon = ""
        Glide.with(context).load(schoolIcon)
            .apply(getGlideRequestOptions(Constant.GLIDE_OPTIONS_SHOW))
            .into(iconImg)

        titleTv.text = "2023届春季“春季畅演”大型展演火 热报名中"
        locationTv.text = "成都市"
        methodContent.text = "线上，线下"


    }
}
