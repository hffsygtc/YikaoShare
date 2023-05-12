package com.info.yikao.ui.adapter

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.info.yikao.R
import com.info.yikao.ext.setAdapterAnimation
import com.info.yikao.model.MajorIntroWrapper

class MajorIntroAdapter(data: MutableList<MajorIntroWrapper>?) :
    BaseDelegateMultiAdapter<MajorIntroWrapper, BaseViewHolder>(data) {

    init {
        //todo 增加设置动画模式的标志
        setAdapterAnimation(0)
        //第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<MajorIntroWrapper>() {
            override fun getItemType(data: List<MajorIntroWrapper>, position: Int): Int {
                return data[position].type
            }
        })
        //第二步 绑定对应的item类型
        getMultiTypeDelegate()?.let {
            it.addItemType(1, R.layout.item_major_intro_head)
            it.addItemType(2, R.layout.item_major_exam_subject)
            it.addItemType(3, R.layout.item_major_more_info)
        }
    }

    override fun convert(helper: BaseViewHolder, item: MajorIntroWrapper) {
        when (helper.itemViewType) {
            1 -> {
                //顶部的时间
                val signTime = helper.getView<TextView>(R.id.sign_time_tv)
                val examTime = helper.getView<TextView>(R.id.exam_time_tv)
                val major = helper.getView<TextView>(R.id.major_name_tv)
                val method = helper.getView<TextView>(R.id.method_tv)

                signTime.text = item.signInfo?.signTime
                examTime.text = item.signInfo?.examTime
                major.text = item.signInfo?.majorName
                method.text = item.signInfo?.method
            }
            2 -> {
                //科目
                val headTv = helper.getView<TextView>(R.id.user_study_tv)
                val sortTv = helper.getView<TextView>(R.id.major_sort_tv)
                val nameTv = helper.getView<TextView>(R.id.major_name)
                val mustTv = helper.getView<TextView>(R.id.must_tv)
                val contentTv = helper.getView<TextView>(R.id.exam_content)

                sortTv.text = item.examBean?.sort
                nameTv.text = item.examBean?.name
                val mustStr = if (item.examBean?.must == true) {
                    "必考"
                } else {
                    "非必考"
                }
                mustTv.text = mustStr
                contentTv.text = item.examBean?.content

                if (item.isHead){
                    headTv.visibility = View.VISIBLE
                }else{
                    headTv.visibility = View.GONE
                }
            }
            3 -> {
                //更多的信息
                val title = helper.getView<TextView>(R.id.head_tv)
                val content = helper.getView<TextView>(R.id.exam_content)

                title.text = item.descInfo?.name
                content.text = item.descInfo?.content

            }
        }

    }


}