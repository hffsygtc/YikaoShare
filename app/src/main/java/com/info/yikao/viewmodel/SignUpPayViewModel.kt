package com.info.yikao.viewmodel

import com.info.yikao.model.PayOrderInfo
import com.info.yikao.model.UserDetailInfo
import com.info.yikao.network.apiService
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * 报名页面
 */
class SignUpPayViewModel : BaseViewModel() {

    var wxSignInfo: UnPeekLiveData<PayOrderInfo> = UnPeekLiveData()

    var aliSignInfo: UnPeekLiveData<PayOrderInfo> = UnPeekLiveData()

    var syncResult: UnPeekLiveData<Boolean> = UnPeekLiveData()

    /**
     * 获取报名请求支付的订单
     */
    fun getPayInfo(subjectId: Int, wechat: Boolean) {
        //获取对应的支付签名订单
        val payType = if (wechat) "wechat" else "alipay"
        request({ apiService.createOrder(subjectId, payType) }, {
            if (wechat) {
                wxSignInfo.value = it
            } else {
                aliSignInfo.value = it
            }
        }, {

        })
    }


    /**
     * 回调支付结果
     */
    fun postPayResult(orderNum: String) {
        request({ apiService.payOrderResult(orderNum) }, {
            syncResult.value = true
        }, { syncResult.value = false })
    }

    var memberInfo = UnPeekLiveData<ResultState<UserDetailInfo>>()

    fun getUserMemberInfo() {
        request({
            apiService.getMemberInfo()
        }, memberInfo)

    }


}