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
import com.info.yikao.model.NewsBean
import com.info.yikao.model.OrderBean
import com.info.yikao.ui.viewholder.TopNewsHolder
import me.hgj.jetpackmvvm.ext.view.visible

class NewsFastAdapter(data: MutableList<NewsBean>?) :
    BaseDelegateMultiAdapter<NewsBean, BaseViewHolder>(data) {

    init {
        //todo 增加设置动画模式的标志
        setAdapterAnimation(0)
        //第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<NewsBean>() {
            override fun getItemType(data: List<NewsBean>, position: Int): Int {
                return 1
            }
        })
        //第二步 绑定对应的item类型
        getMultiTypeDelegate()?.let {
            it.addItemType(1, R.layout.item_top_news)
        }
    }

    override fun convert(helper: BaseViewHolder, item: NewsBean) {
        val holder = TopNewsHolder(helper.itemView)
        item?.let {
            holder.bindData(it,context)
        }
    }


}