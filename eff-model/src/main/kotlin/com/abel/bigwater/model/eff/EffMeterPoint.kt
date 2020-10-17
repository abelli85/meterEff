package com.abel.bigwater.model.eff

import com.abel.bigwater.model.BwBase
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * 水表的单个计量点用水模式
 */
class EffMeterPoint : BwBase() {
    /**
     * 自增量
     */
    var wid: Long? = null

    /**
     * 任务自增量
     */
    var taskId: Long? = null

    /**
     * 水表标示
     */
    var meterId: String? = null

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
     * 模式(只分离用水模式, 不分析计量效率)/效率(包含用水模式及计量效率)
     * @see EffPeriodType
     */
    var pointType: String? = null

    @JsonIgnore
    @JSONField(serialize = false)
    var pointTypeObj: EffPointType? = null
        get() {
            field = EffPointType.values().find { it.name == pointType }
            return field
        }
        set(value) {
            field = value
            pointType = value?.name
        }

    /**
     * 流量点
     */
    var pointName: String? = null

    /**
     * 流量点编号, 1~9.
     */
    var pointNo: Int? = null

    /**
     * 流量点数值，单位 m³/h.
     */
    var pointFlow: Double? = 0.0

    /**
     * 实测误差
     */
    var pointDev: Double? = 0.0

    /**
     * 容许高限
     */
    var highLimit: Double? = 0.0

    /**
     * 容许底限
     */
    var lowLimit: Double? = 0.0

    /**
     * 实测水量（方）
     */
    var pointWater: Double? = null

    /**
     * 真实水量（方）
     */
    var realWater: Double? = null
}