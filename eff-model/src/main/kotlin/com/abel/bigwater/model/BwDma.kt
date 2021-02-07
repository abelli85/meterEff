package com.abel.bigwater.model

import com.abel.bigwater.model.zone.ZoneMeter
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.locationtech.jts.io.WKTReader
import java.util.*

enum class DmaType {
    RESIDENT,
    INDUSTRY
}

class BwDma : BwBase() {
    /**
     * the id to set
     */
    var dmaId: String? = null

    /**
     * the name to set
     */
    var dmaName: String? = null

    /**
     * 用来在combox/listbox中显示为标题。
     */
    var title: String? = ""
        get() = "${dmaId} - ${dmaName}"

    /**
     * the location to set
     */
    var location: String? = null

    /**
     * 位置的WKT数据，对应矢量坐标Point。
     * 转换矢量坐标对象，请参考Geo（C#）等相关框架。
     * @return the dmaLoc
     */
    var dmaLoc: String? = null
        set(value) {
            field = if (!value.isNullOrBlank()) {
                try {
                    val g = WKTReader().read(value)
                    if ("Point".equals(g.geometryType, true)) {
                        value
                    } else null
                } catch (t: Throwable) {
                    null
                }
            } else null
        }

    /**
     * 区域WKT数据，对应矢量边界Polygon.
     * 转换矢量坐标对象，请参考Geo（C#）等相关框架。
     * @return the dmaRegion
     */
    var dmaRegion: String? = null
        set(value) {
            field = if (!value.isNullOrBlank()) {
                try {
                    val g = WKTReader().read(value)
                    if ("Polygon".equals(g.geometryType, true)) {
                        value
                    } else null
                } catch (t: Throwable) {
                    null
                }
            } else null
        }

    /**
     * the r to set
     */
    var reff: Double = 0.0

    /**
     * the count to set
     */
    var meterCount: Int = 0

    /**
     * the userId to set
     */
    var userId: String? = null

    /**
     * the firmId to set
     */
    var firmId: String? = null

    /**
     * sum of monthly user consumption, in m3.
     */
    var sumUser: Double? = null

    /**
     * dma type. RESIDENT can employ loss-model.
     * INDUSTRY should employ LNF-model.
     * default to RESIDENT, never-null.
     */
    var dmaType: String? = null
        set(v1) {
            field = v1
        }
        get() {
            if (field == null) field = DmaType.RESIDENT.toString()
            return field
        }

    /**
     * @param showType
     * the showType to set
     */
    var showType: String? = null
        get() = if (field.isNullOrBlank()) ShowType.LOSS.name else field

    /** legal lowest-night-flow. */
    var legalLnf: Double? = null

    /**
     * 分区状态，参考{@link MeterStatus}.
     */
    var status: String? = null

    @JsonIgnore
    @JSONField(serialize = false)
    var statusObj: MeterStatus? = null
        set(value) {
            field = value ?: MeterStatus.WORK
            status = field?.name
        }

    /// <summary>
    /// 分表数量
    /// </summary>
    var cntUser: Int? = null

    /// <summary>
    /// 分表月用水均值
    /// </summary>
    var avgMonthWater: Double? = null

    /// <summary>
    /// 用户用水方差
    /// </summary>
    var devMonthWater: Double? = null

    /**
     * 售水量样本数量
     */
    var cntMonth: Int? = null

    /// <summary>
    /// 夜间小流均值(方/时)
    /// </summary>
    var avgMnfTotal: Double? = null

    /// <summary>
    /// 夜间小流均值
    /// </summary>
    var devMnfTotal: Double? = null

    /**
     * MNF样本数
     */
    var cntMnf: Int? = null

    /**
     * 上线时间
     */
    @JSONField(serialize = true, deserialize = true, format = JsonHelper.FULL_DATE_FORMAT)
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    var onlineDate: Date? = null

    /**
     * 备注
     */
    var memo: String? = null

    /**
     * 水表列表, 仅在获取单个DMA分区时填充
     */
    var meterList: List<ZoneMeter>? = null

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    override fun toString(): String {
        return String.format("{id:%s, name:%s, r:%s, count:%s}", dmaId, dmaName, reff, meterCount)
    }

    override fun hashCode(): Int {
        return dmaId?.hashCode() ?: 0
    }

    override fun equals(other: Any?): Boolean {
        return dmaId == (other as BwDma?)?.dmaId
    }

    companion object {
        /**
         *
         */
        private val serialVersionUID = 2692238395751732966L

        /**
         * coefficient of flow, 4~8.
         */
        const val R_DEFAULT = 7.0

        /**
         * R default to 7.0D. It must be between 1.0D and 10.0D.
         */
        const val R_MIN = 2.0

        /**
         * R default to 7.0D. It must be between 1.0D and 10.0D.
         */
        const val R_MAX = 8.0
    }

}
