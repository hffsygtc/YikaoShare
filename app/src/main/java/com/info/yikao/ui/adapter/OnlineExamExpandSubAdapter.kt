package com.info.yikao.ui.adapter

import android.graphics.Color
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.info.yikao.R
import com.info.yikao.ext.setAdapterAnimation
import com.info.yikao.model.MajorGroupBean
import com.info.yikao.model.OnlineExamBean

class OnlineExamExpandSubAdapter(data: MutableList<OnlineExamBean>?) :
    BaseDelegateMultiAdapter<OnlineExamBean, BaseViewHolder>(data) {

    var onlineExamClick: ((OnlineExamBean) -> Unit)? = null

    init {
        //todo 增加设置动画模式的标志
        setAdapterAnimation(0)
        //第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<OnlineExamBean>() {
            override fun getItemType(data: List<OnlineExamBean>, position: Int): Int {
                return 1
            }
        })
        //第二步 绑定对应的item类型
        getMultiTypeDelegate()?.let {
            it.addItemType(1, R.layout.item_online_post_exam)
        }
    }

    override fun convert(helper: BaseViewHolder, item: OnlineExamBean) {
        helper.itemView.setOnClickListener {
            onlineExamClick?.invoke(item)
        }

    }


}