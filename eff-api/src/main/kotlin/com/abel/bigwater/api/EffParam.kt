package com.abel.bigwater.api

import com.abel.bigwater.model.JsonDateDeserializer
import com.abel.bigwater.model.JsonDateSerializer
import com.abel.bigwater.model.JsonHelper
import com.abel.bigwater.model.eff.*
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.joda.time.DateTime
import java.util.*

/**
 * 机构ID移到基类中:
 * @see BaseParam.firmId
 */
class EffParam : BaseParam() {

    /**
     * 自增量
     */
    var taskId: Long? = null

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
     * 任务结束时段
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var taskEnd: Date? = null

    /**
     * 运行时间
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
     * 运行结束时间
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var runTime2: Date? = null

    @JsonIgnore
    var jodaTaskStart: DateTime? = null
        get() = if (taskStart == null) null else DateTime(taskStart)

    @JsonIgnore
    var jodaTaskEnd: DateTime? = null
        get() = if (taskEnd == null) null else DateTime(taskEnd)

    /**
     * 计量效率分析的时段类型
     */
    var periodType: String = EffPeriodType.Day.name
    var periodTypeObj: EffPeriodType?
        get() = EffPeriodType.values().find { it.name == periodType }
        set(value) {
            periodType = value!!.name
        }

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
     * 水表品牌标示
     * the meterBrandId to set
     */
    var meterBrandId: String? = null

    /**
     * 水表品牌名称
     * the meterBrandId to set
     */
    var meterBrandName: String? = null

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

    /**
     * 品牌、型号、口径水表对应行度下的老化计量效率
     */
    var decayList: List<VcEffDecay>? = null

}
