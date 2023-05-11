package com.info.yikao.ui.adapter

import android.graphics.Color
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.info.yikao.R
import com.info.yikao.ext.setAdapterAnimation
import com.info.yikao.model.MajorGroupBean

class MajorSubLeftAdapter(data: MutableList<MajorGroupBean>?) :
    BaseDelegateMultiAdapter<MajorGroupBean, BaseViewHolder>(data) {

    private val holderArray = SparseArray<BaseViewHolder>()
    private var selectPos = 0

    var selectPosChange : ((Int)->Unit)? = null

    private val whiteColor = Color.parseColor("#FFFFFF")
    private val blackColor = Color.parseColor("#333333")

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
            it.addItemType(1, R.layout.expand_sub_left_layout)
        }
    }

    override fun convert(helper: BaseViewHolder, item: MajorGroupBean) {
        holderArray[helper.adapterPosition] = helper

        val nameTv = helper.getView<TextView>(R.id.major_parent_name)
        val angleIcon = helper.getView<ImageView>(R.id.major_parent_icon)

        if (helper.adapterPosition == selectPos) {
            angleIcon.visibility = View.VISIBLE
        } else {
            angleIcon.visibility = View.GONE
        }

        helper.itemView.setOnClickListener {
            if (helper.adapterPosition != selectPos) {
                //点击了其他的位置
                val hisHolder = holderArray[selectPos]
                val hisAngleIcon = hisHolder.getView<ImageView>(R.id.major_parent_icon)
                val hisName = hisHolder.getView<TextView>(R.id.major_parent_name)

                hisAngleIcon.visibility = View.GONE
                angleIcon.visibility = View.VISIBLE

                nameTv.setBackgroundResource(R.drawable.blue_corner_10)
                nameTv.setTextColor(whiteColor)
                hisName.setBackgroundResource(R.drawable.white_corner_10)
                hisName.setTextColor(blackColor)

                selectPos = helper.adapterPosition

                selectPosChange?.invoke(helper.adapterPosition)
            }
        }

        if (helper.adapterPosition == selectPos){
            //显示选中的样式
            nameTv.setBackgroundResource(R.drawable.blue_corner_10)
            nameTv.setTextColor(whiteColor)
        }else{
            //显示普通样式
            nameTv.setBackgroundResource(R.drawable.white_corner_10)
            nameTv.setTextColor(blackColor)
        }

        nameTv.text = item.Item.SubjectsName

    }


}