package com.info.yikao.viewmodel

import com.info.yikao.model.QrCardStateBean
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * 扫码考生准考证
 */
class QrStuCardViewModel : BaseViewModel() {

    val testList = arrayListOf<QrCardStateBean>(QrCardStateBean("111","1111",0),
        QrCardStateBean("222","222",0),
        QrCardStateBean("333","333",0),
        QrCardStateBean("444","444",0))

    fun getQrResult(id:Int){


    }


}