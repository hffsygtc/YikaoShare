package com.info.yikao.base

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.databinding.ViewDataBinding
import me.hgj.jetpackmvvm.base.fragment.BaseVmDbFragment
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.getAppViewModel
import me.hgj.jetpackmvvm.ext.util.logw

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbFragment<VM, DB>() {

//    private val requestCollectViewModel:Reqc

    //application全局的viewmodel，里面存放了用户信息等配置
    val appViewModel: AppViewModel by lazy { getAppViewModel<AppViewModel>() }

    //application全局的viewmodel,用于发送全局通知
    val eventViewModel: EventViewModel by lazy { getAppViewModel<EventViewModel>() }

    //application全局的viewmodel,用于发送全局通知
    val userViewModel: UserViewModel by lazy { getAppViewModel<UserViewModel>() }

    abstract override fun layoutId(): Int

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 懒加载，只有当前fragment视图显示的时候才会触发
     */
    override fun lazyLoadData() {
    }

    /**
     * 创建livedata观察者，fragment执行onviewcreated后触发
     */
    override fun createObserver() {
    }

    /**
     *fragment执行onviewcreated后触发
     */
    override fun initData() {
    }


    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
        "网络请求加载框 showLoading $message".logw()
//        showLoadingExt(message)
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
//        dismissLoadingExt()
    }

    override fun onPause() {
        super.onPause()
        hideSoftKeyboard(activity)
    }

    /**
     * 延迟加载 防止 切换动画还没执行完毕时数据就已经加载好了，这时页面会有渲染卡顿  bug
     * 这里传入你想要延迟的时间，延迟时间可以设置比转场动画时间长一点 单位： 毫秒
     * 不传默认 300毫秒
     * @return Long
     */
    override fun lazyLoadTime(): Long {
        return 300
    }

    /**
     * 隐藏软键盘
     */
    fun hideSoftKeyboard(activity: Activity?) {
        activity?.let { act ->
            val view = act.currentFocus
            view?.let {
                val inputMethodManager =
                    act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }
}