package com.info.yikao.ui.adapter

import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.info.yikao.R
import com.info.yikao.ext.setAdapterAnimation
import com.info.yikao.model.TeacherResultBean

class ExamTeacherResultAdapter(data: MutableList<TeacherResultBean>?) :
    BaseDelegateMultiAdapter<TeacherResultBean, BaseViewHolder>(data) {

    init {
        //todo 增加设置动画模式的标志
        setAdapterAnimation(0)
        //第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<TeacherResultBean>() {
            override fun getItemType(data: List<TeacherResultBean>, position: Int): Int {
                return data[position].type
            }
        })
        //第二步 绑定对应的item类型
        getMultiTypeDelegate()?.let {
            it.addItemType(1, R.layout.item_exam_result_head)
            it.addItemType(2, R.layout.item_exam_result_teacher)
            it.addItemType(3, R.layout.item_exam_result_empty)
        }
    }

    override fun convert(helper: BaseViewHolder, item: TeacherResultBean) {
        when (helper.itemViewType) {
            1 -> {

            }
            2 -> {

            }
            3 -> {

            }
        }

    }


}