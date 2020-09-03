package com.abel.bigwater.model.eff

import com.abel.bigwater.model.BwBase
import com.abel.bigwater.model.JsonDateDeserializer
import com.abel.bigwater.model.JsonDateSerializer
import com.abel.bigwater.model.JsonHelper
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

open class VcMeterVerifyPoint : BwBase() {

    /**
     * 自动标示编号
     */
    var pointId: Long? = null

    /**
     * 检定自动编号
     */
    var verifyId: Long? = null

    /**
     * 水表标示
     */
    var meterId: String? = null

    /**
     * 模板标示
     */
    var tempId: String? = null

    /**
     * 流量点编号, 1~9.
     */
    var pointNo: Int? = null

    /**
     * 流量点
     */
    var pointName: String? = null

    /**
     * 流量点数值，单位 m³/h.
     */
    var pointFlow: Double? = 0.0

    /**
     * 启始读数
     */
    var startReading: Double? = 0.0

    /**
     * 终止读数
     */
    var endReading: Double? = 0.0

    /**
     * 累积体积
     */
    var totalVolume: Double? = 0.0

    /**
     * 检定时间
     */
    var verifyDura: Double? = 0.0

    /**
     * 平均流量
     */
    var avgFlow: Double? = 0.0

    /**
     * 水温
     */
    var waterTemp: Double? = 0.0

    /**
     * 启始质量
     */
    var startMass: Double? = 0.0

    /**
     * 终止质量
     */
    var endMass: Double? = 0.0

    /**
     * 标准质量
     */
    var standardMass: Double? = 0.0

    /**
     * 密度
     */
    var density: Double? = 0.0

    /**
     * 标准体积
     */
    var standardVolume: Double? = 0.0

    /**
     * 标准时间
     */
    var standardDura: Double? = 0.0

    /**
     * 标准流量
     */
    var standardFlow: Double? = 0.0

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
     * 1-超限, 不合格; 0 - 不超限, 合格.
     */
    var exceed: Int? = null

    /**
     * 合格
     */
    var boardResult: String? = null

    /**
     * 检定员
     */
    var stuffName: String? = null

    /**
     * 委托单号
     */
    var batchId: String? = null

    /**
     * 装置编号
     */
    var instrumentNo: String? = null

    /**
     * 检定台的自增量标识
     */
    var itemId: Long? = null

    /**
     * 来自检定台的内容
     */
    var boardMemo: String? = null

    /**
     * 检定日期
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var verifyDate: Date? = null
}