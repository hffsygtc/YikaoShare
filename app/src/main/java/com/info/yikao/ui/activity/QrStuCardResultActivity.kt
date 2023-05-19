package com.info.yikao.ui.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityQrStuCardResultBinding
import com.info.yikao.databinding.ActivityTopNewsDetailBinding
import com.info.yikao.ext.*
import com.info.yikao.ui.adapter.QrCardStateAdapter
import com.info.yikao.viewmodel.FastNewsDetailViewModel
import com.info.yikao.viewmodel.QrStuCardViewModel
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 公告快讯的详情页
 */
class QrStuCardResultActivity :
    BaseActivity<QrStuCardViewModel, ActivityQrStuCardResultBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private var stuId = -1

    private val mAdapter : QrCardStateAdapter by lazy {
        QrCardStateAdapter(arrayListOf())
    }

    override fun layoutId(): Int = R.layout.activity_qr_stu_card_result

    override fun initView(savedInstanceState: Bundle?) {

        stuId = intent.getIntExtra("card_id", -1)

        mDatabind.titleBackBtn.setOnClickListener {
            finish()
        }

        //状态页配置
        loadsir = loadServiceInit(mDatabind.mainLayout) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getQrResult(stuId)
        }

        loadsir.showLoading()
        mViewModel.getQrResult(stuId)

        val photoSrc = Constant.imgUrlHead+""
        Glide.with(this).load(photoSrc)
            .apply(getGlideRequestOptions(Constant.GLIDE_OPTIONS_BANNER))
            .into( mDatabind.stuHeadImg)

        mDatabind.stuNameTvContent.text ="赵本山"
        mDatabind.stuSexTvContent.text = "女"
        mDatabind.stuAgeTvContent.text = "16"
        mDatabind.stuExamTypeTvContent.text = "本部艺术楼202教室"
        mDatabind.stuExamSortTvContent.text = "05月15日 10点场"
        mDatabind.stuExamMajorTvContent.text = "乐曲类>流行类>钢琴1级"

        mDatabind.userStateRv.init(LinearLayoutManager(this),mAdapter)

        mAdapter.setList(mViewModel.testList)


    }

    override fun createObserver() {

    }


}