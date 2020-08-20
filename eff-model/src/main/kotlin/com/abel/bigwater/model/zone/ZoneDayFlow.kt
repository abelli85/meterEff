package com.abel.bigwater.model.zone

import java.text.DateFormat

/**
 * 片区的日水量方差
 */
class ZoneDayFlow() : ZoneFlow() {
    val WATER_DELTA = 1E-6
    val WATER_LIMIT = 10.0

    init {
        /** 时长，秒 */
        durationSeconds = 86400
    }

    /** 最小流量，单位：m³/h */
    var minFlow: Double = 0.0
    /** 最大流量，单位：m³/h */
    var maxFlow: Double = 0.0
    /** 平均流量，单位：m³/h */
    var avgFlow: Double = 0.0

    /** 昨日水量，单位：m³ */
    var yestWater: Double? = null
    /** 昨日最小流量，单位：m³/h */
    var yestMin: Double? = null

    /** 上周同日水量 */
    var weekDay: Double? = null
    /** 上周日均水量 */
    var weekAvg: Double? = null
    /** 上周日方差 */
    var weekError: Double? = null

    /** 上周日最小流量，m³/h */
    var weekDayMin: Double? = null
    /** 上周日最小流量均值，m³/h */
    var weekMinAvg: Double? = null
    /** 上周日最小流量方差 */
    var weekMinError: Double? = null

    /** 上月同日水量，m³ */
    var monthDay: Double? = null
    /** 上月日水量均值，m³ */
    var monthAvg: Double? = null
    /** 上月日水量方差，m³ */
    var monthError: Double? = null

    /** 上月日水量极小值, m³/h */
    var monthDayMin: Double? = null
    /** 上月最小流量均值, m³/h */
    var monthMinAvg: Double? = null
    /** 上月最小流量方差, m³/h */
    var monthMinError: Double? = null

    /** 去年同月同日水量，m³ */
    var yearDay: Double? = null
    /** 去年月均水量, m³ */
    var yearMonthAvg: Double? = null
    /** 去年月均水量方差，m³ */
    var yearMonthError: Double? = null

    /** 去年同月水量极小值，m³ */
    var yearMonthMin: Double? = null
    /** 去年月水量极小值均值，m³ */
    var yearMonthMinAvg: Double? = null
    /** 去年月水量最小值方差，m³ */
    var yearMonthMinError: Double? = null

    /** 报警指数 */
    override var warnIndex: Double
        get() {
            if ((WATER_DELTA < waterTotal && waterTotal < WATER_LIMIT)
                    || (WATER_DELTA < waterSubtotal && waterSubtotal < WATER_LIMIT)) return 0.0

            var totalRef: Double = 0.0
            var v1 = if (waterTotal > MINI_DELTA) waterTotal else waterSubtotal

            totalRef += if (yestWater != null && v1 + yestWater!! > 1.0)
                (v1 - yestWater!!) / (v1 + yestWater!!) else 0.0

            totalRef += if (weekDay != null && v1 + weekDay!! > 1.0)
                (v1 - weekDay!!) / (v1 + weekDay!!) else 0.0

            totalRef += if (weekError != null && weekError!! > 1.0 && weekDay != null)
                (v1 - weekDay!!) / weekError!! else 0.0

            totalRef += if (weekMinError != null && weekMinError!! > 1.0 && weekDayMin != null)
                (minFlow - weekDayMin!!) / weekMinError!! else 0.0

            totalRef += if (monthDay != null && v1 + monthDay!! > 1.0)
                (v1 - monthDay!!) / (v1 + monthDay!!) else 0.0

            totalRef += if (monthError != null && monthError!! > 1.0 && monthDay != null)
                (v1 - monthDay!!) / monthError!! else 0.0

            totalRef += if (monthMinError != null && monthMinError!! > 1.0 && monthDayMin != null)
                (minFlow - monthDayMin!!) / monthMinError!! else 0.0

            totalRef += if (yearDay != null && v1 + yearDay!! > 1.0)
                (v1 - yearDay!!) / (v1 + yearDay!!) else 0.0

            totalRef += if (yearMonthError != null && yearMonthError!! > 1.0 && yearDay != null)
                (v1 - yearDay!!) / yearMonthError!! else 0.0

            totalRef += if (yearMonthMinError != null && yearMonthMinError!! > 1.0 && yearMonthMin != null)
                (minFlow - yearMonthMin!!) / yearMonthMinError!! else 0.0

            return totalRef.toInt().toDouble()
        }
        set(value) {
        }

    /** 报警等级 */
    override var checkWarn: WarnLevel = WarnLevel.GREEN
        get() {
            return if (warnIndex < -8.0 || warnIndex > 8.0) WarnLevel.RED
            else if (warnIndex < -4 || warnIndex > 4) WarnLevel.ORANGE
            else WarnLevel.GREEN
        }
        set(value) {
            field = value
        }

    override fun toString(): String {
        return "{id:$id,zoneId:$zoneId,zoneName:$zoneName,time:%s,waterTotal:$waterTotal,subtotal:${waterSubtotal},yestWater:${yestWater},minFlow:${minFlow},warnIndex:$warnIndex}"
                .format(DateFormat.getInstance().format(this.time))
    }
}