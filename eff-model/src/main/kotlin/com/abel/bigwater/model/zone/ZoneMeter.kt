package com.abel.bigwater.model.zone

import com.abel.bigwater.model.BwMeter
import com.abel.bigwater.model.eff.SourceErrorType
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonIgnore

class ZoneMeter : BwMeter() {
    override fun toString(): String {
        return "{zoneId:$zoneId,meterId:$meterId,flowOut:$flowOut}"
    }

    /** 片区标示 */
    var zoneId: String? = null

    /**
     * 片区名称
     */
    var zoneName: String = ""

    /** 流入/流出标示，默认流入，即消费水量；流出~供水量 */
    var flowOut: Int = 0

    /**
     * 仅供后端批量更新使用.
     * 下级机构ID, 一般采用 {@see firmId}%, 供后台统计查询.
     */
    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    var subFirmId: String? = null

    /**
     * 示值误差来源
     */
    var srcError: String? = null

    /**
     * 示值误差来源
     */
    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    var srcErrorObj: SourceErrorType? = null
        get() = if (srcError.isNullOrBlank()) null else SourceErrorType.values().firstOrNull { it.name == srcError }
        set(value) {
            field = value
            srcError = field?.name
        }
}