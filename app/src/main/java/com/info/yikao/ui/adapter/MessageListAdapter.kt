package com.info.yikao.ui.adapter

import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.info.yikao.R
import com.info.yikao.ext.setAdapterAnimation
import com.info.yikao.model.MessageBean
import com.info.yikao.model.StreetShowBean
import com.info.yikao.ui.viewholder.ShowNewsHolder

class MessageListAdapter(data: MutableList<MessageBean>?) :
    BaseDelegateMultiAdapter<MessageBean, BaseViewHolder>(data) {

    init {
        //todo 增加设置动画模式的标志
        setAdapterAnimation(0)
        //第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<MessageBean>() {
            override fun getItemType(data: List<MessageBean>, position: Int): Int {
                return 1
            }
        })
        //第二步 绑定对应的item类型
        getMultiTypeDelegate()?.let {
            it.addItemType(1, R.layout.item_list_msg)
        }
    }

    override fun convert(helper: BaseViewHolder, item: MessageBean) {

        val headIcon = helper.getView<ImageView>(R.id.head_icon)
        val nameTv = helper.getView<TextView>(R.id.title)
        val timeTv = helper.getView<TextView>(R.id.time_tv)
        val content = helper.getView<TextView>(R.id.class_content_tv)

    }


}