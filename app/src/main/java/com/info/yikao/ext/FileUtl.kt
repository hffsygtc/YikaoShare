package com.info.yikao.ext

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.JPEG
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.math.BigDecimal


fun sizeOfDirectory(directory: File): Long {
    var size = 0L

    try {
        val fileList = directory.listFiles()
        fileList?.let {
            for (i in it.indices) {
                val subFile = it[i]
                size += if (subFile.isDirectory) {
                    sizeOfDirectory(subFile)
                } else {
                    subFile.length()
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return size
}


fun clearFile(directory: File) {
    try {
        val fileList = directory.listFiles()
        fileList?.let {
            for (i in it.indices) {
                val subFile = it[i]
                if (subFile.isDirectory) {
                    clearFile(subFile)
                } else {
                    subFile.delete()
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun getFormatSize(size: Double): String {
    val kiloByte = size / 1024
    if (kiloByte < 1) {
        return size.toString() + "Byte"
    }

    val megaByte = kiloByte / 1024
    if (megaByte < 1) {
        val result1 = BigDecimal(kiloByte.toString())
        return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
            .toPlainString() + "KB"
    }

    val gigaByte = megaByte / 1024
    if (gigaByte < 1) {
        val result2 = BigDecimal(megaByte.toString())
        return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
            .toPlainString() + "MB"
    }

    val teraBytes = gigaByte / 1024
    if (teraBytes < 1) {
        val result3 = BigDecimal(gigaByte.toString())
        return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
            .toPlainString() + "GB"
    }
    val result4 = BigDecimal(teraBytes)
    return (result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
            + "TB")

}

fun getCacheDir(context: Context): String {
    if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
        val cacheDir = context.externalCacheDir
        if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
            return cacheDir.absolutePath
        }
    }
    return context.cacheDir.absolutePath
}