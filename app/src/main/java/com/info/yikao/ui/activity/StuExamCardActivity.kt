package com.info.yikao.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityUserExamCardBinding
import com.info.yikao.ext.*
import com.info.yikao.viewmodel.StudentExamCardViewModel
import com.kingja.loadsir.core.LoadService
import com.permissionx.guolindev.PermissionX
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 考生准考证页面
 */
class StuExamCardActivity : BaseActivity<StudentExamCardViewModel, ActivityUserExamCardBinding>() {

    private var cardNum = ""

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    val qrCodeWriter: QRCodeWriter by lazy {
        QRCodeWriter()
    }


    override fun layoutId(): Int = R.layout.activity_user_exam_card

    override fun initView(savedInstanceState: Bundle?) {

        cardNum = intent.getStringExtra("id") ?: ""

        //状态页配置
        loadsir = loadServiceInit(mDatabind.mainLayout) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getTestCardInfo(cardNum)
        }


        mDatabind.saveStuExamCard.setOnClickListener {
            val viewWidth = mDatabind.shotLayout.width

            val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            val widthSpec = View.MeasureSpec.makeMeasureSpec(viewWidth, View.MeasureSpec.EXACTLY)

            val width = mDatabind.shotLayout.measuredWidth * 2
            val height = mDatabind.shotLayout.measuredHeight * 2

            mDatabind.shotLayout.measure(widthSpec, heightSpec)
            val bitmap = getCacheBitmapFromView(mDatabind.shotLayout, width, height)

            PermissionX.init(this@StuExamCardActivity)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason { scope, deniedList ->
                    val message = "保存图片需要您同意获取读取权限才能正常使用"
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
                        ImageSaveUtil.saveAlbum(
                            this@StuExamCardActivity,
                            bitmap,
                            Bitmap.CompressFormat.JPEG,
                            90,
                            true
                        )
                        Snackbar.make(mDatabind.saveStuExamCard, "准考证已保存到相册", Snackbar.LENGTH_SHORT)
                            .show()
                    } else {
                        //"拒绝了权限申请".logw()
                    }
                }

//            bitmap.saveToAlbum(this,"stu_card")
        }

        mDatabind.stuCardTips.text = "　　1．考生应按规定的时间入场，开始考试后15分钟禁止迟到考生进入考场。\n" +
                "\n" +
                "　　2．考生入场时须主动出示《准考证》以及有效身份证件(身份证、军人、武警人员证件、未成年人的户口本、公安户籍部门开具的《身份证》号码证明、护照或者港、澳、台同胞证件)，接受考试工作人员的核验，并按要求在“考生花名册”上签自己的姓名。\n" +
                "\n" +
                "　　3．考生只准携带必要的文具入场,如铅笔、签字笔、毛笔、水粉水彩颜料等，具体要求见招考简章。禁止携带任何已完成作品以及各种无线通信工具(如寻呼机、移动电话)等物品。如发现考生携带以上禁带物品，考生将作为违纪处理，取消该次考试成绩。考场内不得擅自相互借用文具。\n" +
                "\n" +
                "　　4．考生入场后按号入座，将本人《准考证》以及有效身份证件放在课桌上，以便核验。\n" +
                "\n" +
                "　　5．考生答题前应认真填写试卷及答题纸上的姓名、准考证号等栏目并粘贴带有考生个人信息的条形码。凡不按要求填写或字迹不清、无法辨认的，试卷及答题纸一律无效。责任由考生自付。\n" +
                "\n" +
                "　　6．开考后，考生不得中途退场。如因身体不适要求中途退场，须征得监考人员及考点主考批准，并在退场前将试卷、答题纸如数上交。\n" +
                "\n" +
                "　　7．考生遇试卷分发错误或试题字迹不清等情况应及时要求更换；涉及试题内容的疑问，不得向监考人员询问。"



        loadsir.showLoading()
        mViewModel.getTestCardInfo(cardNum)

    }


    private fun getCacheBitmapFromView(view: View, width: Int, height: Int): Bitmap {

        val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)

        c.scale(2.0f, 2.0f)
        view.draw(c)
        return b
    }

    override fun createObserver() {
        mViewModel.currentStu.observe(this) { result ->
            //获取考试信息
            parseState(result, {
                loadsir.showSuccess()

                val schoolIcon = Constant.imgUrlHead + it.Logo
                Glide.with(this@StuExamCardActivity).load(schoolIcon)
                    .apply(getGlideRequestOptions(Constant.GLIDE_OPTIONS_SCHOOL_ICON))
                    .into(mDatabind.stuCardSchoolIcon)

                mDatabind.stuCardTitle.text = it.SchoolName
                mDatabind.stuCardSubTitle.text = it.Title

                val headPhoto = Constant.imgUrlHead + it.StuImg1
                Glide.with(this@StuExamCardActivity).load(headPhoto)
                    .apply(getGlideRequestOptions(Constant.GLIDE_TYPE_DEFAULT))
                    .into(mDatabind.stuCardPhoto)

                //生成二维码
               val qrImg = createQRImage(this,it.QrUrl)

                Glide.with(this@StuExamCardActivity).load(qrImg)
                    .apply(getGlideRequestOptions(Constant.GLIDE_TYPE_DEFAULT))
                    .into(mDatabind.stuCardQrImg)

                mDatabind.stuCardNameContent.text = it.RealName
                mDatabind.stuCardSexContent.text = it.Sex
                mDatabind.stuCardNumContent.text = it.TestCardNo
                mDatabind.stuCardClassNumContent.text = it.TestClass
                mDatabind.stuCardLocationContent.text = it.TestClassAddr
                mDatabind.stuCardMajorContent.text = it.SubjectsNameParent
                mDatabind.stuCardGradeContent.text = it.SubjectsName
                mDatabind.stuCardTimeContent.text = it.TestTimeStart
                mDatabind.stuCardShowContent.text = it.TestContent

            }, {
                loadsir.showError()
                Snackbar.make(mDatabind.saveStuExamCard, it.errorMsg, Snackbar.LENGTH_SHORT).show()
            })
        }
    }


    fun createQRImage(context: Context, data: String): Bitmap? {
        try {
            if (data == null || "" == data) {
                return null
            }
            var widthPix = (context as Activity).windowManager.defaultDisplay
                .width
            widthPix = widthPix / 5 * 3
            val heightPix = widthPix

            //配置参数
            val hints: MutableMap<EncodeHintType, Any?> = HashMap()
            hints[EncodeHintType.CHARACTER_SET] = "utf-8"
            //容错级别 L<M<Q<H 7%-30%
            hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.Q
            //设置空白边距的宽度
            hints[EncodeHintType.MARGIN] = 1 //default is 4


            // 图像数据转换，使用了矩阵转换
            val bitMatrix =
                QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, widthPix, heightPix, hints)
            val pixels = IntArray(widthPix * heightPix)
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (y in 0 until heightPix) {
                for (x in 0 until widthPix) {
                    if (bitMatrix[x, y]) {
                        pixels[y * widthPix + x] = -0x1000000
                    } else {
                        pixels[y * widthPix + x] = -0x1
                    }
                }
            }

            // 生成二维码图片的格式，使用ARGB_8888
            var bitmap = Bitmap.createBitmap(widthPix, heightPix, Bitmap.Config.ARGB_8888)
            bitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix)

            return bitmap
            //必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            //return bitmap != null && bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}