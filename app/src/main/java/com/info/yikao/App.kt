package com.info.yikao

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Process
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.multidex.MultiDex
import com.info.yikao.weight.EmptyCallback
import com.info.yikao.weight.ErrorCallback
import com.info.yikao.weight.LoadingCallback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.tencent.mmkv.MMKV
import me.hgj.jetpackmvvm.base.BaseApp
import com.info.yikao.ext.getProcessName

class App : BaseApp() {

    private var isMainProcess = false
    private var sCurProcessName: String? = null

    companion object {
        lateinit var instance: App
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(this)
        MMKV.initialize(this.filesDir.absolutePath + "/mmkv")
        //界面加载管理 初始化
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())//加载
            .addCallback(ErrorCallback())//错误
            .addCallback(EmptyCallback())//空
            .setDefaultCallback(SuccessCallback::class.java)//设置默认加载状态页
            .commit()
        //初始化bugly
        val context = applicationContext
        //获取当前包名
        val packageName = context.packageName
        //获取当前进程名
        val processName = getProcessName(Process.myPid())

        if (BuildConfig.DEBUG) {
        }

        if (isMainProcess) {
            val kv = MMKV.mmkvWithID("app")

        }

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
        isMainProcess = packageName.isNotEmpty() && packageName == getCurProcessName(this)
    }


    fun getCurProcessName(context: Context): String? {
        if (!TextUtils.isEmpty(sCurProcessName)) {
            return sCurProcessName
        }
        sCurProcessName =
            getProcessName(
                Process.myPid()
            )
        if (!TextUtils.isEmpty(sCurProcessName)) {
            return sCurProcessName
        }
        try {
            val pid = Process.myPid()
            sCurProcessName =
                getProcessName(pid)
            if (!TextUtils.isEmpty(sCurProcessName)) {
                return sCurProcessName
            }
            //获取系统的ActivityManager服务
            val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
                ?: return sCurProcessName
            for (appProcess in am.runningAppProcesses) {
                if (appProcess.pid == pid) {
                    sCurProcessName = appProcess.processName
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return sCurProcessName
    }

}