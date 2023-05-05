package com.info.yikao.ui.viewholder

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.info.yikao.R
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
        itemView.setOnClickListener {
            //跳转到学校的详情
            val intent = Intent(context, SchoolDetailActivity::class.java)
            intent.putExtra("id","")
            context.startActivity(intent)
        }
    }
}