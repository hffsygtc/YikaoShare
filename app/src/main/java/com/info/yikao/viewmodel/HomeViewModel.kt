package com.info.yikao.viewmodel

import com.info.yikao.model.*
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class HomeViewModel : BaseViewModel() {

    val bannerItems = arrayListOf<BannerArticle>(
        BannerArticle(""),
        BannerArticle(""),
        BannerArticle("")
    )

    val listItems = arrayListOf<MainListWarpper>(
        MainListWarpper(0,"艺考头条",null,null,null),
        MainListWarpper(1,"", NewsBean("","","",""),null,null),
        MainListWarpper(1,"",NewsBean("","","",""),null,null),
        MainListWarpper(0,"艺考报名",null,null,null),
        MainListWarpper(2,"",null, SchoolBean("","","",""),null),
        MainListWarpper(2,"",null,SchoolBean("","","",""),null),
        MainListWarpper(0,"展演资讯",null,null,null),
        MainListWarpper(3,"",null,null, StreetShowBean("","","","")),
        MainListWarpper(3,"",null,null,StreetShowBean("","","","")),
    )

}