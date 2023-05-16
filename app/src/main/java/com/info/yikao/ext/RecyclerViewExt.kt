package com.info.yikao.ext

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.material.snackbar.Snackbar
import com.info.yikao.model.ListDataUiState
import com.info.yikao.weight.DefineLoadMoreView
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import me.hgj.jetpackmvvm.base.appContext


//绑定普通的Recyclerview
fun RecyclerView.init(
    layoutManger: RecyclerView.LayoutManager,
    bindAdapter: RecyclerView.Adapter<*>,
    isScroll: Boolean = true
): RecyclerView {
    layoutManager = layoutManger
    setHasFixedSize(true)
    adapter = bindAdapter
    isNestedScrollingEnabled = isScroll
    return this
}

//绑定SwipeRecyclerView
fun SwipeRecyclerView.init(
    layoutManger: RecyclerView.LayoutManager,
    bindAdapter: RecyclerView.Adapter<*>,
    isScroll: Boolean = true
): SwipeRecyclerView {
    layoutManager = layoutManger
    setHasFixedSize(true)
    adapter = bindAdapter
    isNestedScrollingEnabled = isScroll
    return this
}


//初始化 SwipeRefreshLayout
fun SwipeRefreshLayout.init(onRefreshListener: () -> Unit) {
    this.run {
        setOnRefreshListener {
            onRefreshListener.invoke()
        }
    }
}


//绑定SwipeRecyclerView添加加载更多的布局
fun SwipeRecyclerView.initFooter(loadmoreListener: SwipeRecyclerView.LoadMoreListener): DefineLoadMoreView {
    val footerView = DefineLoadMoreView(appContext)
    //设置尾部点击回调
    footerView.setmLoadMoreListener(SwipeRecyclerView.LoadMoreListener {
        footerView.onLoading()
        loadmoreListener.onLoadMore()
    })
    this.run {
        //添加加载更多尾部
        addFooterView(footerView)
        setLoadMoreView(footerView)
        //设置加载更多回调
        setLoadMoreListener(loadmoreListener)
    }
    return footerView
}

/**
 * 加载列表数据
 * 泛型函数，传入包装好的列表数据(含有对应的判断标识位)
 * 将ui相关的逻辑进行封装
 */
fun <T> loadListData(
    data: ListDataUiState<T>,
    baseQuickAdapter: BaseQuickAdapter<T, *>,
    loadService: LoadService<*>,
    recyclerView: SwipeRecyclerView,
    swipeRefreshLayout: SwipeRefreshLayout
) {
    //收起刷新布局
    swipeRefreshLayout.isRefreshing = false
    //控制加载更多
    recyclerView.loadMoreFinish(data.isEmpty, data.hasMore)
    if (data.isSuccess) {
        //成功
        when {
            //第一页并没有数据 显示空布局界面
            data.isFirstEmpty -> {
                loadService.showEmpty()
            }
            //是第一页
            data.isRefresh -> {
                if (data.isEmpty) {
                    loadService.showEmpty()
                } else {
                    baseQuickAdapter.setList(data.listData)
                    loadService.showSuccess()
                }
            }
            //不是第一页
            else -> {
                baseQuickAdapter.addData(data.listData)
                loadService.showSuccess()
            }
        }
    } else {
        //失败
        if (data.isRefresh) {
            //如果是第一页，则显示错误界面，并提示错误信息
            loadService.showError(data.errMessage)
            Snackbar.make(recyclerView, data.errMessage, Snackbar.LENGTH_SHORT).show()
        } else {
            recyclerView.loadMoreError(0, data.errMessage)
        }
    }
}


//设置适配器的列表动画
fun BaseQuickAdapter<*, *>.setAdapterAnimation(mode: Int) {
    //等于0，关闭列表动画 否则开启
    if (mode == 0) {
        this.animationEnable = false
    } else {
        this.animationEnable = true
        this.setAnimationWithDefault(BaseQuickAdapter.AnimationType.values()[mode - 1])
    }
}
