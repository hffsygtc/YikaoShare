package com.info.yikao.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.info.yikao.MainActivity
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.*
import com.info.yikao.ext.*
import com.info.yikao.model.OrderBean
import com.info.yikao.ui.adapter.UserOrderListAdapter
import com.info.yikao.viewmodel.SplashViewModel
import com.info.yikao.viewmodel.UploadVideoViewModel
import com.info.yikao.viewmodel.UserOrderViewModel
import com.info.yikao.weight.DefineLoadMoreView
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.logw

class UploadVideoActivity : BaseActivity<UploadVideoViewModel, ActivityUploadVideoBinding>() {

    override fun layoutId(): Int = R.layout.activity_upload_video

    override fun initView(savedInstanceState: Bundle?) {

       mDatabind.titleTv.text = "考试"

    }
}