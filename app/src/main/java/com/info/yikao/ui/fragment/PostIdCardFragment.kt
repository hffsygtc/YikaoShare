package com.info.yikao.ui.fragment

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentIdCardPhotoBinding
import com.info.yikao.util.GlideEngine
import com.info.yikao.viewmodel.PostIdCardViewModel
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.permissionx.guolindev.PermissionX
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.util.logw
import java.io.File

class PostIdCardFragment : BaseFragment<PostIdCardViewModel, FragmentIdCardPhotoBinding>() {


    private var frontUrlpath: String? = null
    private var frontImageUri: Uri? = null

    private var backUrlpath: String? = null
    private var backImageUri: Uri? = null

    private var isBack = false

    override fun layoutId(): Int = R.layout.fragment_id_card_photo

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.titleBackBtn.setOnClickListener {
            activity?.finish()
        }

        mDatabind.frontLayout.setOnClickListener {
            isBack = false
            //点击上传正面
            PermissionX.init(requireActivity())
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason { scope, deniedList ->
                    val message = "上传本地图片需要您同意获取读取权限才能正常使用"
                    scope.showRequestReasonDialog(
                        deniedList,
                        message,
                        "同意",
                        "拒绝"
                    )
                }
                .request { allGranted, _, _ ->
                    if (allGranted) {
                        //用户授权文件读取
                        //"用户授权文件读取".logw()
                        PictureSelector.create(this)
                            .openGallery(PictureMimeType.ofImage())
                            .imageEngine(GlideEngine.createGlideEngine())
                            .theme(com.luck.picture.lib.R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                            .maxSelectNum(1)// 最大图片选择数量
                            .minSelectNum(1)// 最小选择数量
                            .imageSpanCount(4)// 每行显示个数
                            .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                            //.previewImage(false)// 是否可预览图片
                            //.previewVideo(false)
                            .isCamera(true)// 是否显示拍照按钮
                            //.isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                            //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                            //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                            .enableCrop(true)// 是否裁剪
                            .compress(true)// 是否压缩
                            .synOrAsy(true)//同步true或异步false 压缩 默认同步
                            //.compressSavePath(getPath())//压缩图片保存地址
                            //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                            .glideOverride(
                                160,
                                160
                            )// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                            .withAspectRatio(3, 2)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                            .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                            .isGif(false)// 是否显示gif图片
                            .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                            .circleDimmedLayer(false)// 是否圆形裁剪
                            .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                            .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                            .openClickSound(false)// 是否开启点击声音
                            //.selectionMedia(selectList)// 是否传入已选图片
                            //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                            //.videoMaxSecond(15)
                            //.videoMinSecond(10)
                            //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                            //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                            .minimumCompressSize(100)// 小于100kb的图片不压缩
                            //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                            //.rotateEnabled(true) // 裁剪是否可旋转图片
                            //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                            //.videoQuality()// 视频录制质量 0 or 1
                            .recordVideoSecond(180)//显示多少秒以内的视频or音频也可适用
                            .recordVideoSecond(180)//录制视频秒数 默认60s
                            .forResult(PictureConfig.CHOOSE_REQUEST);
                    } else {
                        //"拒绝了权限申请".logw()
                    }
                }
        }

        mDatabind.backLayout.setOnClickListener {
            //点击上传背面
            isBack = true
            PermissionX.init(requireActivity())
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason { scope, deniedList ->
                    val message = "上传本地图片需要您同意获取读取权限才能正常使用"
                    scope.showRequestReasonDialog(
                        deniedList,
                        message,
                        "同意",
                        "拒绝"
                    )
                }
                .request { allGranted, _, _ ->
                    if (allGranted) {
                        //用户授权文件读取
                        //"用户授权文件读取".logw()
                        PictureSelector.create(this)
                            .openGallery(PictureMimeType.ofImage())
                            .imageEngine(GlideEngine.createGlideEngine())
                            .theme(com.luck.picture.lib.R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                            .maxSelectNum(1)// 最大图片选择数量
                            .minSelectNum(1)// 最小选择数量
                            .imageSpanCount(4)// 每行显示个数
                            .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                            //.previewImage(false)// 是否可预览图片
                            //.previewVideo(false)
                            .isCamera(true)// 是否显示拍照按钮
                            //.isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                            //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                            //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
                            .enableCrop(true)// 是否裁剪
                            .compress(true)// 是否压缩
                            .synOrAsy(true)//同步true或异步false 压缩 默认同步
                            //.compressSavePath(getPath())//压缩图片保存地址
                            //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                            .glideOverride(
                                160,
                                160
                            )// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                            .withAspectRatio(3, 2)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                            .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                            .isGif(false)// 是否显示gif图片
                            .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                            .circleDimmedLayer(false)// 是否圆形裁剪
                            .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                            .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                            .openClickSound(false)// 是否开启点击声音
                            //.selectionMedia(selectList)// 是否传入已选图片
                            //.isDragFrame(false)// 是否可拖动裁剪框(固定)
                            //.videoMaxSecond(15)
                            //.videoMinSecond(10)
                            //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                            //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                            .minimumCompressSize(100)// 小于100kb的图片不压缩
                            //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                            //.rotateEnabled(true) // 裁剪是否可旋转图片
                            //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                            //.videoQuality()// 视频录制质量 0 or 1
                            .recordVideoSecond(180)//显示多少秒以内的视频or音频也可适用
                            .recordVideoSecond(180)//录制视频秒数 默认60s
                            .forResult(PictureConfig.CHOOSE_REQUEST);
                    } else {
                        //"拒绝了权限申请".logw()
                    }
                }
        }



        mDatabind.nextBtn.setOnClickListener {
            nav().navigate(R.id.action_postIdCardFragment_to_postIdUserInfoFragment)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        "onActivityResult called>>>> $requestCode   $resultCode".logw()
        if (resultCode === RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {              // 结果回调
                    var selectList: MutableList<LocalMedia>? =
                        PictureSelector.obtainMultipleResult(data)
                    if (selectList != null && selectList.isNotEmpty()) {
                        val media: LocalMedia = selectList[0]
                        if (media != null) {
                            val urlpath = if (media.isCut && !media.isCompressed) {
                                // 裁剪过
                                media.cutPath
                            } else if (media.isCompressed || media.isCut && media.isCompressed) {
                                // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                                media.compressPath
                            } else {
                                // 原图
                                media.path
                            }

                            "imageUri  urlpath $urlpath".logw()

                            val file: File = File(urlpath)
                            val imageUri = Uri.fromFile(file)

                            if (isBack){
                                backUrlpath = urlpath
                                mDatabind.backImg.setImageURI(imageUri)
                            }else{
                                frontUrlpath = urlpath
                                mDatabind.frontImg.setImageURI(imageUri)
                            }

                        }
                    } else {

                    }
                }

            }
        }
    }

}