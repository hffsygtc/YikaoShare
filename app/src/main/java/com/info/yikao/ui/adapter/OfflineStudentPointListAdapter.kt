package com.info.yikao.ui.adapter

import android.graphics.Color
import android.widget.TextView
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.info.yikao.R
import com.info.yikao.ext.setAdapterAnimation
import com.info.yikao.ext.showResultContent
import com.info.yikao.model.StudentBean

class OfflineStudentPointListAdapter(data: MutableList<StudentBean>?) :
    BaseDelegateMultiAdapter<StudentBean, BaseViewHolder>(data) {

    init {
        //todo 增加设置动画模式的标志
        setAdapterAnimation(0)
        //第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<StudentBean>() {
            override fun getItemType(data: List<StudentBean>, position: Int): Int {
                return 1
            }
        })
        //第二步 绑定对应的item类型
        getMultiTypeDelegate()?.let {
            it.addItemType(1, R.layout.item_offline_student_point)
        }
    }

    override fun convert(helper: BaseViewHolder, item: StudentBean) {
        val sortId = helper.getView<TextView>(R.id.student_num)
        val name = helper.getView<TextView>(R.id.student_name)
        val num = helper.getView<TextView>(R.id.student_card_id)
        val fullPoint = helper.getView<TextView>(R.id.full_point)
        val myPoint = helper.getView<TextView>(R.id.my_point)
        val state = helper.getView<TextView>(R.id.student_state)

        sortId.text = item.ItemNum.toString()
        name.text = item.RealName
        num.text = item.TestCardNo

        fullPoint.showResultContent(item.JuryTotalResultStr)
        myPoint.showResultContent(item.JuryResultStr)

//        when (item.JuryResult) {
//            1 -> {
//                state.text = "候场中"
//                state.setTextColor(Color.parseColor("#7CA861"))
//            }
//            2 -> {
//                state.text = "考试中"
//                state.setTextColor(Color.parseColor("#FF8D31"))
//            }
//            3 -> {
//                state.text = "已结束"
//                state.setTextColor(Color.parseColor("#FF3434"))
//            }
//            4 -> {
//                state.text = "未到"
//                state.setTextColor(Color.parseColor("#FF34EE"))
//
//            }
//        }

        state.text = "重新评分"
        state.setTextColor(Color.parseColor("#52618B"))


    }


}