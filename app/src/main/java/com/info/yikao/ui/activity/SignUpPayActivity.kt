package com.info.yikao.ui.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import com.alipay.sdk.app.PayTask
import com.google.android.material.snackbar.Snackbar
import com.info.yikao.R
import com.info.yikao.base.BaseActivity
import com.info.yikao.databinding.ActivitySignPayBinding
import com.info.yikao.ext.Constant
import com.info.yikao.ext.canShow
import com.info.yikao.model.PayOrderInfo
import com.info.yikao.viewmodel.SignUpPayViewModel
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.logw

/**
 * 报名支付页面
 */
class SignUpPayActivity : BaseActivity<SignUpPayViewModel, ActivitySignPayBinding>() {

    private var price = ""
    private var subject = ""
    private var time = ""
    private var id = -1

    private var isWechat = true

    var wxPayResult = -1
    var aliPayResult = -1

    override fun layoutId(): Int = R.layout.activity_sign_pay

    override fun initView(savedInstanceState: Bundle?) {

        id = intent.getIntExtra("id", -1)
        price = intent.getStringExtra("price") ?: ""
        subject = intent.getStringExtra("subject") ?: ""
        time = intent.getStringExtra("time") ?: ""

        mViewModel.getUserMemberInfo()

        mDatabind.majorNameTv.text = "报考专业：$subject"
        mDatabind.majorTimeTv.text = "考试时间：$time"
        mDatabind.nextBtn.text = "￥$price 立即支付"

        mDatabind.wechatBtn.setOnClickListener {
            if (!isWechat) {
                mDatabind.aliRadio.setBackgroundResource(R.mipmap.icon_ok_small_empty)
                mDatabind.wechatRadio.setBackgroundResource(R.mipmap.icon_ok_small_white)
                isWechat = true
            }
        }

        mDatabind.aliBtn.setOnClickListener {
            if (isWechat) {
                mDatabind.wechatRadio.setBackgroundResource(R.mipmap.icon_ok_small_empty)
                mDatabind.aliRadio.setBackgroundResource(R.mipmap.icon_ok_small_white)
                isWechat = false
            }
        }

        mDatabind.nextBtn.setOnClickListener {
            mViewModel.getPayInfo(id, isWechat)
        }


    }

    override fun createObserver() {
        mViewModel.memberInfo.observe(this) { result ->
            parseState(result, {
                "member info is $it".logw()
                //获取到了用户信息
                mDatabind.nameTv.text = "考生姓名：${it.RealName}"
                mDatabind.proviceTv.text = "报考省份：${it.Province}"
                mDatabind.idCardTv.text = "身份证号：${it.IDNumber}"
            }, {
                //如果没有对应的信息，则需要登录
                Snackbar.make(mDatabind.nextBtn, it.errorMsg, Snackbar.LENGTH_SHORT).show()

            })

        }

        mViewModel.wxSignInfo.observe(this) {
//            callWxPay(it)
            //todo 测试支付成功
            mViewModel.postPayResult(it.OrderNum)
        }

        mViewModel.aliSignInfo.observe(this) {
//            callAliPay(it.sign)
            //测试支付失败
            Snackbar.make(mDatabind.nextBtn,"未知错误，请前往订单查看支付状态",Snackbar.LENGTH_SHORT).show()
        }

        mViewModel.syncResult.observe(this){
            Snackbar.make(mDatabind.nextBtn,"支付完成，请前往订单查看支付状态",Snackbar.LENGTH_SHORT).show()
            mDatabind.nextBtn.postDelayed({
                finish()
            },1500)
        }
    }


    private fun callWxPay(info: PayOrderInfo) {
        val wxApi = WXAPIFactory.createWXAPI(this@SignUpPayActivity, null)
        wxApi.registerApp(Constant.WX_APP_ID)
        val payRequest = PayReq()
        payRequest.apply {
            appId = info.appid
            partnerId = info.partnerid
            prepayId = info.prepayid
            packageValue = "Sign=WXPay"
            nonceStr = info.noncestr
            timeStamp = info.timestamp
            sign = info.sign
        }
        wxApi.sendReq(payRequest)
    }


    private fun callAliPay(aliOrderInfo: String) {
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                val aliPay = PayTask(this@SignUpPayActivity)

                "start pay aliOrderInfo $aliOrderInfo".logw()
                val result = aliPay.payV2(aliOrderInfo, true)

                //支付结果
                val payResult = result.getOrDefault("resultStatus", "-1")

                //9000代表支付成功
                if (payResult == "9000") {
                    aliPayResult = 1
                    "ali 支付成功".logw()
                } else {
                    //支付失败
                    aliPayResult = 2
                    "ali 支付错误 ".logw()
                }
            }

            when (aliPayResult) {
                1 -> {
                    aliPayResult = -1
                }
                2 -> {
                    aliPayResult = -1
                }
            }
        }
    }
}