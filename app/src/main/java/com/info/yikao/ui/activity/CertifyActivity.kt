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

class CertifyActivity : BaseActivity<BaseViewModel, ActivityVertifyInfoBinding>() {


    private var urlpath: String? = null
    private var imageUri: Uri? = null


    override fun layoutId(): Int = R.layout.activity_vertify_info

    override fun initView(savedInstanceState: Bundle?) {

    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        "onActivityResult called>>>> $requestCode   $resultCode".logw()
//        if (resultCode === RESULT_OK) {
//            when (requestCode) {
//                PictureConfig.CHOOSE_REQUEST -> {              // 结果回调
//                    var selectList: MutableList<LocalMedia>? =
//                        PictureSelector.obtainMultipleResult(data)
//                    if (selectList != null && selectList.isNotEmpty()) {
//                        val media: LocalMedia = selectList[0]
//                        if (media != null) {
//                            urlpath = if (media.isCut && !media.isCompressed) {
//                                // 裁剪过
//                                media.cutPath
//                            } else if (media.isCompressed || media.isCut && media.isCompressed) {
//                                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
//                                media.compressPath
//                            } else {
//                                // 原图
//                                media.path
//                            }
//
//                            "imageUri  urlpath $urlpath".logw()
//
//                            val file: File = File(urlpath)
//                            imageUri = Uri.fromFile(file)
//
//                            "imageUri  $imageUri".logw()
////                            val result = if (data == null || resultCode != RESULT_OK) null else data.data
//                            var results = arrayOf(imageUri!!)
//                            if (results != null) {
//
//                            } else {
//                                results = arrayOf(imageUri!!)
//                            }
//                        }
//                    } else {
//
//                    }
//                }
//
//            }
//        }
//    }
}