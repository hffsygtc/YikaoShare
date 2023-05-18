package com.info.yikao.ui.activity

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivitySignPayBinding
import com.info.yikao.databinding.ActivityUserExamBinding
import com.info.yikao.databinding.ActivityUserExamCardBinding
import com.info.yikao.ext.saveToAlbum
import com.info.yikao.viewmodel.SignUpPayViewModel
import com.info.yikao.viewmodel.StudentExamCardViewModel

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
            bitmap.saveToAlbum(this,"stu_card")
        }

    }


    fun getCacheBitmapFromView(view: View, width: Int, height: Int): Bitmap {

        val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)

        c.scale(2.0f, 2.0f)
        view.draw(c)
        return b
    }


}