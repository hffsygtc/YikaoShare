package com.info.yikao.base

import android.content.Intent
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.databinding.ViewDataBinding
import com.zackratos.ultimatebarx.ultimatebarx.statusBarOnly
import me.hgj.jetpackmvvm.base.activity.BaseVmDbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.getAppViewModel

/**
 * 这是项目中的activity基类，在这里封装实现弹窗，吐司
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbActivity<VM, DB>() {

    //application全局的viewmodel，里面存放了一些用户信息，配置等
    val appViewModel: AppViewModel by lazy { getAppViewModel<AppViewModel>() }

    //application全局的viewmodel，用于发送全局通知操作
    val eventViewModel: EventViewModel by lazy { getAppViewModel<EventViewModel>() }

    //application全局的viewmodel，用于记录用户相关信息
    val userViewModel: UserViewModel by lazy { getAppViewModel<UserViewModel>() }


    var mActivityJumpTag: String? = null
    var mClickTime: Long = 0

    abstract override fun layoutId(): Int

    abstract override fun initView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        showMajorStatusBar()
        super.onCreate(savedInstanceState)
    }

    /**
     * 创建livedata观察者
     */
    override fun createObserver() {
    }

    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
//        showLoadingExt(message)
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
//        dismissLoadingExt()
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        if (checkDoubleClick(intent)) {
            super.startActivityForResult(intent, requestCode)
        }
    }

    private fun checkDoubleClick(intent: Intent?): Boolean {
        //默认检查通过
        var result = true
        var tag: String? = null
        intent?.let {
            if (it.component != null) {
                //显示跳转
                tag = it.component!!.className
            } else if (intent.action != null) {
                //隐式跳转
                tag = it.action
            } else {
                return true
            }
        }

        if (tag.equals(mActivityJumpTag) && mClickTime >= SystemClock.uptimeMillis() - 1000) {
            //检查不通过
            result = false
        }

        mActivityJumpTag = tag
        mClickTime = SystemClock.uptimeMillis()
        return result
    }

    protected fun showTransStatusBar() {
        statusBarOnly {
            fitWindow = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun setPageGrey() {
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        paint.colorFilter = ColorMatrixColorFilter(cm)
        window.decorView.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
    }

    open fun showMajorStatusBar() {
        statusBarOnly {
            fitWindow = true
            color = Color.parseColor("#FFFFFF")
            light = true
            lvlColor = Color.parseColor("#FFFFFF")
        }
    }

}