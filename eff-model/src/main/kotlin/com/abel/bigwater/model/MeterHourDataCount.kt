package com.abel.bigwater.model

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

/**
 * 水表数据量统计
 */
class MeterHourDataCount : BwBase() {

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
     * 小时数据量的日期.
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var statDay: Date? = null

    /**
     * 今日零点.
     * 临时字段, 不返回前端.
     */
    @JsonIgnore
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var endDay: Date? = null

    /**
     * 小时.
     */
    var hourInt: Int? = null

    /**
     * 小时数据条数.
     */
    var cnt1: Int? = null
}