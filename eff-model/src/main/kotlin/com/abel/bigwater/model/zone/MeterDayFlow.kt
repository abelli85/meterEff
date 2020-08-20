package com.abel.bigwater.model.zone

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * time - 周水量中表示当周第一个水量的日期，在水量完整的情况下，为当周第一天即周日。
 * 月水量中表示当月第一个水量的日期, 在水量完整的情况下，为当月1日。
 *
 * 周水量/月水量- 如下字段有效，其他字段无未赋值:
 *
SELECT firm_id AS firmId
, MIN(meter_time) AS `time`
, AVG(pressure_avg) AS pressureAvg
, MIN(flow_min) AS flowMin
, MAX(flow_max) AS flowMax
, MIN(pressure_min) AS pressureMin
, MAX(pressure_max) AS pressureMax
, MIN(pressure_reading_min) AS pressureReadingMin
, MAX(pressure_reading_max) AS pressureReadingMax
, SUM(meter_volume) AS volume
, SUM(meter_duration) AS duration
, MIN(start_reading) AS startReading
, MIN(start_time) AS startTime
, MAX(end_reading) AS endReading
, MAX(end_time) AS endTime
, pulse_liter AS pulseLiter
, YEAR(meter_time) * 100 + WEEK(meter_time) AS yearWeek
FROM dm_meter_day_flow zf
 */
class MeterDayFlow : MeterBaseFlow() {
    init {
        // 一天时间
        duration = 86400
    }

    /**
     * 天数，默认为1天。7天-表示周水量; >=28天-表示月水量.
     * 不满1天-表示当天数据还不全;
     * 不满7天-表示当周水量不全;
     * 不满一月-表示当月水量不全.
     */
    @JsonIgnore
    var days: Double? = 1.0
        get() = duration / 86400.0

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
}