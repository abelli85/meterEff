package com.abel.bigwater.api

import com.abel.bigwater.model.JsonDateDeserializer
import com.abel.bigwater.model.JsonDateSerializer
import com.abel.bigwater.model.JsonHelper
import com.abel.bigwater.model.eff.EffMeter
import com.abel.bigwater.model.eff.EffMeterPoint
import com.abel.bigwater.model.eff.VcMeterVerifyPoint
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

class EffParam : BaseParam() {

    /**
     * 自增量
     */
    var taskId: Long? = null

    /**
     * @return the id
     */
    var firmId: String? = null

    /**
     * 水表ID
     */
    var meterId: String? = null

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
     * 运行开始时间
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var runTime1: Date? = null

    /**
     * 运行开始时间
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var runTime2: Date? = null

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
     * 水表ID列表
     */
    var meterIdList: List<String>? = null

    /**
     * 批量添加
     */
    var meterList: List<EffMeter>? = null

    /**
     * 用水模式
     */
    var pointEffList: List<EffMeterPoint>? = null

    /**
     * 记录点检定结果
     */
    var pointList: List<VcMeterVerifyPoint>? = null

}
