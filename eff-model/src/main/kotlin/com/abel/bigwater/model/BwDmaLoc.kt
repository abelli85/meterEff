/**
 *
 */
package com.abel.bigwater.model

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.text.SimpleDateFormat
import java.util.*

/**
 * DMA的最后漏失量及日期, 坐标及边界.
 * 用来在地图上渲染.
 *
 * @author Abel
 */
class BwDmaLoc : BwBase() {
    /**
     * DMA-ID
     */
    var dmaId: String? = null

    /**
     * 位置的WKT数据，对应矢量坐标Point。
     * 转换矢量坐标对象，请参考Geo（C#）等相关框架。
     * @return the dmaLoc
     */
    var dmaLoc: String? = null

    /**
     * 区域WKT数据，对应矢量边界Polygon.
     * 转换矢量坐标对象，请参考Geo（C#）等相关框架。
     * @return the dmaRegion
     */
    var dmaRegion: String? = null

    /**
     * DMA名称
     */
    @get:JsonProperty
    @set:JsonIgnore
    var dmaName: String? = null

    /**
     * 分析结果详情.
     */
    var comments: String? = null

    /**
     * 最新漏失日期. 截断时间部分.
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var lastLossDate: Date? = null

    /**
     * 调整后物理漏失量 (立方米).
     * Loss in m³ after adjusted with pressure.
     */
    @get:JsonProperty
    @set:JsonIgnore
    var lastLoss: Double? = null

    /**
     * 最新漏失率 (%)
     */
    @get:JsonProperty
    @set:JsonIgnore
    var lastLossRate: Double? = null

    /**
     * ****************
     * 以下字段可忽略.
     * ****************
     *
     * 示例： 测试DMA: 32.3 m³ (2018-06-30)
     * 测试DMA: 数据不适合分析 (2018-06-30)
     * 显示为标题
     */
    @JsonIgnore
    var title: String? = null
        get() {
            val lossStr = if (lastLoss != null) String.format("%.1f", lastLoss) else ""
            val dateStr = if (lastLossDate != null) SimpleDateFormat("yyyy-MM-dd").format(lastLossDate) else ""

            return if (lastLoss == null) "${dmaName}: ${comments} (${dateStr})"
            else "${dmaName}：${lossStr} m³ (${dateStr})"
        }

    /**
     * 示例： 11112 - 测试DMA 漏损流量: 32.3 m³ / 漏损率: 37% (2018-06-30)
     * 11112 - 测试DMA 数据不适合分析 (2018-06-30)
     * 鼠标停留时显示
     */
    @JsonIgnore
    var detail: String? = null
        get() {
            val lossStr = if (lastLoss != null) String.format("%.1f", lastLoss) else ""
            val rateStr = if (lastLossRate != null) String.format("%.0f%%", lastLossRate!! * 100.0) else ""
            val dateStr = if (lastLossDate != null) SimpleDateFormat("yyyy-MM-dd").format(lastLossDate) else ""

            return if (lastLoss == null) "${dmaId} - ${dmaName} ${comments} (${dateStr})"
            else "${dmaId} - ${dmaName} 漏损量: ${lossStr} m³ / 漏损率: ${rateStr} (${dateStr})"
        }

    /**
     * 显示类型.
     * @see ShowType
     */
    var showType: String? = null

    /**
     * 显示天数.
     */
    var showDays: Int = 0

    var flowMax: Double? = null

    var flowMin: Double? = null

    var flowAvg: Double? = null

    /**
     * @return the numSeconds
     */
    var numSeconds: Long = 0

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    override fun toString(): String {
        return String.format(
                "{dmaId:%s, dmaLoc:%s, dmaRegion:%s, dmaName:%s, showType:%s, showDays:%s, lastLossDate:%s, lastLoss:%s, lastLossRate:%s, qMax:%s, qMin:%s, avgFlow:%s, numSeconds:%s, comments:%s}",
                dmaId, dmaLoc, dmaRegion, dmaName, showType, showDays, lastLossDate, lastLoss, lastLossRate,
                flowMax, flowMin, flowAvg, numSeconds, comments)
    }
}

enum class ShowType {
    /**
     * 漏控
     */
    LOSS,

    /**
     * 计量
     */
    STAT
}
