package com.abel.bigwater.model.eff

import com.abel.bigwater.model.BwBase
import com.abel.bigwater.model.JsonDateDeserializer
import com.abel.bigwater.model.JsonDateSerializer
import com.abel.bigwater.model.JsonHelper
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

data class EffDetail(
        /**
         * 自增量
         */
        var wid: Long? = null,

        /**
         * 水表ID
         */
        var meterId: String? = null,

        /**
         * 任务名称
         */
        var taskName: String? = null,

        /**
         * @return the id
         */
        var firmId: String? = null,

        /**
         * @return the name
         */
        var firmName: String? = null
) : BwBase() {
    /**
     * 任务开始时段
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var taskStart: Date? = null

    /**
     * 任务开始时段
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var taskEnd: Date? = null

    /**
     * 运行开始时间
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var runTime: Date? = null

    /**
     * 运行耗时（秒）
     */
    var runDuration: Int? = null

    /**
     * 分析结论
     */
    var taskResult: String? = null

    /**
     * 实测水量（方）
     */
    var meterWater: Double? = null

    /**
     * 流量区间的水量(方), 从0～Q4 计5个区间
     */
    var q0v: Double? = null
    var q1v: Double? = null
    var q2v: Double? = null
    var q3v: Double? = null
    var q4v: Double? = null

    /**
     * 其他区间的水量（方）
     */
    var qtv: Double? = null

    /**
     * 真实水量（方）
     */
    var realWater: Double? = null

    /**
     * 计量效率
     */
    var meterEff: Double? = null

    /**
     * 起始行度
     */
    var startFwd: Double? = null

    /**
     * 结束行度
     */
    var endFwd: Double? = null

    /**
     * 口径
     * the size to set
     */
    var sizeId: String? = null

    /**
     * 口径
     * the size to set
     */
    var sizeName: String? = null

    /**
     * 水表型号
     * the model to set
     */
    var modelSize: String? = null

    /**
     * 引用的衰减系数
     */
    var decayEff: String? = null

    /**
     * Q4
     */
    var q4: Double? = 0.0

    /**
     * Q3
     */
    var q3: Double? = 0.0

    /**
     * Q3/Q1
     */
    var q3toq1: Double? = 0.0

    /**
     * Q4/Q3
     */
    var q4toq3: Double? = 0.0

    /**
     * Q2/Q1
     */
    var q2toq1: Double? = 0.0

    /**
     * q1~9的精度数据
     */
    var qr1: Double? = null
    var qr2: Double? = null
    var qr3: Double? = null
    var qr4: Double? = null
}