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
import com.info.yikao.model.MajorBean
import com.info.yikao.model.MajorGroupBean

class SchoolMajorExpandedAdapter(data: MutableList<MajorGroupBean>?) :
    BaseDelegateMultiAdapter<MajorGroupBean, BaseViewHolder>(data) {

    var clickMajor: ((MajorBean) -> Unit)? = null

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
            it.addItemType(1, R.layout.item_school_major_list)
        }
    }

    override fun convert(helper: BaseViewHolder, item: MajorGroupBean) {
        val subLayout = helper.getView<ConstraintLayout>(R.id.sub_content_layout)

        val parentName = helper.getView<TextView>(R.id.major_parent_name)
        val parentOpenIcon = helper.getView<ImageView>(R.id.major_parent_icon)

        parentName.text = item.Item.SubjectsName

        val subLeftRv = helper.getView<RecyclerView>(R.id.major_child_left_rv)
        val subRightRv = helper.getView<RecyclerView>(R.id.major_child_right_rv)

        val leftAdapter = MajorSubLeftAdapter(item.Children)
        subLeftRv.init(LinearLayoutManager(context), leftAdapter)

        val rightAdapter = MajorSubRightAdapter(item.Children?.get(0)?.Children)
        subRightRv.init(LinearLayoutManager(context), rightAdapter)

        rightAdapter.clickMajor = clickMajor

        leftAdapter.selectPosChange = { pos ->
            //左侧的内容切换了
            val majors = item.Children?.getOrNull(pos)
            val subMajors = majors?.Children
            if (subMajors != null) {
                rightAdapter.data = subMajors!!
                rightAdapter.notifyDataSetChanged()
            }
        }


        parentName.setOnClickListener {
            if (item.open) {
                //收起 关闭
                subLayout.visibility = View.GONE
                parentOpenIcon.setBackgroundResource(R.mipmap.icon_major_list_close)
                parentName.setBackgroundResource(R.drawable.major_close_bg)

            } else {
                subLayout.visibility = View.VISIBLE
                parentOpenIcon.setBackgroundResource(R.mipmap.icon_major_list_open)
                parentName.setBackgroundResource(R.drawable.major_close_top_bg)
            }
            item.open = !item.open

        }
    }


}