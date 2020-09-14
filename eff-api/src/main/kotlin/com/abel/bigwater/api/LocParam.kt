package com.abel.bigwater.api

import com.abel.bigwater.model.JsonDateDeserializer
import com.abel.bigwater.model.JsonDateSerializer
import com.abel.bigwater.model.JsonHelper
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

/**
 * 机构ID移到基类中:
 * @see BaseParam.firmId
 */
data class LocParam(

        /**
         * DMA标识
         */
        var dmaId: String? = null,

        /**
         * 统计基准
         */
        var durationDay: String? = null,

        /**
         * 统计类型, 参考{@link com.abel.bigmeter.model.BwStatType}.
         */
        var statTypeId: String? = null,

        /**
         * 用来删除漏损、最小流量的标示列表
         */
        var widList: List<String>? = null) : BaseParam() {

    /**
     * 统计日期
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var statDate: Date? = null

    /**
     * 统计时段 起始日期
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var statDate1: Date? = null

    /**
     * 统计时段 结束日期
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var statDate2: Date? = null

    /**
     * 创建日期范围的起始日期
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var createDate1: Date? = null

    /**
     * 创建日期范围的结束日期
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var createDate2: Date? = null

}