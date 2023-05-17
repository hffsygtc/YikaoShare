package com.info.yikao.ui.adapter

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.info.yikao.R
import com.info.yikao.ext.Constant
import com.info.yikao.ext.getGlideRequestOptions
import com.info.yikao.ext.setAdapterAnimation
import com.info.yikao.model.TeacherResultBean

class ExamTeacherResultAdapter(data: MutableList<TeacherResultBean>?) :
    BaseDelegateMultiAdapter<TeacherResultBean, BaseViewHolder>(data) {

    init {
        //todo 增加设置动画模式的标志
        setAdapterAnimation(0)
        //第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<TeacherResultBean>() {
            override fun getItemType(data: List<TeacherResultBean>, position: Int): Int {
                return data[position].type
            }
        })
        //第二步 绑定对应的item类型
        getMultiTypeDelegate()?.let {
            it.addItemType(1, R.layout.item_exam_result_head)
            it.addItemType(0, R.layout.item_exam_result_teacher)
            it.addItemType(3, R.layout.item_exam_result_empty)
        }
    }

    override fun convert(helper: BaseViewHolder, item: TeacherResultBean) {
        when (helper.itemViewType) {
            1 -> {
                //显示头部
                val leftResult = helper.getView<TextView>(R.id.left_result)
                val rightResult = helper.getView<TextView>(R.id.right_result)

                leftResult.text = item.JuryResultStr

                when (item.JuryResult) {
                    1 -> {
                        //通过
                        rightResult.text = "通过"
                        rightResult.setTextColor(Color.parseColor("#7CA861"))
                    }
                    2 -> {
                        //未通过
                        rightResult.text = "未通过"
                        rightResult.setTextColor(Color.parseColor("#FF3434"))
                    }
                    else -> {
                        //等待结果
                        rightResult.text = "等待结果"
                        rightResult.setTextColor(Color.parseColor("#FF8D31"))
                    }
                }
            }
            0 -> {
                //普通老师结果
                val headIcon = helper.getView<ImageView>(R.id.head_icon)
                val name = helper.getView<TextView>(R.id.name)
                val result = helper.getView<TextView>(R.id.result)
                val comment = helper.getView<TextView>(R.id.comment)

                val userHead = Constant.imgUrlHead + "item.HeadImg"
                Glide.with(context).load(userHead)
                    .apply(getGlideRequestOptions(Constant.GLIDE_TYPE_USER_HEAD))
                    .into(headIcon)
                name.text = "评委老师"
                result.text = item.JuryResultStr
                comment.text = item.Remark
            }
            3 -> {

            }
        }

    }


}