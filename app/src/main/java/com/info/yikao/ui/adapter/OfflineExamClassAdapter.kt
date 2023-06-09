package com.info.yikao.ui.adapter

import android.graphics.Color
import android.widget.TextView
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.info.yikao.R
import com.info.yikao.ext.canShow
import com.info.yikao.ext.setAdapterAnimation
import com.info.yikao.model.ExamClassBean
import com.info.yikao.model.StudentBean

class OfflineExamClassAdapter(data: MutableList<ExamClassBean>?) :
    BaseDelegateMultiAdapter<ExamClassBean, BaseViewHolder>(data) {

    init {
        //todo 增加设置动画模式的标志
        setAdapterAnimation(0)
        //第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<ExamClassBean>() {
            override fun getItemType(data: List<ExamClassBean>, position: Int): Int {
                if (data[position].date.canShow()) {
                    return 2
                }
                return 1
            }
        })
        //第二步 绑定对应的item类型
        getMultiTypeDelegate()?.let {
            it.addItemType(1, R.layout.item_exam_class_list)
            it.addItemType(2, R.layout.item_exam_class_list_head)
        }
    }

    override fun convert(helper: BaseViewHolder, item: ExamClassBean) {
        when (helper.itemViewType) {
            2 -> {
                //考试日期的头部
                val dataTv = helper.getView<TextView>(R.id.date_tv)
                dataTv.text = item.date
            }
            else -> {
                //正常的教室
                val timeTv = helper.getView<TextView>(R.id.student_num)
                val majorTv = helper.getView<TextView>(R.id.student_name)
                val classTv = helper.getView<TextView>(R.id.student_card_id)
                val enterTv = helper.getView<TextView>(R.id.student_type)

                timeTv.text = item.TestTimeStart
                majorTv.text = item.SubjectsStr
                classTv.text = item.TestClass
            }
        }


    }


}