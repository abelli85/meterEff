package com.abel.bigwater.model.zone

import com.abel.bigwater.model.JsonDateDeserializer
import com.abel.bigwater.model.JsonDateSerializer
import com.abel.bigwater.model.JsonHelper
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

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
class MeterWeekFlow : MeterBaseFlow() {
    init {
        // 一天时间
        duration = 86400 * 7
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

    /**
     * 年份+周序号, 周序号-0~53.
     * 如 201819 - 2018年第19周
     */
    var yearWeek: Int? = null

    /** 周日均水量, m³ */
    var weekDailyAvg: Double? = null
    /** 周日方差, m³ */
    var weekDailyError: Double? = null

    /** 周的日最小水量，m³ */
    var weekDailyMin: Double? = null

    /** 周最小水量的日*/
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var weekDailyMinDay: Date? = null

    /** 周的日最大水量, m³ */
    var weekDailyMax: Double? = null

    /** 周最大水量的日*/
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var weekDailyMaxDay: Date? = null

    /** 去年同周的日水量，m³ */
    var lastYearWeekDaily: Double? = null
}