package calebxzhou.rdi.core.util

import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TimeUtils {
    val now: Timestamp
        get() = Timestamp(System.currentTimeMillis())
    @JvmStatic
	fun getTimePeriodOfDay(): String{
        return when (LocalDateTime.now().hour) {
            in 0..5 -> "凌晨"
            in 6..8 -> "早上"
            in 9..10 -> "上午"
            in 11..12 -> "中午"
            in 13..17 -> "下午"
            in 18..23 -> "晚上"
            else -> {"您好"}
        }
    }
    fun getFormattedDateTime(dateTime: LocalDateTime): String {
        val myFormatObj = DateTimeFormatter.ofPattern("yyyy年MM月dd日 E HH:mm:ss")
        return dateTime.format(myFormatObj).replace("Mon", "周一")
            .replace("Tue", "周二").replace("Wed", "周三")
            .replace("Thu", "周四").replace("Fri", "周五")
            .replace("Sat", "周六").replace("Sun", "周日")
    }

    fun getFormattedDateTime(dateTime: LocalDateTime, pattern: String?): String {
        val myFormatObj = DateTimeFormatter.ofPattern(pattern)
        return dateTime.format(myFormatObj).replace("Mon", "周一")
            .replace("Tue", "周二").replace("Wed", "周三")
            .replace("Thu", "周四").replace("Fri", "周五")
            .replace("Sat", "周六").replace("Sun", "周日")
    }

    fun getWeekByInt(day: Int): String {
        return when (day) {
            1 -> "一"
            2 -> "二"
            3 -> "三"
            4 -> "四"
            5 -> "五"
            6 -> "六"
            7 -> "日"
            else -> "?"
        }
    }

    fun getComparedDateTime(dtR: Timestamp): String {
        return getComparedDateTime(
            dtR.toLocalDateTime(),
            now.toLocalDateTime()
        )
    }

    /**
     * 1.day相同 - 今天
     * 2.day差1  - 昨天
     * 3.day差2~7 - 周123/上周456日
     *
     *
     * @param dtRec 旧日期
     * @param dtNow 新日期--现在的日期
     * @return 类似于 今天9：20，昨天2：40，周二2：49，上周六22：45，4月4日12：45，20xx年x月x日 xx:xx
     */
    fun getComparedDateTime(dtRec: LocalDateTime, dtNow: LocalDateTime): String {
        var formattedTimePrefix: String? = null
        //如果不是今年，返回完整记录
        if (dtRec.year != dtNow.year) {
            return getFormattedDateTime(dtRec)
        }
        //如果是今年，但 不是这个月，返回X月X日 xx:xx记录
        if (dtRec.month.value != dtNow.month.value) {
            return getFormattedDateTime(dtRec, "MM月dd日HH:mm")
        }

        //如果是今年，是 这个月，但是日期是 前天 或更早（），显示周几/上周几
        if (dtNow.dayOfYear - dtRec.dayOfYear >= 2) {
            val dt2Week = dtNow.dayOfWeek.value
            val dt1Week = dtRec.dayOfWeek.value
            //早周X>=晚周X说明是上周
            formattedTimePrefix =
                if (dt1Week >= dt2Week) "上周" + getWeekByInt(dt1Week) else "周" + getWeekByInt(dt2Week)
        }
        //如果是昨天
        if (dtRec.dayOfYear == dtNow.dayOfYear - 1) {
            formattedTimePrefix = "昨天"
        }
        //如果是今天
        if (dtRec.dayOfYear == dtNow.dayOfYear) {
            formattedTimePrefix = "今天"
        }
        return (formattedTimePrefix + dtRec.hour + ":"
                + if (dtRec.minute < 10) "0" + dtRec.minute else dtRec.minute)
    }
}
