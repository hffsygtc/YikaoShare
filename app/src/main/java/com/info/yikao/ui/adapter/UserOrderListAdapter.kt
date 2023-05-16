package com.info.yikao.ui.adapter

import android.graphics.Color
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.info.yikao.R
import com.info.yikao.ext.Constant
import com.info.yikao.ext.getGlideRequestOptions
import com.info.yikao.ext.setAdapterAnimation
import com.info.yikao.model.OrderBean

class UserOrderListAdapter(data: MutableList<OrderBean>?) :
    BaseDelegateMultiAdapter<OrderBean, BaseViewHolder>(data) {

    private val timerList = arrayListOf<CountDownTimer>()

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
            it.addItemType(1, R.layout.item_user_order_list)
        }
    }

    override fun convert(helper: BaseViewHolder, item: OrderBean) {
        val schoolLogo = helper.getView<ImageView>(R.id.school_icon)
        val schoolName = helper.getView<TextView>(R.id.school_name)
        val payState = helper.getView<TextView>(R.id.pay_state)
        val payStateIcon = helper.getView<ImageView>(R.id.pay_state_icon)
        val subjectName = helper.getView<TextView>(R.id.order_info)
        val examTime = helper.getView<TextView>(R.id.order_time)
        val price = helper.getView<TextView>(R.id.order_money)
        val timerGroup = helper.getView<Group>(R.id.order_bottom_timer_group)
        val timerTv = helper.getView<TextView>(R.id.bottom_time_counter)
        val payBtn = helper.getView<TextView>(R.id.bottom_pay_btn)
        val cancelBtn = helper.getView<TextView>(R.id.bottom_cancel_btn)


        val schoolIcon = Constant.imgUrlHead + item.Logo
        Glide.with(context).load(schoolIcon)
            .apply(getGlideRequestOptions(Constant.GLIDE_OPTIONS_SCHOOL_ICON))
            .into(schoolLogo)
        schoolName.text = item.SchoolName
        when (item.PayStatus) {
            "0" -> {
                //未支付
                payStateIcon.setBackgroundResource(R.mipmap.icon_pay_wait)
                payState.text = "未支付"
                payState.setTextColor(Color.parseColor("#FF8D31"))
            }
            "1" -> {
                //已支付
                payStateIcon.setBackgroundResource(R.mipmap.icon_pay_success)
                payState.text = "已支付"
                payState.setTextColor(Color.parseColor("#7CA861"))

            }
            "2" -> {
                //已关闭
                payStateIcon.setBackgroundResource(R.mipmap.icon_pay_cancel)
                payState.text = "已关闭"
                payState.setTextColor(Color.parseColor("#999999"))
            }
        }

        subjectName.text = item.SubjectsName
        examTime.text = "考试时间：${item.TestStart}"
        price.text = item.Amount

        if (item.PayStatus == "0") {
            timerGroup.visibility = View.VISIBLE
            //未支付，需要显示底部的倒计时
            val codeTimer = object : CountDownTimer((item.ExpiresMins * 60 * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timerTv.text =
                        "${millisUntilFinished / 60000}:${millisUntilFinished % 60000 / 1000}"
//                        mDatabind.timerTv.text = "${millisUntilFinished / 1000}秒后重新获取"
                }

                override fun onFinish() {
                    item.PayStatus = "2"
                    notifyItemChanged(helper.adapterPosition)
                }
            }
            codeTimer.start()
            timerList.add(codeTimer)
        } else {
            timerGroup.visibility = View.GONE
        }


    }

    fun clearTimer(){
        for (time in timerList){
            time?.cancel()
        }
    }


}