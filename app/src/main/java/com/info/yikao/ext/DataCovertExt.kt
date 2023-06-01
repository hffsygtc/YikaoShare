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

/**
 * 省份证id正则校验
 */
fun isIDNumber(IDNumber: String?): Boolean {
    if (IDNumber == null || "" == IDNumber) {
        return false
    }
    // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
    val regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" + "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)"
    //假设18位身份证号码:41000119910101123X  410001 19910101 123X
    //^开头
    //[1-9] 第一位1-9中的一个      4
    //\\d{5} 五位数字           10001（前六位省市县地区）
    //(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
    //\\d{2}                    91（年份）
    //((0[1-9])|(10|11|12))     01（月份）
    //(([0-2][1-9])|10|20|30|31)01（日期）
    //\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
    //[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
    //$结尾

    //假设15位身份证号码:410001910101123  410001 910101 123
    //^开头
    //[1-9] 第一位1-9中的一个      4
    //\\d{5} 五位数字           10001（前六位省市县地区）
    //\\d{2}                    91（年份）
    //((0[1-9])|(10|11|12))     01（月份）
    //(([0-2][1-9])|10|20|30|31)01（日期）
    //\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
    //$结尾


    val matches = IDNumber.matches(regularExpression.toRegex())

    //判断第18位校验值
    if (matches) {

        if (IDNumber.length == 18) {
            try {
                val charArray = IDNumber.toCharArray()
                //前十七位加权因子
                val idCardWi = intArrayOf(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)
                //这是除以11后，可能产生的11位余数对应的验证码
                val idCardY = arrayOf("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2")
                var sum = 0
                for (i in idCardWi.indices) {
                    val current = Integer.parseInt(charArray[i].toString())
                    val count = current * idCardWi[i]
                    sum += count
                }
                val idCardLast = charArray[17]
                val idCardMod = sum % 11
                if (idCardY[idCardMod].toUpperCase() == idCardLast.toString().toUpperCase()) {
                    return true
                } else {
                    println("身份证最后一位:" + idCardLast.toString().toUpperCase() +
                            "错误,正确的应该是:" + idCardY[idCardMod].toUpperCase())
                    return false
                }

            } catch (e: Exception) {
                e.printStackTrace()
                println("异常:$IDNumber")
                return false
            }

        }

    }
    return matches
}