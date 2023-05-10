package com.info.yikao.ui.adapter

import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.info.yikao.R
import com.info.yikao.ext.setAdapterAnimation
import com.info.yikao.model.SchoolBean
import com.info.yikao.ui.viewholder.SchoolListHolder

class SchoolListAdapter(data: MutableList<SchoolBean>?) :
    BaseDelegateMultiAdapter<SchoolBean, BaseViewHolder>(data) {

    init {
        //todo 增加设置动画模式的标志
        setAdapterAnimation(0)
        //第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<SchoolBean>() {
            override fun getItemType(data: List<SchoolBean>, position: Int): Int {
                return 1
            }
        })
        //第二步 绑定对应的item类型
        getMultiTypeDelegate()?.let {
            it.addItemType(1, R.layout.item_school_sign)
        }
    }

    override fun convert(helper: BaseViewHolder, item: SchoolBean) {
        val holder = SchoolListHolder(helper.itemView)
        item?.let {
            holder.bindData(it,context)
        }
    }


}