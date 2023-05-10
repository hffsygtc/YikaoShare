package com.info.yikao.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.info.yikao.R
import com.info.yikao.model.MainListWarpper
import com.info.yikao.ui.viewholder.ShowNewsHolder
import com.info.yikao.ui.viewholder.SchoolListHolder
import com.info.yikao.ui.viewholder.TopNewsHolder

class MainHomeListAdapter(private val mContext: Context, var list: ArrayList<MainListWarpper>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var enterMoreClick: ((String) -> Unit)? = null

    var itemClickListener: ((MainListWarpper) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        when (viewType) {
            1 -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_top_news, parent, false)
                return TopNewsHolder(view)
            }
            2 -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_school_sign, parent, false)
                return SchoolListHolder(view)
            }
            3 -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_show_news, parent, false)
                return ShowNewsHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_home_section_title, parent, false)
                return TitleHolder(view)
            }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bean = list.getOrNull(position)
        if (bean != null) {
            when (holder) {
                is TitleHolder -> showTitle(holder, bean, position == 0)
                is TopNewsHolder -> showTopNews(holder, bean)
                is SchoolListHolder -> showSignUp(holder, bean)
                is ShowNewsHolder -> showOutsideView(holder, bean)
            }
        }


//        Glide.with(appContext).load(data?.get(position)?.section_avatar)
//            .apply(getGlideRequestOptions(Constant.GLIDE_NEWSPAPER))
//            .into(holder.coverImg)
//
//        holder.itemView.setOnClickListener {
//            itemFunctionAction?.invoke(data?.get(position), position)
//        }

    }

    /**
     * 显示路演信息
     */
    private fun showOutsideView(holder: ShowNewsHolder, bean: MainListWarpper) {
        bean.streetBean?.let {
            holder.bindData(it, mContext)

            holder.itemView.setOnClickListener {
                itemClickListener?.invoke(bean)
            }
        }
    }

    /**
     * 显示艺考报名
     */
    private fun showSignUp(holder: SchoolListHolder, bean: MainListWarpper) {
        bean.schoolBean?.let {
            holder.bindData(it, mContext)

            holder.itemView.setOnClickListener {
                itemClickListener?.invoke(bean)
            }
        }
    }

    /**
     * 显示艺考资讯
     */
    private fun showTopNews(holder: TopNewsHolder, bean: MainListWarpper) {
        bean.newsBean?.let {
            holder.bindData(it, mContext)

            holder.itemView.setOnClickListener {
                itemClickListener?.invoke(bean)
            }
        }
    }

    /**
     * 显示分割的标题部分
     */
    private fun showTitle(
        holder: MainHomeListAdapter.TitleHolder,
        bean: MainListWarpper,
        isHead: Boolean
    ) {
        if (isHead) {
            holder.topGap.visibility = View.GONE
        } else {
            holder.topGap.visibility = View.VISIBLE
        }

        holder.titleTv.text = bean.titleName

        holder.moreBtn.setOnClickListener {
            enterMoreClick?.invoke(bean.titleName ?: "")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    override fun getItemCount(): Int = list.size


    inner class TitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val topGap = itemView.findViewById<View>(R.id.gap_line)
        val moreBtn = itemView.findViewById<View>(R.id.more_btn)
        val titleTv = itemView.findViewById<TextView>(R.id.title)
    }

}