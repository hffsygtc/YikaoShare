package com.info.yikao.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.info.yikao.R
import com.info.yikao.ext.init
import com.info.yikao.ext.setAdapterAnimation
import com.info.yikao.model.OnlineExamBean
import com.info.yikao.model.OnlineListBean

class OnlineExamExpandAdapter(data: MutableList<OnlineListBean>?) :
    BaseDelegateMultiAdapter<OnlineListBean, BaseViewHolder>(data) {

    var clickExam: ((OnlineExamBean) -> Unit)? = null

    init {
        //todo 增加设置动画模式的标志
        setAdapterAnimation(0)
        //第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<OnlineListBean>() {
            override fun getItemType(data: List<OnlineListBean>, position: Int): Int {
                return 1
            }
        })
        //第二步 绑定对应的item类型
        getMultiTypeDelegate()?.let {
            it.addItemType(1, R.layout.item_online_exam_list)
        }
    }

    override fun convert(helper: BaseViewHolder, item: OnlineListBean) {
        val subLayout = helper.getView<ConstraintLayout>(R.id.sub_content_layout)

        val parentView = helper.getView<View>(R.id.head_view)
        val parentName = helper.getView<TextView>(R.id.head_name)
        val parentTime = helper.getView<TextView>(R.id.head_time)
        val parentOpenIcon = helper.getView<ImageView>(R.id.major_parent_icon)

        parentName.text = item.SubjectsStr
        parentTime.text = item.TestStartStr

        val subTopRv = helper.getView<RecyclerView>(R.id.major_child_top_rv)
        val subTopTv = helper.getView<TextView>(R.id.pointed_name)
        val subBottomRv = helper.getView<RecyclerView>(R.id.major_child_bottom_rv)
        val subBottomTv = helper.getView<TextView>(R.id.no_pointed_name)

        val leftAdapter = OnlineExamExpandSubAdapter(true,item.GradedList)
        leftAdapter.onlineExamClick = clickExam
        subTopRv.init(LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false), leftAdapter)

        if (item.GradedList.isNotEmpty()){
            //如果存在已经评分的数据
            subTopTv.text = "已评分（${item.GradedList.size}）"
            subTopRv.visibility = View.VISIBLE
        }else{
            //还没有已经评分的数据
            subTopTv.text = "已评分（0）"
            subTopRv.visibility = View.GONE
        }

        val rightAdapter = OnlineExamExpandSubAdapter(false,item.UnGradedList)
        rightAdapter.onlineExamClick = clickExam
        subBottomRv.init(LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false), rightAdapter)

        if (item.UnGradedList.isNotEmpty()){
            //如果存在已经评分的数据
            subBottomTv.text = "未评分（${item.UnGradedList.size}）"
            subBottomRv.visibility = View.VISIBLE
        }else{
            //还没有已经评分的数据
            subBottomTv.text = "未评分（0）"
            subBottomRv.visibility = View.GONE
        }

        parentView.setOnClickListener {
            if (item.open) {
                //收起 关闭
                subLayout.visibility = View.GONE
                parentOpenIcon.setBackgroundResource(R.mipmap.icon_major_list_close)
                parentView.setBackgroundResource(R.drawable.white_corner_15)

            } else {
                subLayout.visibility = View.VISIBLE
                parentOpenIcon.setBackgroundResource(R.mipmap.icon_major_list_open)
                parentView.setBackgroundResource(R.drawable.white_15_top)
            }
            item.open = !item.open

        }
    }


}