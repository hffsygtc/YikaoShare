package com.info.yikao.ui.activity

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivitySignPayBinding
import com.info.yikao.databinding.ActivityUserExamBinding
import com.info.yikao.databinding.ActivityUserExamCardBinding
import com.info.yikao.ext.Constant
import com.info.yikao.ext.ImageSaveUtil
import com.info.yikao.ext.getGlideRequestOptions
import com.info.yikao.ext.saveToAlbum
import com.info.yikao.util.GlideEngine
import com.info.yikao.viewmodel.SignUpPayViewModel
import com.info.yikao.viewmodel.StudentExamCardViewModel
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.permissionx.guolindev.PermissionX

/**
 * 考生准考证页面
 */
class StuExamCardActivity : BaseActivity<StudentExamCardViewModel, ActivityUserExamCardBinding>() {

    override fun layoutId(): Int = R.layout.activity_user_exam_card

    override fun initView(savedInstanceState: Bundle?) {

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
                        ImageSaveUtil.saveAlbum(this@StuExamCardActivity,bitmap,Bitmap.CompressFormat.JPEG,90,true)
                        Snackbar.make(mDatabind.saveStuExamCard,"准考证已保存到相册",Snackbar.LENGTH_SHORT).show()
                    } else {
                        //"拒绝了权限申请".logw()
                    }
                }


//            bitmap.saveToAlbum(this,"stu_card")
        }

        val schoolIcon = Constant.imgUrlHead+""
        Glide.with(this@StuExamCardActivity).load(schoolIcon)
            .apply(getGlideRequestOptions(Constant.GLIDE_OPTIONS_NEWS))
            .into(mDatabind.stuCardSchoolIcon)

        mDatabind.stuCardTitle.text = "四川音乐学院社会艺术培训与考级中心"
        mDatabind.stuCardSubTitle.text = "四川音乐学院社会艺术培训与考级中心"


        val headPhoto = Constant.imgUrlHead+""
        Glide.with(this@StuExamCardActivity).load(headPhoto)
            .apply(getGlideRequestOptions(Constant.GLIDE_TYPE_DEFAULT))
            .into(mDatabind.stuCardPhoto)

        val qrImg = Constant.imgUrlHead+""
        Glide.with(this@StuExamCardActivity).load(qrImg)
            .apply(getGlideRequestOptions(Constant.GLIDE_TYPE_DEFAULT))
            .into(mDatabind.stuCardQrImg)

        mDatabind.stuCardNameContent.text = "赵本山"
        mDatabind.stuCardSexContent.text = "男"
        mDatabind.stuCardNumContent.text = "15456451231231"
        mDatabind.stuCardClassNumContent.text ="12"
        mDatabind.stuCardLocationContent.text = "第三教学楼"
        mDatabind.stuCardMajorContent.text ="音乐10级"
        mDatabind.stuCardGradeContent.text = "10级"
        mDatabind.stuCardTimeContent.text="14:00:12"
        mDatabind.stuCardShowContent.text="《节目》"
        mDatabind.stuCardTips.text = "1．考生应按规定的时间入场，开始考试后15分钟禁止迟到考生进入考场。\n" +
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


    }


    private fun getCacheBitmapFromView(view: View, width: Int, height: Int): Bitmap {

        val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)

        c.scale(2.0f, 2.0f)
        view.draw(c)
        return b
    }


}