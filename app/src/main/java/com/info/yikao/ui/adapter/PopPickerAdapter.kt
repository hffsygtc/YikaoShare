package com.info.yikao.ui.adapter

import android.graphics.Color
import android.widget.TextView
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.info.yikao.R
import com.info.yikao.ext.setAdapterAnimation
import com.info.yikao.model.ClassAndSortBean

class PopPickerAdapter(data: MutableList<ClassAndSortBean>?) :
    BaseDelegateMultiAdapter<ClassAndSortBean, BaseViewHolder>(data) {

    var selectPos = -1

    init {
        //todo 增加设置动画模式的标志
        setAdapterAnimation(0)
        //第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<ClassAndSortBean>() {
            override fun getItemType(data: List<ClassAndSortBean>, position: Int): Int {
                return 1
            }
        })
        //第二步 绑定对应的item类型
        getMultiTypeDelegate()?.let {
            it.addItemType(1, R.layout.item_pop_tv)
        }
    }

    override fun convert(helper: BaseViewHolder, item: ClassAndSortBean) {
        val name = helper.getView<TextView>(R.id.content_tv)

        name.text = item.name

        if (helper.adapterPosition == selectPos){
            name.setBackgroundColor(Color.parseColor("#EAF0FF"))
        }else{
            name.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }

        helper.itemView.setOnClickListener {
            selectPos = helper.adapterPosition
            notifyDataSetChanged()
        }

    }


}