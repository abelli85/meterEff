package com.abel.bigwater.model

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

enum class LossStatus {
    CLOSED,
    OPEN
}

class BwLoss : BwBase() {

    /**
     * 自增量.
     */
    var wid: Long = 0

    /**
     * 分析日期, 截断时间部分.
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var statDate: Date? = null

    /**
     * 时长, 一般1天
     */
    var durationDay: Int = 0

    /**
     * DMA-ID
     */
    var dmaId: String? = null

    /**
     * DMA名称
     */
    var dmaName: String? = null

    /**
     * 调整后物理漏失量 (立方米).
     * Loss in m³ after adjusted with pressure.
     */
    var parentPhyloss1: Double? = null

    /**
     * 折算为小时漏量 (m³/h)
     */
    @JsonIgnore
    var lossPerHour: Double? = null
        get() = if (parentPhyloss1 == null) null else parentPhyloss1!! / if (durationDay ?: 0 < 1) 24.0 else 24.0 * durationDay
        private set

    /**
     * 总水量(立方米)
     * Total water usage in m³.
     */
    var parentV0: Double? = null

    /**
     * 机构ID. 一般可忽略, 集团单位有效.
     */
    var firmId: String? = null

    /**
     * 分析结果详情.
     */
    var comments: String? = null

    /**
     * 夜间小流 (m³/h)
     * Q (m³/h) in night, commonly between 3:00 AM to 6:00 AM.
     */
    var flowNight: Double = 0.0

    /**
     * 日均流量 (m³/h)
     * Q (m³/h) across the entire day.
     */
    var avgFlow: Double = 0.0

    /**
     * 状态, 当天数据完整, 状态为 {@see LossStatus.CLOSED};
     * 否则为 {@see LossStatus.OPEN}.
     */
    var status: LossStatus? = null


    /**
     * *************************
     * 水表ID, 及以下字段可忽略.
     * *************************
     * @return the meterId
     */
    var meterId: String? = null

    /**
     * 忽略
     * @return the reff
     */
    var reff: Double = 0.toDouble()

    /**
     * 调整前物理漏失量(立方米), 忽略
     * Loss in m³ before adjusted.
     */
    var parentPhyloss0: Double? = null

    /**
     * 调整系数, 忽略.
     * @return the pressureT
     */
    var pressureTeff: Double = 0.toDouble()

    /** Total duration in seconds.  */
    var numSeconds: Long? = null

    /**
     * @return the phylossRate
     */
    var phylossRate: Double? = null

    /**
     * @return the numberMeter
     */
    var numberMeter: Double? = null

    /**
     * @return the avgMeterv
     */
    var avgMeterv: Double? = null

    /**
     * @return the grossMeterv
     */
    var grossMeterv: Double? = null

    /**
     * @return the visualLoss
     */
    var visualLoss: Double? = null

    /**
     * @return the visualLossRate
     */
    var visualLossRate: Double? = null


    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    override fun toString(): String {
        return ("BwLoss [id=" + wid + ", statDate=" + statDate + ", durationDay="
                + durationDay + ", dmaId=" + dmaId + ", meterId=" + meterId
                + ", reff=" + reff + ", parentPhyloss0=" + parentPhyloss0
                + ", pressureT=" + pressureTeff + ", parentPhyloss1=" + parentPhyloss1
                + ", parentV0=" + parentV0 + ", phylossRate=" + phylossRate
                + ", numberMeter=" + numberMeter + ", avgMeterv=" + avgMeterv
                + ", grossMeterv=" + grossMeterv + ", visualLoss=" + visualLoss
                + ", visualLossRate=" + visualLossRate + ", firmId=" + firmId
                + ", status=" + status + "]")
    }
}
