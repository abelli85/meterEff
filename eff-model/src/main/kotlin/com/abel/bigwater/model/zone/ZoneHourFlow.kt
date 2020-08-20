package com.abel.bigwater.model.zone

import java.text.DateFormat

/**
 * 分区小时水量
 */
class ZoneHourFlow() : ZoneFlow() {
    val WATER_DELTA = 1E-6
    val WATER_LIMIT = 1.0

    /** 昨日同时水量 */
    var yestHour: Double? = null

    /** 上周同日同时水量 */
    var weekHour: Double? = null

    /** 上周7天的同时水量均值 */
    var weekHourAvg: Double? = null
    /** 上周7天的同时水量方差 */
    var weekHourError: Double? = null

    /** 上月同日同时水量 */
    var monthHour: Double? = null

    /** 上月所有天的同时水量均值 */
    var monthHourAvg: Double? = null
    /** 上月所有天的同时水量方差 */
    var monthHourError: Double? = null

    /** 昨日同时水量 */
    var yearHour: Double? = null
    /** 昨日24时的水量均值 */
    var yearHourAvg: Double? = null
    /** 昨日24时的水量方差 */
    var yearHourError: Double? = null

    /** 报警指数 */
    override var warnIndex: Double = 0.0
        get() {
            if ((WATER_DELTA < waterTotal && waterTotal < WATER_LIMIT)
                    || (WATER_DELTA < waterSubtotal && waterSubtotal < WATER_LIMIT)) return 0.0

            var totalRef: Double = 0.0
            var v1 = if (waterTotal > MINI_DELTA) waterTotal else waterSubtotal

            totalRef += if (weekHourError != null && weekHourError!! > 0.1 && weekHour != null)
                (v1 - weekHour!!) / weekHourError!! else 0.0

            totalRef += if (yestHour != null && waterTotal + yestHour!! > 1.0)
                10 * (v1 - yestHour!!) / (v1 + yestHour!!) else 0.0

            totalRef += if (monthHourError != null && monthHourError!! > 1.0 && monthHour != null)
                (v1 - monthHour!!) / monthHourError!! else 0.0

            totalRef += if (yearHourError != null && yearHourError!! > 1.0 && yearHour != null)
                (v1 - yearHour!!) / yearHourError!! else 0.0

            return totalRef.toInt().toDouble()
        }
        set(value) {
            field = value
        }

    /** 报警等级 */
    override var checkWarn: WarnLevel = WarnLevel.GREEN
        get() {
            return if (warnIndex < -4 || warnIndex > 4) WarnLevel.RED
            else if (warnIndex < -2 || warnIndex > 2) WarnLevel.ORANGE
            else WarnLevel.GREEN
        }
        set(value) {
            field = value
        }

    override fun toString(): String {
        return "{id:$id,zoneId:$zoneId,zoneName:$zoneName,time:%s,waterTotal:$waterTotal,subtotal:${waterSubtotal},yestHour:$yestHour,warnIndex:$warnIndex}"
                .format(DateFormat.getInstance().format(this.time))
    }
}