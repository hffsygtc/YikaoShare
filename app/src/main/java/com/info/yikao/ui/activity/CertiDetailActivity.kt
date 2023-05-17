package com.info.yikao.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivityCertiDetailBinding
import com.info.yikao.databinding.ActivityExamResultBinding
import com.info.yikao.ext.*
import com.info.yikao.model.CityData
import com.info.yikao.model.TeacherResultBean
import com.info.yikao.ui.adapter.ExamTeacherResultAdapter
import com.info.yikao.viewmodel.CertiDetailViewModel
import com.info.yikao.viewmodel.ExamResultViewModel
import com.kingja.loadsir.core.LoadService
import com.lljjcoder.Interface.OnCustomCityPickerItemClickListener
import com.lljjcoder.bean.CustomCityData
import com.lljjcoder.citywheel.CustomConfig
import com.lljjcoder.style.citycustome.CustomCityPicker
import com.lljjcoder.utils.utils
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.parseState
import java.util.regex.Matcher
import java.util.regex.Pattern

class CertiDetailActivity : BaseActivity<CertiDetailViewModel, ActivityCertiDetailBinding>() {
    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>


    private val mCityPicker: CustomCityPicker by lazy { CustomCityPicker(this) }

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

    private val mAdapter by lazy { ExamTeacherResultAdapter(arrayListOf()) }

    private var examNum = ""
    private var online = false
    private var examName = ""

    private var certNum = ""

    private var imgSrc = ""

    override fun layoutId(): Int = R.layout.activity_certi_detail

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.titleBackBtn.setOnClickListener {
            finish()
        }

        intent?.let {
            examNum = it.getStringExtra("id") ?: ""
            examName = it.getStringExtra("name") ?: ""
        }

        mDatabind.titleTv.text = examName + "证书"
        //状态页配置
        loadsir = loadServiceInit(mDatabind.mainLayout) {
            //点击重试时触发操作
            loadsir.showLoading()
            mViewModel.getDetail(examNum)
        }


        mDatabind.nextBtn.setOnClickListener {
            //确认付邮费

        }

        mDatabind.locationLayout.setOnClickListener {
            //显示收件信息的通信地址
            cityConfig.mWheelType = CustomConfig.WheelType.PRO_CITY_DIS
            mCityPicker.setCustomConfig(cityConfig)
            mCityPicker.showCityPicker()
        }

        //初始化省市区选择器
        initCityData()
        cityConfig.cityDataList = cityList as List<CustomCityData>?
        mCityPicker.setCustomConfig(cityConfig)
//        mCityPicker.init(requireContext())


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

                //通信地址详细
                mDatabind.detailLocationTv.text = pName + cName + dName
            }
        })


        loadsir.showLoading()
        mViewModel.getDetail(examNum)

    }

    private fun initCityData() {
        var cityJson = utils.getJson(this, "sysRegion.json")
        var type = object : TypeToken<ArrayList<CityData>>() {}.type

        cityList = Gson().fromJson(cityJson, type)
    }


    override fun createObserver() {
        mViewModel.certiDetail.observe(this) { result ->
            parseState(result, {
                loadsir.showSuccess()

                val topStr =
                    " 恭喜你通过了：" + " <font color='#ed2a36'>${it.SubjectsStr}</font>" + "，特颁发此证书以资鼓励。"
                mDatabind.topInfoTv.text = Html.fromHtml(topStr)

                imgSrc = Constant.imgUrlHead + it.CertificateImgUrl

                Glide.with(appContext).load(imgSrc)
                    .apply(getGlideRequestOptions(Constant.GLIDE_OPTIONS_BANNER))
                    .into(mDatabind.certImg)

                mDatabind.detailLocationTv.showContent(it.PostProvince + it.PostCity + it.PostArea)
                mDatabind.inputStreet.showContent(it.PostDetail)
                mDatabind.inputEmsName.showContent(it.PostName)
                mDatabind.inputEmsPhone.showContent(it.PostTel)


            }, {
                loadsir.showError()
                Snackbar.make(mDatabind.nextBtn, it.errorMsg, Snackbar.LENGTH_SHORT).show()
            })

        }
    }

    private fun EditText.showContent(content: String?) {
        if (content != null && content != "") {
            setText(content)
        }
    }

    private fun TextView.showContent(content: String?) {
        if (content != null && content != "") {
            text = content
        }
    }

}