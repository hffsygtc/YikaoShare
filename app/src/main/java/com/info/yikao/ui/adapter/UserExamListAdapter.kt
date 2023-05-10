package com.info.yikao.ui.adapter

import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.info.yikao.R
import com.info.yikao.ext.setAdapterAnimation
import com.info.yikao.model.ExamBean
import com.info.yikao.model.OrderBean

class UserExamListAdapter(data: MutableList<ExamBean>?) :
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

    }


}