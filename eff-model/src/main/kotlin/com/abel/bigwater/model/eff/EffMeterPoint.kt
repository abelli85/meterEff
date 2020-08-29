package com.abel.bigwater.model.eff

import com.abel.bigwater.model.BwBase

/**
 * 水表的单个计量点用水模式
 */
class EffMeterPoint : BwBase() {
    /**
     * 任务自增量
     */
    var taskId: Long? = null

    /**
     * 水表标示
     */
    var meterId: String? = null

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