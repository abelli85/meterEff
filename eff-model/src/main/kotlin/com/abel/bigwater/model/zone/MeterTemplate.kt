package com.abel.bigwater.model.zone

import com.abel.bigwater.model.JsonDateDeserializer
import com.abel.bigwater.model.JsonDateSerializer
import com.abel.bigwater.model.JsonHelper
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.io.Serializable
import java.util.*

/**
 * The template is for both hourly-flow and daily-flow of the zone with zoneId.
 * For hourly-flow, dayHour can be 0-23, which can be referred with same hour;
 * or 0, which can be referred as any hour (implemented by sorting dayHour desc).
 * Now other fields including weekDay/monthDay/yearMonth are ignored (may be
 * activated in future).
 * e.g., 2 hourly flow templates will fetch 10.5 for 1:00, while fetch 11.2 for 6:00 -
 * dayHour weekDay monthDay yearMonth flow
 * 2        -1      -1      -1      3.6
 * 1        -1      -1      -1      10.5
 * 0        -1      -1      -1      11.2
 *
 * For daily-flow, dayHour should be negative(-1); and weekDay can be same as day
 * of request, or 0 for all week days, or negative(-1) which doesn't care weekDay;
 * monthDay can be same as day of request, or 0 for all month days, or negative(-1)
 * which doesn't care monthDay. This is implemented by sorting weekDay * 100 + monthDay
 * desc.
 * e.g., the following daily-flow templates will fetch -
 * 23.2 for Tue; 109.6 for none Mon/Tue.
 * dayHour weekDay monthDay yearMonth flow
 * -1       2       -1      2       23.2
 * -1       1       -1      2       55.6
 * -1       0       -1      1       109.6
 *
 * the following daily-flow templates will fetch -
 * 160.5 for 5th March, 76.8 for 4th May, 66.8 for none 5th/4th.
 * -1       -1      5       3       160.5
 * -1       -1      4       3       76.8
 * -1       -1      0       3       66.8
 */
class MeterTemplate : Serializable {
    override fun toString(): String {
        return "{id:$wid,zoneId:$meterId,refType:$refType,[$dayHour,$weekDay,$monthDay,$yearMonth],Flow:($minFlow,$avgFlow,$maxFlow)}"
    }

    var wid: Long = 0

    /** 水表编号 */
    var meterId: String = ""

    /** 水表名称 */
    var meterName: String = ""

    /** 用户编号 */
    var userCode: String = ""

    /**
     * A_WEEK/B_DAY/C_MONTH/D_YEAR/E_HOUR
     */
    var refType: RefType = RefType.A_WEEK
    var dayHour: Int = -1
    var weekDay: Int = -1
    var monthDay: Int = -1
    var yearMonth: Int = -1

    /**
     * specified date for flow template.
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var tempDate: Date? = null

    /**
     * flow in (m3/h generally, but can be m3/day(month/double month) for manual meters.
     */
    var minFlow: Double? = null
    var maxFlow: Double = 0.0
    var avgFlow: Double = 0.0
    var flowStderr: Double? = null
    
    var minp: Double? = null
    var avgp: Double? = null
    var maxp: Double? = null
    var minpReading: Double? = null
    var maxpReading: Double? = null

    /**
     * should address period of reading, e.g., h/day/month/double month/etc.
     */
    var memo: String? = null
    
    var firmId: String? = null

    /**
     * update timestamp of flow template.
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var updateTime: Date? = null

}