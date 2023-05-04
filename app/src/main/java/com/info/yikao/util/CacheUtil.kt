package com.info.yikao.util

import android.text.TextUtils
import com.google.gson.Gson
import com.info.yikao.model.UserInfo
import com.tencent.mmkv.MMKV

object CacheUtil {

    /**
     * 获取保存的登录信息
     */
    fun getUser(): UserInfo? {
        val kv = MMKV.mmkvWithID("app")
        val userStr = kv.decodeString("user")
        return if (TextUtils.isEmpty(userStr)) {
            null
        } else {
            Gson().fromJson(userStr, UserInfo::class.java)
        }
    }

    /**
     * 设置账户信息
     */
    fun setUser(userResponse: UserInfo?) {
        val kv = MMKV.mmkvWithID("app")
        if (userResponse == null) {
            kv.encode("user", "")
        } else {
            kv.encode("user", Gson().toJson(userResponse))
        }
    }
}