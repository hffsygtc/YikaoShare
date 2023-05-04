package com.info.yikao.ext

import android.text.TextUtils
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException


/**
 * 获取进程号对应的进程名
 *
 * @param pid 进程号
 * @return 进程名
 */
fun getProcessName(pid: Int): String? {
    var reader: BufferedReader? = null
    try {
        reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
        var processName = reader.readLine()
        if (!TextUtils.isEmpty(processName)) {
            processName = processName.trim { it <= ' ' }
        }
        return processName
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
    } finally {
        try {
            reader?.close()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }

    }
    return null
}



fun List<*>?.isNull(): Boolean {
    return this?.isEmpty() ?: true
}

fun List<*>?.isNotNull(): Boolean {
    return this != null && this.isNotEmpty()
}

/**
 * 根据索引获取集合的child值
 * @receiver List<T>?
 * @param position Int
 * @return T?
 */
inline fun <reified T> List<T>?.getChild(position: Int): T? {
    //如果List为null 返回null
    return if (this == null) {
        null
    } else {
        //如果position大于集合的size 返回null
        if (position + 1 > this.size) {
            null
        } else {
            //返回正常数据
            this[position]
        }
    }
}

