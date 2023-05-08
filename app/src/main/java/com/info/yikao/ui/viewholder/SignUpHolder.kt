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
import com.info.yikao.model.SchoolBean
import com.info.yikao.ui.activity.SchoolDetailActivity

class SignUpHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val iconImg = itemView.findViewById<ImageView>(R.id.head_icon)
    val schoolName = itemView.findViewById<TextView>(R.id.school_name)
    val titleTv = itemView.findViewById<TextView>(R.id.title)
    val signUpBtn = itemView.findViewById<TextView>(R.id.sign_up_btn)
    val classContent = itemView.findViewById<TextView>(R.id.class_content_tv)
    val methodContent = itemView.findViewById<TextView>(R.id.method_content_tv)

    fun bindData(data: SchoolBean, context: Context) {
        val schoolIcon = ""
        Glide.with(context).load(schoolIcon)
            .apply(getGlideRequestOptions(Constant.GLIDE_OPTIONS_SCHOOL_ICON))
            .into(iconImg)

        schoolName.text = "四川音乐学院"
        titleTv.text = "2023届夏季川音考级报名"
        classContent.text = "中国舞，爵士舞，钢琴，吉他，声乐，二胡等..."
        methodContent.text = "线上，线下"


    }
}