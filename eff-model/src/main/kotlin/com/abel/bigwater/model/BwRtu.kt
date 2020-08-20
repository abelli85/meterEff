/**
 *
 */
package com.abel.bigwater.model

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

/**
 * @author Abl
 */
class BwRtu : BwBase() {
    /**
     * @return the rtuId
     */
    var rtuId: String? = null

    /**
     * @return the meterId
     */
    var meterId: String? = null

    /** This is address of meter.  */
    var meterAddr: String? = null

    /**
     * @return the lastTime
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var lastTime: Date? = null

    /**
     * @return the lastCmd
     */
    var lastCmd: String? = null

    /**
     * @return the lastDataTime
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var lastDataTime: Date? = null

    /**
     * @return the lastText
     */
    var lastText: String? = null

    /**
     * @return the lastResp
     */
    var lastResp: String? = null

    /**
     * @return the firstTime
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var firstTime: Date? = null

    /**
     * @return the firstCmd
     */
    var firstCmd: String? = null

    /**
     * @return the firstText
     */
    var firstText: String? = null

    /**
     * @return the firstResp
     */
    var firstResp: String? = null

    /**
     * @return the meterName
     */
    var meterName: String? = null

    /**
     * 位置的WKT数据，对应矢量坐标Point。
     * 转换矢量坐标对象，请参考Geo（C#）等相关框架。
     * @return the rtuLoc
     */
    var rtuLoc: String? = null

    /**
     * 正向行度, 脉冲数.
     */
    var forwardReading: Double? = null

    /**
     * 反向行度, 脉冲数.
     */
    var revertReading: Double? = null

    /**
     * 1 L/P - 每个脉冲代表一升; 10 - 1个脉冲10升; 100, 1000.
     */
    var literPulse: Short = 1

    /**
     * @return the rssi
     */
    var rssi: Short = 0

    /**
     * @return the reverseWarn
     */
    var reverseWarn: Short = 0

    /**
     * @return the maxWarn
     */
    var maxWarn: Short = 0

    /**
     * @return the cutWarn
     */
    var cutWarn: Short = 0

    /**
     * @return the voltWarn
     */
    var voltWarn: Short = 0

    /**
     * @return the rssiWarn
     */
    var rssiWarn: Short = 0

    /**
     * @return the remoteServer
     */
    var remoteServer: String? = null

    /**
     * @return the stateDesc
     */
    var stateDesc: String? = null

    /**
     * @return the hoursSleep
     */
    var hoursSleep: Long = 0

    /**
     * @return the firmId
     */
    var firmId: String? = null

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    override fun toString(): String {
        return ("BwRtu [rtuId=" + rtuId + ", meterId=" + meterId + ", meterAddr=" + meterAddr + ", lastTime=" + lastTime
                + ", lastCmd=" + lastCmd + ", lastText=" + lastText + ", lastResp=" + lastResp + ", firstTime=" + firstTime
                + ", firstCmd=" + firstCmd + ", firstText=" + firstText + ", firstResp=" + firstResp + ", meterName="
                + meterName + ", rtuLoc=" + rtuLoc + ", forwardReading=" + forwardReading + ", revertReading=" + revertReading + ", literPulse=" + literPulse
                + ", rssi=" + rssi + ", reverseWarn=" + reverseWarn + ", maxWarn=" + maxWarn + ", cutWarn=" + cutWarn
                + ", voltWarn=" + voltWarn + ", rssiWarn=" + rssiWarn + "]")
    }
}
