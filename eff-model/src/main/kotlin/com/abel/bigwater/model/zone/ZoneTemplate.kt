package com.abel.bigwater.model.zone

import java.io.Serializable

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
class ZoneTemplate : Serializable {
    override fun toString(): String {
        return "{id:$wid,zoneId:$zoneId,refType:$refType,[$dayHour,$weekDay,$monthDay,$yearMonth],waterConsume:$waterConsume,minFlow:$minFlow}"
    }

    var wid: Long = 0
    var zoneId: String = ""

    /**
     * A_WEEK/B_DAY/C_MONTH/D_YEAR/E_HOUR
     */
    var refType: RefType = RefType.A_WEEK
    var dayHour: Int = -1
    var weekDay: Int = -1
    var monthDay: Int = -1
    var yearMonth: Int = -1

    /** 消费水量，m³ */
    var waterConsume: Double = 0.0

    /** 总漏损，m³ */
    var totalLeakage: Double = 0.0

    /** 物理漏损，m³ */
    var physicalLeakage: Double = 0.0

    /** 表观漏损，m³ */
    var measureLeakage: Double = 0.0

    /** 其他未监控 */
    var otherMissing: Double = 0.0

    /** 最小流量，m³/h */
    var minFlow: Double? = null
    /** 最大流量，m³/h */
    var maxFlow: Double = 0.0
    /** 平均流量，m³/h */
    var avgFlow: Double = 0.0

    /** 备注 */
    var memo: String? = null

}