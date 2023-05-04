package com.info.yikao.ext

import java.text.SimpleDateFormat
import java.util.*

fun Int.covertNetParam(): String? {
    if (this > 0) {
        return this.toString()
    }
    return null
}

fun Int?.covertNetParam(): String? {
    this?.let {
        if (this > 0) {
            return this.toString()
        }
    }
    return null
}


fun String?.canShow(): Boolean {
    this?.let {
        if (this != "") {
            return true
        }
    }
    return false
}


fun String?.getTailWidth(): Float {
    this?.let {
        if (it.indexOf("nbddata-width=") > 0) {
            //截取字符串中含有的宽度值
            val widthStr = it.substring(it.indexOf("nbddata-width="))
            var widthData: String? = null
            widthData = if (widthStr.indexOf("&") > 0) {
                //如果后面还有其他参数
                widthStr.substring(14, widthStr.indexOf("&"))
            } else {
                widthStr.substring(14)
            }

            widthData?.let {
                return widthData.toFloat()
            }
        }
    }
    return 0f
}


fun String?.getTailHeight(): Float {
    this?.let {
        if (it.indexOf("nbddata-height=") > 0) {
            //截取字符串中含有的宽度值
            val heightStr = it.substring(it.indexOf("nbddata-height="))
            var heightData: String? = null
            heightData = if (heightStr.indexOf("&") > 0) {
                //如果后面还有其他参数
                heightStr.substring(15, heightStr.indexOf("&"))
            } else {
                heightStr.substring(15)
            }

            heightData?.let {
                return heightData.toFloat()
            }
        }
    }
    return 0f
}


fun Long.getWeek(): String {

    val cd = Calendar.getInstance()
    cd.time = Date(this)


    return when (cd[Calendar.DAY_OF_WEEK]) {
        Calendar.SUNDAY -> "  星期日"
        Calendar.MONDAY -> "  星期一"
        Calendar.TUESDAY -> "  星期二"
        Calendar.WEDNESDAY -> "  星期三"
        Calendar.THURSDAY -> "  星期四"
        Calendar.FRIDAY -> "  星期五"
        else -> "  星期六"
    }
}

fun String.getShareDate(): String {

    val dataDataFormat = SimpleDateFormat("yyyy.MM.dd")

    val createTime: Long = this.toLong()
    val createDate = Date(createTime)

    return dataDataFormat.format(createDate) + createTime.getWeek()

}


fun String.getShareDateYMD(): String {

    val dataDataFormat = SimpleDateFormat("yyyy.MM.dd")

    val createTime: Long = this.toLong()
    val createDate = Date(createTime)

    return dataDataFormat.format(createDate)

}

fun String.getShareTime(): String {

    val dataDataFormat = SimpleDateFormat("HH:mm")

    val createTime: Long = this.toLong()
    val createDate = Date(createTime)

    return dataDataFormat.format(createDate) + "讯"
}

fun getClockTime(creatTime: Int): String {
    val minuteFormat = SimpleDateFormat("mm:ss")
    val hourFormat = SimpleDateFormat("HH:mm:ss")

    var diffTimeStr: String? = ""
    val createTime = (creatTime * 1000).toLong()
    val createDate = Date(createTime)
    diffTimeStr = if (createTime > 3600000) {
        hourFormat.format(createDate)
    } else {
        minuteFormat.format(createDate)
    }
    return diffTimeStr!!
}

fun Long.getFeatureTime(): String {

    val dataDataFormat = SimpleDateFormat("yyyy-MM-dd")
    val createDate = Date(this)

    return dataDataFormat.format(createDate)

}


fun Long.getFeatureSecondTime(): String {

    val dataDataFormat = SimpleDateFormat("MM-dd")
    val createDate = Date(this*1000)

    return dataDataFormat.format(createDate)

}



fun Long.getSecondFullTime(isMill: Boolean = false): String {


    val dataDataFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val createDate = if (isMill) {
        Date(this * 1000)
    } else {
        Date(this)
    }

    return dataDataFormat.format(createDate)

}

fun Long.getShowRecentTime(): String {
    val current = System.currentTimeMillis() / 1000
    val disCount = current - this
    return when {
        disCount > 3600 * 24 -> {
            "${disCount / (3600 * 24)}天前"
        }
        disCount in 3600..3600 * 24 -> {
            "${disCount / (3600)}小时前"
        }
        disCount in 60..3600 -> {
            "${disCount / (60)}分钟前"
        }
        else -> {
            "刚刚"
        }
    }
}

fun Long.getHm(): String {
    val dataDataFormat = SimpleDateFormat("HH:mm")
    val createDate = Date(this*1000)
    return dataDataFormat.format(createDate)
}