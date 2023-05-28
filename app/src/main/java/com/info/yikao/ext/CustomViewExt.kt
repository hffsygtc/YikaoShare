package com.info.yikao.ext

import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.info.yikao.R
import com.info.yikao.weight.EmptyCallback
import com.info.yikao.weight.ErrorCallback
import com.info.yikao.weight.LoadingCallback
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir


fun Float.px(): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun Float.fpx(): Float {
    val scale = Resources.getSystem().displayMetrics.density
    return (this * scale + 0.5f)
}


fun screenFullWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}


fun screenFullHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

fun LoadService<*>.setErrorText(message: String) {

    if (message.isNotEmpty()) {
        this.setCallBack(ErrorCallback::class.java) { _, view ->
//            view.findViewById<TextView>(R.id.error_text).text = message
        }
    }
}

/**
 * 设置错误布局
 * @param message 错误布局显示的提示内容
 */
fun LoadService<*>.showError(message: String = "") {
    this.setErrorText(message)
    this.showCallback(ErrorCallback::class.java)
}

/**
 * 设置空布局
 */
fun LoadService<*>.showEmpty() {
    this.showCallback(EmptyCallback::class.java)
}

/**
 * 设置加载中
 */
fun LoadService<*>.showLoading() {
    this.showCallback(LoadingCallback::class.java)
}

fun loadServiceInit(view: View, callback: () -> Unit): LoadService<Any> {
    val loadsir = LoadSir.getDefault().register(view) {
        //点击重试时触发的操作
        callback.invoke()
    }
    loadsir.showSuccess()
    return loadsir
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

fun ViewPager2.init(
    fragment: Fragment,
    fragments: ArrayList<Fragment>,
    isUserInputEnabled: Boolean = true
): ViewPager2 {
    //是否可滑动
    this.isUserInputEnabled = isUserInputEnabled
    //设置适配器
    adapter = object : FragmentStateAdapter(fragment) {
        override fun createFragment(position: Int) = fragments[position]
        override fun getItemCount() = fragments.size
    }
    return this
}


fun getGlideRequestOptions(type: Int, corner: Float = 0f): RequestOptions {
    val defaultConner = if (corner > 0) {
        corner.px()
    } else 1
    return when (type) {
        Constant.GLIDE_TYPE_USER_HEAD ->
            RequestOptions()
                .placeholder(R.mipmap.icon_default_user_head)
                .transform(
                    MultiTransformation(
                        CircleCrop()
                    )
                )
        Constant.GLIDE_OPTIONS_SCHOOL_ICON ->
            RequestOptions()
                .placeholder(R.mipmap.default_school_icon)
                .transform(
                    MultiTransformation(
                        CircleCrop()
                    )
                )
        Constant.GLIDE_OPTIONS_SHOW ->
            RequestOptions()
                .placeholder(R.mipmap.default_show_img)
                .transform(
                    MultiTransformation(
                        CircleCrop()
                    )
                )
        Constant.GLIDE_OPTIONS_NEWS ->
            RequestOptions()
                .placeholder(R.mipmap.default_news_img)
                .transform(
                    MultiTransformation(
                        CircleCrop()
                    )
                )
        else -> RequestOptions()
            .placeholder(R.color.color_999999)
            .transform(
                MultiTransformation(
                    CenterCrop(),
                    RoundedCorners(2f.px())
                )
            )
    }
}


/**
 * 拦截BottomNavigation长按事件 防止长按时出现Toast
 * @receiver BottomNavigationViewEx
 * @param ids IntArray
 */
fun BottomNavigationViewEx.interceptLongClick(vararg ids: Int) {
    val bottomNavigationMenuView: ViewGroup = (this.getChildAt(0) as ViewGroup)
    for (index in ids.indices) {
        bottomNavigationMenuView.getChildAt(index).findViewById<View>(ids[index])
            .setOnLongClickListener {
                true
            }
    }
}



fun TextView.showResultContent(content: String) {
    when (content) {
        "合格" -> {
            setTextColor(Color.parseColor("#FF8D31"))
        }
        "优秀" -> {
            setTextColor(Color.parseColor("#7CA861"))
        }
        "不合格" -> {
            setTextColor(Color.parseColor("#FF3434"))
        }
        else -> {
            setTextColor(Color.parseColor("#333333"))
        }
    }
    text = content
}
