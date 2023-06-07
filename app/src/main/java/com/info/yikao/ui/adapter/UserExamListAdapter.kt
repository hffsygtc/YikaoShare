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
import com.info.yikao.ext.canShow
import com.info.yikao.ext.getGlideRequestOptions
import com.info.yikao.ext.setAdapterAnimation
import com.info.yikao.model.ExamBean

class UserExamListAdapter(data: MutableList<ExamBean>?,var onlineState:Boolean) :
    BaseDelegateMultiAdapter<ExamBean, BaseViewHolder>(data) {

    init {
        //todo 增加设置动画模式的标志
        setAdapterAnimation(0)
        //第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<ExamBean>() {
            override fun getItemType(data: List<ExamBean>, position: Int): Int {
                return 1
            }
        })
        //第二步 绑定对应的item类型
        getMultiTypeDelegate()?.let {
            it.addItemType(1, R.layout.item_user_exam_list)
        }
    }

    override fun convert(helper: BaseViewHolder, item: ExamBean) {
        val subjectName = helper.getView<TextView>(R.id.order_info)
        val payState = helper.getView<TextView>(R.id.pay_state)
        val schoolLogo = helper.getView<ImageView>(R.id.school_icon)
        val schoolName = helper.getView<TextView>(R.id.school_name)
        val examTime = helper.getView<TextView>(R.id.order_time)

        val bottomGroup = helper.getView<Group>(R.id.bottom_more_group)
        val certName = helper.getView<TextView>(R.id.cert_tv)
        val certBtn = helper.getView<TextView>(R.id.show_cert_btn)
        val stuCardBtn = helper.getView<TextView>(R.id.stu_card_btn)


        val schoolIcon = Constant.imgUrlHead + item.Logo
        Glide.with(context).load(schoolIcon)
            .apply(getGlideRequestOptions(Constant.GLIDE_OPTIONS_SCHOOL_ICON))
            .into(schoolLogo)
        schoolName.text = item.SchoolName
        when (item.TestResult) {
            1 -> {
                //通过
                payState.setBackgroundResource(R.drawable.green_corner_10)
                payState.text = "通过"
            }
            2 -> {
                //未通过
                payState.setBackgroundResource(R.drawable.red_corner_10)
                payState.text = "未通过"
            }
            else -> {
                //等待结果
                payState.setBackgroundResource(R.drawable.orange_corner_10)
                payState.text = "等待结果"
            }
        }

        subjectName.text = item.SubjectsName
        examTime.text = "考试时间：${item.TestStart}"


        if (item.TestResult == 1 && item.CertificateNo.canShow()) {
            bottomGroup.visibility = View.VISIBLE
            certName.text = item.SubjectsName
        } else {
            bottomGroup.visibility = View.GONE
        }

        if (onlineState){
            //线上考试，显示进入考试
            stuCardBtn.text = "进入考试"
            if (!item.OnLineVideoUrl.canShow()) {
                stuCardBtn.visibility = View.VISIBLE
            } else {
                stuCardBtn.visibility = View.GONE
            }
        }else{
            //线下考试，显示准考证
            stuCardBtn.text = "准考证"
            if (item.TestCardNo.canShow()) {
                stuCardBtn.visibility = View.VISIBLE
            } else {
                stuCardBtn.visibility = View.GONE
            }
        }
    }


}