package com.abel.bigwater.api

import com.abel.bigwater.model.JsonDateDeserializer
import com.abel.bigwater.model.JsonDateSerializer
import com.abel.bigwater.model.JsonHelper
import com.abel.bigwater.model.PowerType
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
     * 计量效率自增量
     */
    var effId: Long? = null

    /**
     * 自增量
     */
    var wid: Long? = null

    /**
     * 水表ID
     */
    var meterId: String? = null

    /**
     * 水表编号
     * the meterCode to set
     */
    var meterCode: String? = null

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
     * 供电类型
     * the powerType to set
     */
    var powerType: String? = null

    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    var powerTypeObj: PowerType? = null
        get() {
            field = PowerType.values().find { it.name == powerType }
            return field
        }
        set(value) {
            field = value
            powerType = field?.name
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
     * 远传水表进行口径匹配分析.
     * 暂定Q2～Q3 流量段用水量占比≥80%，匹配。否则，不匹配。
     */
    var matchQ2v: Double = 80.0

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

    /**
     * 老化模板ID
     */
    var decayId: Long? = null

    /**
     * 分析结论
     */
    var taskResult: String? = null

    /**
     * 数据页
     */
    var index: Int = 0
    var rows: Int = 2000

    /**
     * 日水量范围, 从
     * @see lowDayConsume 到
     * @see highDayConsume
     */
    var lowDayConsume: Double? = null

    var highDayConsume: Double? = null

}
