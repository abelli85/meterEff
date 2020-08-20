package com.abel.bigwater.model.zone

import com.abel.bigwater.model.JsonDateDeserializer
import com.abel.bigwater.model.JsonDateSerializer
import com.abel.bigwater.model.JsonHelper
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.io.Serializable
import java.util.*

const val MINI_DELTA: Double = 1.0E-6

/**
 * 分区水量
 */
abstract class ZoneFlow : Serializable {
    open var id: Long = 0

    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    open var time: Date = Date()

    open var zoneId: String = ""
    open var zoneName: String = ""
    open var zoneType: ZoneType = ZoneType.FAMILY

    /** 时长，秒 */
    var durationSeconds: Int = 3600

    /** 进水量, 单位：m³ */
    var waterIn: Double = 0.0

    /** 出水量，单位: m³ */
    var waterOut: Double = 0.0

    /** 计算供水量，可能为空，如果无进水量&出水量 */
    var waterTotal: Double = 0.0
        get() = waterIn - waterOut

    /** 需水量，即所有子分区的供水量合计，单位：m³ */
    var waterSubtotal: Double = 0.0

    /** 实测水量，即有数据的水量 */
    var waterMeasure: Double = 0.0

    /** 历史水量，当实测水量缺失时，用历史数据作为需水量的部分。*/
    var waterHist: Double = 0.0

    /** 模板水量，当实测水量缺失时，用模板水量作为需水量的部分。*/
    var waterTemp: Double = 0.0

    /** 合计漏损。*/
    var totalLeakage: Double = 0.0

    /** 物理漏损. */
    var physicalLeakage: Double = 0.0

    /** 表观漏损。*/
    var measureLeakage: Double = 0.0

    /** 其他未监控水量 */
    var otherMissing: Double = 0.0

    /** 行度，单位：m³ */
    var reading: Double? = 0.0

    /** This dummy field is not used among this project.*/
    var pulseLiter: Int = 1000

    /** 是否完成计算。 */
    var finished: Boolean = false

    /**
     * 本片区包含的大表数量
     */
    var totalMeterCount: Int? = null

    /**
     * 本时段内(小时或日)已上传完整数据的大表数量
     */
    var finishedMeterCount: Int? = null

    /**
     * 本时段内(小时或日)尚未完成(正在上传数据)的大表数量
     */
    var uploadingMeterCount: Int? = null

    /**
     * 本片区包含的子片区（不含孙片区等）数量
     */
    var totalSubCount: Int? = null

    /**
     * 本时段内已上传完整数据的子片区（不含孙片区等）
     */
    var finishedSubCount: Int? = null

    /**
     * 本时段内正在上传数据的子片区（不含孙片区等）
     */
    var uploadingSubCount: Int? = null

    /** 统计方法。 */
    var sumMethod: SumMethod = SumMethod.MIXED

    /** 报警水量 */
    var warnWater: Double = 0.0

    /** 报警指数 */
    abstract var warnIndex: Double

    /** 是否需要维修 */
    var shouldFix: Boolean = false

    /** 报警等级 */
    abstract var checkWarn: WarnLevel

    /** 故障计数 */
    var redCount: Int = 0

    /**
     * 位置的WKT文本。
     */
    var zoneLoc: String? = null

    /** 水司ID */
    var firmId: String? = null

    /** 最后更新时间 */
    var updateTime: Date? = null

    /** 报警文本 */
    var header: String = ""
        get() {
            if (redCount >= 3) {
                return "$zoneName(报警:$redCount"
            } else {
                return zoneName
            }
        }

    /** 来自实测水量的比例*/
    var measureRatio: Double = 0.0
        get() {
            if (waterTotal > 1.0) {
                return waterMeasure / waterTotal
            } else {
                return 0.0
            }
        }

}