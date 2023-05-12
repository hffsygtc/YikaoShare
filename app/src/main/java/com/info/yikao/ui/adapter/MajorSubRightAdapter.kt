package com.info.yikao.ui.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.info.yikao.R
import com.info.yikao.ext.setAdapterAnimation
import com.info.yikao.model.MajorBean
import com.info.yikao.model.MajorGroupBean

class MajorSubRightAdapter(data: MutableList<MajorGroupBean>?) :
    BaseDelegateMultiAdapter<MajorGroupBean, BaseViewHolder>(data) {

    var clickMajor : ((MajorBean)->Unit)? = null

    init {
        //todo 增加设置动画模式的标志
        setAdapterAnimation(0)
        //第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<MajorGroupBean>() {
            override fun getItemType(data: List<MajorGroupBean>, position: Int): Int {
                return 1
            }
        })
        //第二步 绑定对应的item类型
        getMultiTypeDelegate()?.let {
            it.addItemType(1, R.layout.expand_sub_right_layout)
        }
    }

    override fun convert(helper: BaseViewHolder, item: MajorGroupBean) {
        val name = helper.getView<TextView>(R.id.major_parent_name)
        name.text = item.Item.SubjectsName
        helper.itemView.setOnClickListener {
            clickMajor?.invoke(item.Item)
        }

    }


}