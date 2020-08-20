package com.abel.bigwater.model.zone

import java.text.DateFormat

class MeterHourFlow: MeterBaseFlow() {

    /** 昨日同时水量 */
    var yestHour: Double? = null
    /** 上周同日同时水量 */
    var weekHour: Double? = null
    /** 上周同时水量均值 */
    var weekHourAvg: Double? = null
    /** 上周同时水量方差 */
    var weekHourError: Double? = null
    /** 上月同日同时水量 */
    var monthHour: Double? = null
    /** 上月同时水量均值 */
    var monthHourAvg: Double? = null
    /** 上月同时水量方差 */
    var monthHourError: Double? = null
    /** 上年同日同时水量 */
    var yearHour: Double? = null
    /** 上年同月同日同时水量均值 */
    var yearHourAvg: Double? = null
    /** 上年同月同日同时水量方差 */
    var yearHourError: Double? = null


    override fun toString(): String {
        return "{id:$wid,meterId:$meterId,meterName:meterName,time:%s,yestHour:$yestHour,}"
                .format(DateFormat.getInstance().format(this.time))
    }
}