package com.info.yikao.viewmodel

import androidx.lifecycle.MutableLiveData
import com.info.yikao.model.*
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

class HomeViewModel : BaseViewModel() {


    //轮播的数据
    var bannerData: MutableLiveData<ResultState<ArrayList<BannerArticle>>> = MutableLiveData()


    val listItems = arrayListOf<MainListWarpper>(
        MainListWarpper(0, "艺考头条", null, null, null),
        MainListWarpper(1, "", NewsBean("", "头条1", "来源1", "20W", 1), null, null),
        MainListWarpper(1, "", NewsBean("", "头条1", "来源2", "10W", 2), null, null),
        MainListWarpper(0, "艺考报名", null, null, null),
        MainListWarpper(2, "", null, SchoolBean("", "", "", ""), null),
        MainListWarpper(2, "", null, SchoolBean("", "", "", ""), null),
        MainListWarpper(0, "展演资讯", null, null, null),
        MainListWarpper(3, "", null, null, StreetShowBean("", "", "", "")),
        MainListWarpper(3, "", null, null, StreetShowBean("", "", "", "")),
    )

    fun getBannerNews() {
        request({ apiService.getBanner() }, bannerData)
    }

}