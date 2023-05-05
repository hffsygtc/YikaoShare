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
import com.info.yikao.model.MajorTypeBean
import com.info.yikao.model.OrderBean
import com.skydoves.expandablelayout.ExpandableLayout
import me.hgj.jetpackmvvm.ext.util.loge
import me.hgj.jetpackmvvm.ext.util.logw

class SchoolMajorExpandedAdapter(data: MutableList<MajorTypeBean>?) :
    BaseDelegateMultiAdapter<MajorTypeBean, BaseViewHolder>(data) {

    init {
        //todo 增加设置动画模式的标志
        setAdapterAnimation(0)
        //第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<MajorTypeBean>() {
            override fun getItemType(data: List<MajorTypeBean>, position: Int): Int {
                return 1
            }
        })
        //第二步 绑定对应的item类型
        getMultiTypeDelegate()?.let {
            it.addItemType(1, R.layout.item_school_major_list)
        }
    }

    override fun convert(helper: BaseViewHolder, item: MajorTypeBean) {
        val subLayout = helper.getView<ConstraintLayout>(R.id.sub_content_layout)

        val parentName = helper.getView<TextView>(R.id.major_parent_name)
        val parentOpenIcon = helper.getView<ImageView>(R.id.major_parent_icon)

        parentName.text = item.name

        val subLeftRv = helper.getView<RecyclerView>(R.id.major_child_left_rv)
        val subRightRv = helper.getView<RecyclerView>(R.id.major_child_right_rv)

        val leftAdapter = MajorSubLeftAdapter(item.majors)
        subLeftRv.init(LinearLayoutManager(context), leftAdapter)

        val rightAdapter = MajorSubRightAdapter(item.majors[0].majors)
        subRightRv.init(LinearLayoutManager(context), rightAdapter)
        leftAdapter.selectPosChange = {pos->
            //左侧的内容切换了
            val majors = item.majors.getOrNull(pos)
            val subMajors = majors?.majors
            if (subMajors != null){
                rightAdapter.data =subMajors!!
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