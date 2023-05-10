package com.info.yikao.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.info.yikao.model.*
import com.info.yikao.network.apiService
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.network.ExceptionHandle
import me.hgj.jetpackmvvm.state.ResultState

class HomeViewModel : BaseViewModel() {


    //轮播的数据
    var bannerData: MutableLiveData<ResultState<ArrayList<BannerArticle>>> = MutableLiveData()


    //列表的数据
    var listData: MutableLiveData<ArrayList<MainListWarpper>> = MutableLiveData()


    fun getBannerNews() {
        request({ apiService.getBanner() }, bannerData)
    }

    fun getListData() {
        viewModelScope.launch {
            runCatching {
                //获取列表的文章
                val articleAsync = async {
                    apiService.getHomeArticles()
                }
                //获取首页学校
                val schoolAsync = async {
                    apiService.getHomeSchools()
                }
                //获取首页路演
                val showAsync = async {
                    apiService.getHomeShows()
                }

                val articleResult = articleAsync.await()
                val schoolResult = schoolAsync.await()
                val showResult = showAsync.await()


                //开始拼接结果
                val resultList = arrayListOf<MainListWarpper>()

                if (articleResult.isSucces()) {
                    //拼上文章的数据
                    resultList.add(MainListWarpper(0, "艺考头条", null, null, null))
                    for (article in articleResult.Data) {
                        resultList.add(MainListWarpper(1, "", article, null, null))
                    }
                }

                if (schoolResult.isSucces()) {
                    //拼上学校的数据
                    resultList.add(MainListWarpper(0, "艺考报名", null, null, null))
                    for (school in schoolResult.Data) {
                        resultList.add(MainListWarpper(2, "", null, school, null))
                    }
                }

                if (showResult.isSucces()) {
                    //拼上路演的数据
                    resultList.add(MainListWarpper(0, "展演资讯", null, null, null))
                    for (show in showResult.Data) {
                        resultList.add(MainListWarpper(3, "", null, null, show))
                    }
                }

                resultList
            }.onSuccess {
                listData.value = it
            }.onFailure {
                listData.value = arrayListOf()
            }
        }
    }


}