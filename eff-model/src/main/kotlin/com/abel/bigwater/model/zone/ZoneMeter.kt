package com.abel.bigwater.model.zone

import com.abel.bigwater.model.BwMeter
import java.io.Serializable

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
}