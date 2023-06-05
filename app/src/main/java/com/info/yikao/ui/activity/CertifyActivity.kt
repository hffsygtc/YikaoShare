package com.info.yikao.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityVertifyInfoBinding
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.logw
import java.io.File

/**
 * 上传身份证认证页面
 */
class CertifyActivity : BaseActivity<BaseViewModel, ActivityVertifyInfoBinding>() {


    private var urlpath: String? = null
    private var imageUri: Uri? = null


    override fun layoutId(): Int = R.layout.activity_vertify_info

    override fun initView(savedInstanceState: Bundle?) {

    }
}