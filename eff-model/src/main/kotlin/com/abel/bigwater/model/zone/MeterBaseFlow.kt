package com.abel.bigwater.model.zone

import com.abel.bigwater.model.JsonDateDeserializer
import com.abel.bigwater.model.JsonDateSerializer
import com.abel.bigwater.model.JsonHelper
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.io.Serializable
import java.util.*

abstract class MeterBaseFlow : Serializable {
    /** 顺序号，唯一标示，无意义仅做查询使用 */
    var wid: Int = 0

    /** 水表标示 */
    var meterId: String = ""

    /** 水司标示 */
    var firmId: String = ""

    /**　水表名称 */
    var meterName: String? = null

    /**
     * 位置的WKT数据，对应矢量坐标Point。
     * 转换矢量坐标对象，请参考Geo（C#）等相关框架。
     * the meterLoc to set
     */
    var meterLoc: String? = null

    /**
     * 小时水量- 小时的开始，小时以下的字段(分/秒/毫秒)为0;
     * 日水量 - 日的开始，日以下的字段为0;
     * 周水量 - 周的开始日期，日以下的字段为0;
     * 月水量 - 月的开始日期，日以下的字段为0.
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var time: Date = Date()

    /** 流量，In m3/h.*/
    var flow: Double = 0.0
        set(value) {
            field = value
        }
        get() {
            return if (field < 1E-6 && duration > 0 && (volume ?: 0.0) > 0.0)
                (volume ?: 0.0) * 3600 / duration else field
        }

    /** 实测流量，Same as flow: m3/h. */
    var measureFlow: Double? = null

    /** 压力，单位：In meter of water. */
    var pressureAvg: Double? = null

    /** 最小流量，Same as flow: m3/h. */
    var flowMin: Double? = null

    /** 最大流量, Same as flow: m3/h. */
    var flowMax: Double? = null

    /** 压力最小值，单位：In meter of water. */
    var pressureMin: Double? = null

    /** 压力最大值，单位：In meter of water. */
    var pressureMax: Double? = null

    /** 压力读数最小值，单位不确定：In meter of water. */
    var pressureReadingMin: Double? = null

    /** 压力读数最大值，单位不确定：In meter of water. */
    var pressureReadingMax: Double? = null

    /** 历史流量 */
    var histFlow: Double? = null
    /** 模板流量 */
    var tempFlow: Double? = null

    /** 水量In m3.*/
    var volume: Double? = null
    /** 时长In seconds.*/
    var duration: Int = 3600
    /** 开始行度 */
    var startReading: Double? = null

    /** 开始日期 */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var startTime: Date? = null

    /** 结束读数 */
    var endReading: Double? = null

    /** 结束日期 */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var endTime: Date? = null

    /** This dummy field is not used among this project.*/
    var pulseLiter: Int = 1000

    /** 最后更新时间 */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var updateTime: Date? = null
}