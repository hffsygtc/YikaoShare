package com.info.yikao.ui.activity

import android.net.Uri
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityUploadVideoBinding
import com.info.yikao.ext.canShow
import com.info.yikao.viewmodel.UploadVideoViewModel
import com.qiniu.android.common.FixedZone
import com.qiniu.android.storage.Configuration
import com.qiniu.android.storage.UpProgressHandler
import com.qiniu.android.storage.UploadManager
import com.qiniu.android.storage.UploadOptions
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.loge
import me.hgj.jetpackmvvm.ext.util.logw
import java.io.File

class UploadVideoActivity : BaseActivity<UploadVideoViewModel, ActivityUploadVideoBinding>() {

    private var path: Uri? = null
    private var videoFile: File? = null

    private var videoName = ""
    private var uploadToken = ""
    private var orderNum = ""

    private var uploadManager: UploadManager? = null

    override fun layoutId(): Int = R.layout.activity_upload_video

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.titleTv.text = "考试"
        path = Uri.parse(intent.getStringExtra("path") ?: "")
        orderNum = intent.getStringExtra("orderNum") ?: ""
        "UPLOAD VIDEO PATH IS $path".loge()

//        videoFile = File(path)

        mViewModel.getUploadToken()
        mViewModel.getUserMemberInfo()

        mDatabind.nextBtn.setOnClickListener {
            if (videoName.canShow() && uploadToken.canShow()) {
                upload()
            } else {
                Snackbar.make(mDatabind.nextBtn, "获取凭证失败，稍后重试", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    //FixedZone.zone0   华东机房
    //FixedZone.zone1   华北机房
    //FixedZone.zone2   华南机房
    //FixedZone.zoneNa0 北美机房
    private fun upload() {
        val startTime = System.currentTimeMillis()
        val key = "Video/$videoName"

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
            path, contentResolver, key, uploadToken,
            { key, info, res ->
                //res 包含 hash、key 等信息，具体字段取决于上传策略的设置
                if (info.isOK()) {
                    "$key \r\n $info \r\n $res".logw()
                    mViewModel.saveVideo(orderNum, videoName)

                } else {
                    //如果失败，这里可以把 info 信息上报自己的服务器，便于后面分析上传错误原因
                    Snackbar.make(mDatabind.nextBtn, "图片视频失败，请重试", Snackbar.LENGTH_SHORT).show()
                }
                "$key \r\n $info \r\n $res".logw()
            }, UploadOptions(null, null, false, object : UpProgressHandler {
                override fun progress(key: String?, percent: Double) {
                    "上传进度 $key---$percent".logw()
                    mDatabind.progress.setProgress((percent * 100).toInt())
                }
            }) {
                //取消上传，通过返回参数，需要一个全局变量控制
                false
            }
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


    override fun createObserver() {
        mViewModel.memberInfo.observe(this) { result ->
            parseState(result, {
                "member info is $it".logw()
                mDatabind.nameTv.text = "考生姓名：${it.RealName}"
                mDatabind.idCardTv.text = "身份证号：${it.IDNumber}"
                videoName = "${it.RealName}-$orderNum-${System.currentTimeMillis()}.mp4"

            }, {
                Snackbar.make(mDatabind.nextBtn, it.errorMsg, Snackbar.LENGTH_SHORT).show()
            })
        }

        mViewModel.token.observe(this) {
            //获取到token，开始上传
            uploadToken = it

        }
    }

}