package com.abel.bigwater.model.eff

import com.abel.bigwater.model.BwBase
import com.abel.bigwater.model.JsonDateDeserializer
import com.abel.bigwater.model.JsonDateSerializer
import com.abel.bigwater.model.JsonHelper
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

/**
 * 水表的计量效率
 */
data class EffMeter(
        /**
         * 计量效率自增量
         */
        var effId: Long? = null,

        /**
         * 任务自增量
         */
        var taskId: Long? = null,

        /**
         * 水表ID
         */
        var meterId: String? = null,

        /**
         * 水表名称
         * the name to set
         */
        var meterName: String? = null,

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
     * 任务结束时段
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var taskEnd: Date? = null

    /**
     * 计量效率分析的时段类型
     */
    var periodType: String = EffPeriodType.Day.name

    @JsonIgnore
    @JSONField(serialize = false)
    var periodTypeObj: EffPeriodType? = null
        get() = EffPeriodType.values().find { it.name == periodType }
        set(value) {
            field = value
            periodType = value?.name ?: EffPeriodType.Day.name
        }

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
     * 水表品牌标示
     * the meterBrandId to set
     */
    var meterBrandId: String? = null

    /**
     * 口径
     * the size to set
     */
    var sizeId: Int? = null

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
     * 引用的数据行数
     */
    var dataRows: Int = 0

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

    /**
     * 计量效率的用水模式
     */
    var pointEffList: List<EffMeterPoint>? = null

    /**
     * 标准的用水模式
     */
    var modelPointList: List<EffMeterPoint>? = null

    /**
     * 记录点检定结果
     */
    var pointList: List<VcMeterVerifyPoint>? = null

    /**
     * 该品牌、型号、口径水表对应行度下的老化计量效率
     */
    var effDecay: VcEffDecay? = null
}