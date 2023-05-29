package com.info.yikao.ui.adapter

import android.graphics.Color
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.info.yikao.R
import com.info.yikao.ext.Constant
import com.info.yikao.ext.getGlideRequestOptions
import com.info.yikao.ext.setAdapterAnimation
import com.info.yikao.ext.showResultContent
import com.info.yikao.model.MajorGroupBean
import com.info.yikao.model.OnlineExamBean

class OnlineExamExpandSubAdapter(val graded: Boolean = false, data: MutableList<OnlineExamBean>?) :
    BaseDelegateMultiAdapter<OnlineExamBean, BaseViewHolder>(data) {

    var onlineExamClick: ((OnlineExamBean) -> Unit)? = null

    init {
        //todo 增加设置动画模式的标志
        setAdapterAnimation(0)
        //第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<OnlineExamBean>() {
            override fun getItemType(data: List<OnlineExamBean>, position: Int): Int {
                return 1
            }
        })
        //第二步 绑定对应的item类型
        getMultiTypeDelegate()?.let {
            it.addItemType(1, R.layout.item_online_post_exam)
        }
    }

    override fun convert(helper: BaseViewHolder, item: OnlineExamBean) {
        val topImg = helper.getView<ImageView>(R.id.top_cover_img)
        val name = helper.getView<TextView>(R.id.name_tv)
        val subjectTv = helper.getView<TextView>(R.id.subject_content_tv)

        val pointName = helper.getView<TextView>(R.id.point_tv)
        val pointTv = helper.getView<TextView>(R.id.point_content_tv)
        val commentName = helper.getView<TextView>(R.id.comment_tv)
        val commentTv = helper.getView<TextView>(R.id.comment_content_tv)
        val pointBtn = helper.getView<TextView>(R.id.go_point_btn)

        if (graded) {
            //已评分的列表
            pointName.visibility = View.VISIBLE
            pointTv.visibility = View.VISIBLE
            commentName.visibility = View.VISIBLE
            commentTv.visibility = View.VISIBLE
            pointBtn.visibility = View.GONE

            pointTv.showResultContent(item.JuryResultStr)
            commentTv.text = item.Remark
        } else {
            //未评分的列表
            pointName.visibility = View.GONE
            pointTv.visibility = View.GONE
            commentName.visibility = View.GONE
            commentTv.visibility = View.GONE
            pointBtn.visibility = View.VISIBLE
        }

        val cover = Constant.imgUrlHead+""
        Glide.with(context).load(cover)
            .apply(getGlideRequestOptions(Constant.GLIDE_TYPE_DEFAULT))
            .into(topImg)

        name.text = item.RealName
        subjectTv.text = item.SubjectsStr

        helper.itemView.setOnClickListener {
            onlineExamClick?.invoke(item)
        }

    }


}