package com.info.yikao.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentInputUserInfoBinding
import com.info.yikao.ext.Constant
import com.info.yikao.ext.canShow
import com.info.yikao.ext.getFileMD5
import com.info.yikao.ext.isIDNumber
import com.info.yikao.model.CityData
import com.info.yikao.util.GlideEngine
import com.info.yikao.viewmodel.PostIdInfoViewModel
import com.lljjcoder.Interface.OnCustomCityPickerItemClickListener
import com.lljjcoder.bean.CustomCityData
import com.lljjcoder.citywheel.CustomConfig
import com.lljjcoder.style.citycustome.CustomCityPicker
import com.lljjcoder.utils.utils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.permissionx.guolindev.PermissionX
import com.qiniu.android.common.FixedZone
import com.qiniu.android.storage.Configuration
import com.qiniu.android.storage.UploadManager
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.loge
import me.hgj.jetpackmvvm.ext.util.logw
import java.io.File

class PostIdUserInfoFragment : BaseFragment<PostIdInfoViewModel, FragmentInputUserInfoBinding>() {

    private val mCityPicker: CustomCityPicker by lazy { CustomCityPicker(requireContext()) }

    private val cityConfig: CustomConfig = CustomConfig.Builder()
        .title("选择城市")
        .visibleItemsCount(5)
        .provinceCyclic(false)
        .province("四川省")
        .city("成都市")
        .district("锦江区")
        .cityCyclic(false)
        .setCustomItemLayout(R.layout.item_custom_city)//自定义item的布局
        .setCustomItemTextViewId(R.id.item_custome_city_name_tv)
        .districtCyclic(false)
//            .drawShadows(isShowBg)
        .setCityWheelType(CustomConfig.WheelType.PRO_CITY_DIS)
        .build()

    private var cityList = ArrayList<CityData>()


    private var uploadManager: UploadManager? = null

    private var frontUrlpath: String? = null
    private var frontName = ""

    companion object {
        fun newInstance(fromUser: Boolean): PostIdUserInfoFragment {
            val args = Bundle()
            args.putBoolean("from_user", fromUser)
            val fragment = PostIdUserInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var fromUser = false

    private var showDetailAddr = false


    override fun layoutId(): Int = R.layout.fragment_input_user_info

    override fun initView(savedInstanceState: Bundle?) {
        //初始化bundle数据
        arguments?.let {
            fromUser = it.getBoolean("from_user")
        }

        if (fromUser) {
            mDatabind.nextBtn.text = "完成"
            mDatabind.titleBackBtn.setOnClickListener {
                activity?.finish()
            }
        } else {
            mDatabind.nextBtn.text = "下一步"
            mDatabind.titleBackBtn.setOnClickListener {
                nav().popBackStack()
            }
        }

        //初始化省市区选择器
        initCityData()
        cityConfig.cityDataList = cityList as List<CustomCityData>?
        mCityPicker.setCustomConfig(cityConfig)
//        mCityPicker.init(requireContext())

        mViewModel.getUploadToken()

        mCityPicker.setOnCustomCityPickerItemClickListener(object :
            OnCustomCityPickerItemClickListener() {
            override fun onSelected(
                province: CustomCityData?,
                city: CustomCityData?,
                district: CustomCityData?
            ) {
                //选择了
                val pName = province?.name
                val cName = city?.name
                val dName = district?.name

                if (showDetailAddr) {
                    //通信地址详细
                    mDatabind.detailLocationTv.text = pName + cName + dName

                    mViewModel.inputUserInfo.PostProvinceId = province?.id?.toInt() ?: -1
                    mViewModel.inputUserInfo.PostProvince = province?.name ?: ""
                    mViewModel.inputUserInfo.PostCityId = city?.id?.toInt() ?: -1
                    mViewModel.inputUserInfo.PostCity = city?.name ?: ""
                    mViewModel.inputUserInfo.PostAreaId = district?.id?.toInt() ?: -1
                    mViewModel.inputUserInfo.PostArea = district?.name ?: ""
                } else {
                    //所在省份
                    mDatabind.proviceTv.text = pName
                    mViewModel.inputUserInfo.Province = pName ?: ""
                }
            }
        })

        mDatabind.proviceLayout.setOnClickListener {
            //显示所在省份
            cityConfig.mWheelType = CustomConfig.WheelType.PRO
            mCityPicker.setCustomConfig(cityConfig)
            showDetailAddr = false
            mCityPicker.showCityPicker()
        }

        mDatabind.locationLayout.setOnClickListener {
            //显示收件信息的通信地址
            cityConfig.mWheelType = CustomConfig.WheelType.PRO_CITY_DIS
            mCityPicker.setCustomConfig(cityConfig)
            showDetailAddr = true
            mCityPicker.showCityPicker()
        }

        mDatabind.nextBtn.setOnClickListener {
            //判断是不是需要添加判空逻辑
            //增加判断身份证合法正则
            val idNum = mDatabind.inputIdCard.text.toString()
            if (idNum.canShow()) {
                //如果填写了，则需要判断合法性
                if (!isIDNumber(idNum)) {
                    //没有合法
                    Snackbar.make(mDatabind.nextBtn, "请输入合法的身份证号码", Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            mViewModel.inputUserInfo.apply {
                RealName = mDatabind.realNameEt.text.toString()
                IDNumber = mDatabind.inputIdCard.text.toString()
                try {
                    StuHeight = mDatabind.inputHeight.text.toString()?.toFloat()
                    StuWeight = mDatabind.inputWeight.text.toString()?.toFloat()
                } catch (e: Exception) {

                }
                PostDetail = mDatabind.inputStreet.text.toString()
                PostName = mDatabind.inputEmsName.text.toString()
                PostTel = mDatabind.inputEmsPhone.text.toString()
                EmergencyContact = mDatabind.inputRelativeName.text.toString()
                EmergencyContactTel = mDatabind.inputRelativePhone.text.toString()
            }
            //处理完了后上传
            mViewModel.postMemberInfo()
        }

        mDatabind.headPhotoLayout.setOnClickListener {
            //点击上传1寸图片
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
                            .withAspectRatio(5, 7)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
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

        mViewModel.getUserMemberInfo()

    }

    private fun initCityData() {
        var cityJson = utils.getJson(context, "sysRegion.json")
        var type = object : TypeToken<ArrayList<CityData>>() {}.type

        cityList = Gson().fromJson(cityJson, type)
    }

    override fun createObserver() {
        mViewModel.postResult.observe(this) { result ->
            parseState(result, {
                appViewModel.memberData = mViewModel.inputUserInfo
                appViewModel.memberInfo.value = mViewModel.inputUserInfo
//                if (fromUser) {
                activity?.finish()
//                }
            }, {
                Snackbar.make(mDatabind.nextBtn, it.errorMsg, Snackbar.LENGTH_SHORT).show()
            })
        }

        mViewModel.memberInfo.observe(this) { result ->
            parseState(result, {
                "member info is $it".logw()
                //获取到了用户信息
                mViewModel.inputUserInfo = it
                //显示界面
                mDatabind.proviceTv.showContent(it.Province)
                mDatabind.inputIdCard.showContent(it.IDNumber)
                mDatabind.realNameEt.showContent(it.RealName)
                //一寸照片
                if (it.StuImg1.canShow()){
                    mDatabind.userSmallHeadTv.showContent("已上传")
                }else{
                    mDatabind.userSmallHeadTv.showContent("")
                }
                mDatabind.inputHeight.showContent(it.StuHeight.toString())
                mDatabind.inputWeight.showContent(it.StuWeight.toString())
                mDatabind.detailLocationTv.showContent(it.PostProvince + it.PostCity + it.PostArea)
                mDatabind.inputStreet.showContent(it.PostDetail)
                mDatabind.inputEmsName.showContent(it.PostName)
                mDatabind.inputEmsPhone.showContent(it.PostTel)
                mDatabind.inputRelativeName.showContent(it.EmergencyContact)
                mDatabind.inputRelativePhone.showContent(it.EmergencyContactTel)


            }, {
                Snackbar.make(mDatabind.nextBtn, it.errorMsg, Snackbar.LENGTH_SHORT).show()
            })

        }
    }


    private fun TextView.showContent(content: String?) {
        if (content != null && content != "") {
            text = content
        }
    }

    private fun EditText.showContent(content: String?) {
        if (content != null && content != "") {
            setText(content)
        }
    }

    //FixedZone.zone0   华东机房
    //FixedZone.zone1   华北机房
    //FixedZone.zone2   华南机房
    //FixedZone.zoneNa0 北美机房
    private fun upload(key: String) {
        val startTime = System.currentTimeMillis()

//        //可以自定义zone
//        val zone = FixedZone(arrayOf("domain1","domain2"))
//        //手动指定上传区域
//        val zone = FixedZone.zone0 //华东
        //config配置上传参数
        val config = Configuration.Builder()
            .connectTimeout(90) // 链接超时。默认90秒
            .useHttps(true) // 是否使用https上传域名
            .useConcurrentResumeUpload(true) // 使用并发上传，使用并发上传时，除最后一块大小不定外，其余每个块大小固定为4M，
            .resumeUploadVersion(Configuration.RESUME_UPLOAD_VERSION_V2) // 使用新版分片上传
            .concurrentTaskCount(3) // 并发上传线程数量为3
            .responseTimeout(90) // 服务器响应超时。默认90秒
//            .recorder(recorder) // recorder分片上传时，已上传片记录器。默认null
//            .recorder(recorder, keyGen) // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
            .zone(FixedZone.zone2) // 设置区域，不指定会自动选择。指定不同区域的上传域名、备用域名、备用IP。
            .build()

        if (uploadManager == null) {
            uploadManager = UploadManager(config)
        }

        uploadManager!!.put(
            frontUrlpath, key, mViewModel.uploadToken,
            { key, info, res ->
                //res 包含 hash、key 等信息，具体字段取决于上传策略的设置
                if (info.isOK()) {

                    "$key \r\n $info \r\n $res".logw()

//                    val frontUrl = "http://rvin5iszh.hn-bkt.clouddn.com/Student/$frontName"
//                    val frontUrl = "${Constant.imgUrlHead}/Student/$frontName"
                    val frontUrl = "/Student/$frontName"

                    mViewModel.inputUserInfo.StuImg1 = frontUrl
                    mDatabind.userSmallHeadTv.showContent("已上传")
                } else {
                    //如果失败，这里可以把 info 信息上报自己的服务器，便于后面分析上传错误原因
                    Snackbar.make(mDatabind.nextBtn, "图片上传失败，请重试", Snackbar.LENGTH_SHORT).show()
                }
                "$key \r\n $info \r\n $res".logw()
            }, null
        )

//        uploadManager!!.put(
//            path,
//            key,
//            token,
//            { key, info, res ->
//                //res 包含 hash、key 等信息，具体字段取决于上传策略的设置
//                if (info.isOK()) {
//                } else {
//                    //如果失败，这里可以把 info 信息上报自己的服务器，便于后面分析上传错误原因
//                }
//                "$key \r\n $info \r\n $res".logw()
//            },
//            UploadOptions(null, null, false, object : UpProgressHandler {
//                override fun progress(key: String?, percent: Double) {
//                    "上传进度 $key---$percent".logw()
//                }
//            }) {
//                //取消上传，通过返回参数，需要一个全局变量控制
//                false
//            }
//        )

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        "onActivityResult called>>>> $requestCode   $resultCode".logw()
        if (resultCode === Activity.RESULT_OK) {
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


                            frontUrlpath = urlpath
                            frontName = getFileMD5(file) + System.currentTimeMillis() + ".jpeg"

                            upload("Student/$frontName")


                        }
                    } else {

                    }
                }

            }
        }
    }

}