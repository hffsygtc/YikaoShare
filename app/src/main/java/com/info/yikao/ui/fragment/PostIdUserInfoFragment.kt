package com.info.yikao.ui.fragment

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.info.yikao.R
import com.info.yikao.base.BaseFragment
import com.info.yikao.databinding.FragmentInputUserInfoBinding
import com.info.yikao.model.CityData
import com.info.yikao.viewmodel.PostIdInfoViewModel
import com.lljjcoder.Interface.OnCustomCityPickerItemClickListener
import com.lljjcoder.bean.CustomCityData
import com.lljjcoder.citywheel.CustomConfig
import com.lljjcoder.style.citycustome.CustomCityPicker
import com.lljjcoder.utils.utils
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.logw

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
                if (fromUser) {
                    activity?.finish()
                }
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
                mDatabind.userSmallHeadTv.showContent(it.StuImg1)
                mDatabind.inputHeight.showContent(it.StuHeight.toString())
                mDatabind.inputWeight.showContent(it.StuWeight.toString())
                mDatabind.detailLocationTv.showContent(it.PostProvince+it.PostCity+it.PostArea)
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


}