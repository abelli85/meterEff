package com.abel.bigwater.model

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import java.util.*

class DataRange : BwBase() {
    /**
     * 水司标示
     */
    var firmId: String? = null

    /**
     * 片区标示
     * @return the zoneId
     */
    var zoneId: String? = null

    /**
     * 片区名称
     */
    var zoneName: String = ""

    /**
     * DMA分区标示
     */
    var dmaId: String? = null

    /**
     * the name to set
     */
    var dmaName: String? = null

    /**
     * 水表标示
     * @return the meterId
     */
    var meterId: String? = null

    /**
     * 水表名称
     * the name to set
     */
    var meterName: String? = null

    /**
     * 外来水表标示，主要用在数据中。
     */
    var extId: String? = null
        get() {
            return if (field != null) field else meterId
        }

    /**
     * 最小时标
     * @return the minTime
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var minTime: Date? = null

    /**
     * 最大时标
     * @return the maxTime
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var maxTime: Date? = null

    /** 最小时标镜像 */
    @JsonIgnore
    var minDateTime: DateTime? = null
        get() {
            field = if (minTime == null) null else DateTime(minTime)
            return field
        }
        set(value) {
            field = value
            this.minTime = value?.toDate()
        }

    /** 最大时标镜像 */
    @JsonIgnore
    var maxDateTime: DateTime? = null
        get() {
            field = if (maxTime == null) null else DateTime(maxTime)
            return field
        }
        set(value) {
            field = value
            this.maxTime = value?.toDate()
        }

    /** 最小时标镜像 */
    @JsonIgnore
    var minLocalDateTime: LocalDateTime? = null
        get() {
            field = if (minTime == null) null else LocalDateTime(minTime)
            return field
        }
        set(value) {
            field = value
            this.minTime = value?.toDate()
        }

    /** 最大时标镜像 */
    @JsonIgnore
    var maxLocalDateTime: LocalDateTime? = null
        get() {
            field = if (maxTime == null) null else LocalDateTime(maxTime)
            return field
        }
        set(value) {
            field = value
            this.maxTime = value?.toDate()
        }

    /**
     * 引用的数据行数
     */
    var dataRows: Int = 0

    /**
     * 最小数据标识
     */
    var minDataId: Long? = null

    /**
     * 最大数据标识
     */
    var maxDataId: Long? = null

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    override fun toString(): String {
        return String.format("{zoneId:%s, meterId:%s, minTime:%s, maxTime:%s}", zoneId, meterId,
                minLocalDateTime, maxLocalDateTime)
    }
}
