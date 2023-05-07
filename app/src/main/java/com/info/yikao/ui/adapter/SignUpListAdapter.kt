package com.info.yikao.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.info.yikao.R
import com.info.yikao.ext.setAdapterAnimation
import com.info.yikao.model.OrderBean
import me.hgj.jetpackmvvm.ext.view.visible

class SignUpListAdapter(data: MutableList<OrderBean>?) :
    BaseDelegateMultiAdapter<OrderBean, BaseViewHolder>(data) {

    init {
        //todo 增加设置动画模式的标志
        setAdapterAnimation(0)
        //第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<OrderBean>() {
            override fun getItemType(data: List<OrderBean>, position: Int): Int {
                return 1
            }
        })
        //第二步 绑定对应的item类型
        getMultiTypeDelegate()?.let {
            it.addItemType(1, R.layout.item_exam_list)
        }
    }

    override fun convert(helper: BaseViewHolder, item: OrderBean) {
        val examName = helper.getView<TextView>(R.id.order_info)
        val signUpState = helper.getView<TextView>(R.id.pay_state)
        val schoolIcon = helper.getView<ImageView>(R.id.school_icon)
        val schoolName = helper.getView<TextView>(R.id.school_name)
        val examTime = helper.getView<TextView>(R.id.order_time)
        val timerGroup = helper.getView<Group>(R.id.bottom_timer_group)
        val timerTv = helper.getView<TextView>(R.id.bottom_time_head)
        val enterBtn = helper.getView<TextView>(R.id.bottom_btn)

        examName.text = "中国舞-四级考试"

        when (helper.adapterPosition) {
            0 -> {
                signUpState.setBackgroundResource(R.drawable.sign_state_green)
                signUpState.text = "考试中"
            }
            1 -> {
                signUpState.setBackgroundResource(R.drawable.sign_state_red)
                signUpState.text = "报名中"
            }
            2 -> {
                signUpState.setBackgroundResource(R.drawable.sign_state_grey)
                signUpState.text = "已结束"
            }
        }

        schoolName.text = "四川音乐学院"
        examTime.text = "考试时间：2023-04-23 08:30"

        if (helper.adapterPosition == 0) {
            timerGroup.visibility = View.VISIBLE
        } else {
            timerGroup.visibility = View.GONE
        }


    }


}