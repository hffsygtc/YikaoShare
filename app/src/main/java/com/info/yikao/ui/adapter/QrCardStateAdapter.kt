package com.info.yikao.ui.adapter

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.info.yikao.R
import com.info.yikao.ext.setAdapterAnimation
import com.info.yikao.model.MessageBean
import com.info.yikao.model.QrCardStateBean
import com.info.yikao.model.StreetShowBean
import com.info.yikao.ui.viewholder.ShowNewsHolder

class QrCardStateAdapter(data: MutableList<QrCardStateBean>?) :
    BaseDelegateMultiAdapter<QrCardStateBean, BaseViewHolder>(data) {

    init {
        //todo 增加设置动画模式的标志
        setAdapterAnimation(0)
        //第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<QrCardStateBean>() {
            override fun getItemType(data: List<QrCardStateBean>, position: Int): Int {
                return 1
            }
        })
        //第二步 绑定对应的item类型
        getMultiTypeDelegate()?.let {
            it.addItemType(1, R.layout.item_qr_card_state)
        }
    }

    override fun convert(helper: BaseViewHolder, item: QrCardStateBean) {
        val topLine = helper.getView<View>(R.id.top_line)
        val bottomLine = helper.getView<View>(R.id.bottom_line)
        val timeTv = helper.getView<TextView>(R.id.time_tv)
        val resultIcon = helper.getView<ImageView>(R.id.result_icon)
        val contentTv = helper.getView<TextView>(R.id.content_tv)

        if (helper.absoluteAdapterPosition == 0) {
            topLine.visibility = View.GONE
        } else {
            topLine.visibility = View.VISIBLE
        }

        if (helper.absoluteAdapterPosition == (itemCount - 1)) {
            bottomLine.visibility = View.GONE
        } else {
            bottomLine.visibility = View.VISIBLE
        }


        if (helper.absoluteAdapterPosition == (itemCount - 1)) {
            contentTv.setTextColor(Color.parseColor("#FF3434"))
            resultIcon.visibility = View.VISIBLE
        }else{
            contentTv.setTextColor(Color.parseColor("#333333"))
            resultIcon.visibility = View.GONE
        }

        timeTv.text = "05-15 08:01:01"
        contentTv.text = "评委打分结束，请等待最终结果。"
    }


}