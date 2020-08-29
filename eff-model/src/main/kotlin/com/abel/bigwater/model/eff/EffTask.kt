package com.abel.bigwater.model.eff

import com.abel.bigwater.model.BwBase
import com.abel.bigwater.model.JsonDateDeserializer
import com.abel.bigwater.model.JsonDateSerializer
import com.abel.bigwater.model.JsonHelper
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

/**
 * 计量效率分析任务
 */
data class EffTask(
        /**
         * 自增量
         */
        var taskId: Long? = null,

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
        var firmName: String? = null,

        /**
         * 水表数量
         */
        var meterCount: Int? = null,

        /**
         * 备注
         */
        var taskMemo: String? = null,

        /**
         * 任务包含的水表列表
         */
        var meterIdList: List<String>? = null
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
    var totalWater: Double? = null

    /**
     * 真实水量（方）
     */
    var realWater: Double? = null

    /**
     * 计量效率
     */
    var totalEff: Double? = null

    /**
     * 是否失效
     */
    var deprecated: Boolean? = null

    /**
     * 任务包括的水表计量效率
     */
    var meterList: List<EffMeter>? = null

}