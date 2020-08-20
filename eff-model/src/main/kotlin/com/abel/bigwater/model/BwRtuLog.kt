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
class BwRtuLog : BwBase() {

    /**
     * @return the logId
     */
    var logId: Long = 0

    /**
     * @return the logTime
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var logTime: Date? = null

    /**
     * @return the logCmd
     */
    var logCmd: String? = null

    /**
     * @return the logLen
     */
    var logLen: Int = 0

    /**
     * @return the rtuId
     */
    var rtuId: String? = null

    /**
     * @return the meterId
     */
    var meterId: String? = null

    /**
     * Maximum size: 255.
     */
    var logText: String? = null

    /**
     * Maximum size: 45.
     */
    var logResp: String? = null

    /**
     * Maximum size: 255.
     */
    var logComment: String? = null

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
     * Maximum size: 45.
     *
     * ip:port of device.
     */
    var remoteDevice: String? = null

    /**
     * Maximum size: 45.
     *
     * @return the remoteServer
     */
    var remoteServer: String? = null

    /**
     * @return the firmId
     */
    var firmId: String? = null

    /**
     * 信号强度或其他
     */
    var f18: String? = null

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    override fun toString(): String {
        return ("BwRtuLog [logId=" + logId + ", logTime=" + logTime + ", logCmd=" + logCmd + ", logLen=" + logLen
                + ", rtuId=" + rtuId + ", meterId=" + meterId + ", logText=" + logText + ", logResp=" + logResp
                + ", logComment=" + logComment + ", forwardReading=" + forwardReading + ", revertReading=" + revertReading
                + ", literPulse=" + literPulse
                + ", rssi=" + rssi + ", reverseWarn=" + reverseWarn + ", maxWarn=" + maxWarn + ", cutWarn=" + cutWarn
                + ", voltWarn=" + voltWarn + ", rssiWarn=" + rssiWarn + "]")
    }
}
