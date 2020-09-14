package com.abel.bigwater.model

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

/**
 * 水表数据量统计
 */
class MeterDayDataCount : BwBase() {

    /**
     * the id to set
     */
    var meterId: String? = null

    /**
     * the name to set
     */
    var meterName: String? = null

    /**
     * 远传标识码
     */
    var extId: String? = null

    /**
     * the firmId to set
     */
    var firmId: String? = null

    /**
     * 大前天
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var day1: Date? = null

    /**
     * 大前天 数据量
     */
    var day1Count: Int? = null

    /**
     * 前天 数据量
     */
    var day2Count: Int? = null

    /**
     * 昨天 数据量
     */
    var day3Count: Int? = null

    /**
     * 默认每次补传2小时.
     * 注: 如果让后台自动补传, 须准确填写该字段.
     */
    var hoursOnce: Int = 2

    /**
     * 补传数据的间隔, 默认5分钟.
     * 注: 如果让后台自动补传, 须准确填写该字段.
     */
    var periodMinutes: Int = 5

    /**
     * 今日零点.
     * 临时字段, 不返回前端.
     */
    @JsonIgnore
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var endDay: Date? = null

    /**
     * 日期
     */
    var dayInt: Int? = null
}