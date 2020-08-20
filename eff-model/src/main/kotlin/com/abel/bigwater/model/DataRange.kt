package com.abel.bigwater.model

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.joda.time.DateTime
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

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    override fun toString(): String {
        return String.format("{zoneId:%s, meterId:%s, minTime:%s, maxTime:%s}", zoneId, meterId, minTime, maxTime)
    }
}
