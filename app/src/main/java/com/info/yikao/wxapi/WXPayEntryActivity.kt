package com.info.yikao.wxapi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.info.yikao.base.EventViewModel
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import me.hgj.jetpackmvvm.base.BaseApp

class WXPayEntryActivity : Activity(), IWXAPIEventHandler {

    //application全局的viewmodel，用于发送全局通知操作
    val eventViewModel: EventViewModel by lazy {
        (this.application as BaseApp).getAppViewModelProvider().get(EventViewModel::class.java)
    }

    //与微信通信的api
    private val wxApi: IWXAPI by lazy {
        WXAPIFactory.createWXAPI(this@WXPayEntryActivity, "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //无界面的activity
        wxApi.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        wxApi.handleIntent(intent, this)
    }

    override fun onReq(p0: BaseReq?) {
    }

    /**
     * 处理回调
     */
    override fun onResp(resp: BaseResp?) {
        if (resp?.type == ConstantsAPI.COMMAND_PAY_BY_WX) {
            when (resp.errCode) {
//                0 -> {
//                    //支付成功
//                    eventViewModel.payResultEvent.value = 1
//                    "wx 支付成功".logw()
//                }
//                -1 -> {
//                    eventViewModel.payResultEvent.value = 2
//                    "wx 支付错误 ".logw()
//                }
//                -2 -> {
//                    eventViewModel.payResultEvent.value = 2
//                    "wx 支付取消".logw()
//                }
            }
            finish()
        }
    }
}