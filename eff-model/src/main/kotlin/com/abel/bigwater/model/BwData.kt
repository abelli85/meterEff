package com.abel.bigwater.model

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

class BwData : BwBase() {

    /** 水表编号。  */
    var meterId: String? = null
        get() {
            return if (field != null) field else extId
        }

    /**
     * 外来水表标示，主要用在数据中。
     */
    var extId: String? = null
        get() {
            return if (field != null) field else meterId
        }

    /**
     * 水表名称。
     * @param meterName
     * the meterName to set
     */
    var meterName: String? = null

    /**
     * 水表位置。
     * @param location
     * the location to set
     */
    @get:JsonProperty
    @set:JsonIgnore
    var location: String? = null

    //    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    /**
     * 采集时间
     * @param sampleTime
     * the sampleTime to set
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var sampleTime: Date? = null

    //    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    /**
     * 结束时间
     * @param endTime
     * the endTime to set
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var endTime: Date? = null

    /**
     * 时段以秒计。
     * @param durationSecond
     * the durationSecond to set
     */
    var durationSecond: Int = 0

    /**
     * 时段内正向水量，以方计。
     * Unit: m³, the sign should be +.
     */
    private var _forwardSum: Double? = null
    var forwardSum: Double?
        set(value) {
            _forwardSum = value
        }
        get() {
            checkVol()
            return _forwardSum
        }

    /**
     * Unit: m³, the sign should be +.
     */
    @JsonIgnore
    var waterMin: Double? = null

    /**
     * Unit: m³, the sign should be +.
     */
    @JsonIgnore
    var waterMax: Double? = null

    /**
     * @return the durationSecondAvg
     */
    @JsonIgnore
    var durationSecondAvg: Int = 0

    /**
     * Unit: m³, the sign should be +.
     */
    private var _revertSum: Double? = null
    var revertSum: Double?
        set(value) {
            _revertSum = value
        }
        get() {
            return _revertSum ?: 0.0
        }

    /**
     * Abel：已通过插件的系数修正单位：米。
     * 压力，Unit: KPa
     */
    var pressure: Double? = null

    /**
     * 时段内最小压力，Unit: KPa。
     * @return the pressure1Min
     */
    @JsonIgnore
    var pressureMin: Double? = null

    /**
     * 时段内最大压力，Unit: KPa。
     * @return the pressureMax
     */
    @JsonIgnore
    var pressureMax: Double? = null

    /**
     * 压力，原始读数，未计入压力系数。
     */
    var pressureDigits: Double? = null

    /**
     * 时段内最低压力，原始读数，未计入压力系数。
     * @return the pressureReadingMin
     */
    @JsonIgnore
    var pressureDigitsMin: Double? = null

    /**
     * 时段内最高压力，原始读数，未计入压力系数。
     * @return the pressureReadingMax
     */
    @JsonIgnore
    var pressureDigitsMax: Double? = null

    /**
     * 时段内平均流量Unit: m3/h.<br></br>
     * Note: This field won't be persisted to database.
     */
    @JsonIgnore
    private var _avgFlow: Double? = null
    var avgFlow: Double?
        set(value) {
            _avgFlow = value
        }
        get() {
            checkVol()
            return _avgFlow
        }

    /**
     * @return the flowMin
     */
    @JsonIgnore
    var flowMin: Double? = null

    /**
     * @return the flowMax
     */
    @JsonIgnore
    var flowMax: Double? = null

    /**
     * 底度，原始读数，未计入脉冲当量
     */
    var baseDigits: Double = 0.0

    /**
     * 行度，未计入脉冲当量，原始读数. The sign should be +.
     */
    var forwardDigits: Double? = 0.0

    /**
     * 反向行度，原始读数，未计入脉冲当量. The sign should be +.
     */
    var revertDigits: Double? = 0.0

    /**
     * 每个脉冲(最小读数)表示的水量(L), 默认 1 L/Pulse.
     */
    var literPulse: Short = 1

    /**
     * Unit: m3. water volume between 0 and q0.
     */
    var q0Sum: Double? = null
    var q1Sum: Double? = null
    var q2Sum: Double? = null
    var q3Sum: Double? = null
    var q4Sum: Double? = null

    /**
     * @return the firmId
     */
    var firmId: String? = null

    /**
     * identify if the data is updated when populating consume mode data -
     * vq0/vq1/vq2/vq3/vq4.
     */
    var flapped: Int = 0

    /** 0: IN; 1: OUT. Default to 0.  */
    var inOutput: Int = 0

    /**
     *
     * 行度，已计入脉冲当量，m³. literPulse默认1000.0 L/Pulse.
     *
     * @return
     */
    val forwardReading: Double?
        @JsonIgnore
        get() = if (literPulse < 1 || forwardDigits == null) forwardDigits
        else (forwardDigits ?: 0.0) * literPulse / 1000

    /**
     * 水表所在片区，可能为NULL，即不属于任何片区
     */
    var zoneId: String? = null

    /**
     * 位置的WKT数据，对应矢量坐标Point。
     * 转换矢量坐标对象，请参考Geo（C#）等相关框架。
     * the meterLoc to set
     */
    var meterLoc: String? = null

    /**
     * change to 0.0 if null.
     */
    fun checkVol() {
        if (_forwardSum == null) {
            _forwardSum = 0.0
        }
        if (_avgFlow == null) {
            _avgFlow = 0.0
        }
        if (_revertSum == null) {
            _revertSum = 0.0
        }

        if (durationSecond <= 0) {
            return
        }

        // if waterV1 not assigned then compute from waterQ1.
        if (_forwardSum!! - _revertSum!! > MIN_V && _avgFlow ?: 0.0 < MIN_V) {
            avgFlow = (_forwardSum!! - _revertSum!!) * 3600.0 / durationSecond
        } else if (_forwardSum ?: 0.0 <= MIN_V && _avgFlow ?: 0.0 > MIN_V) {
            _forwardSum = _avgFlow!! * durationSecond / 3600.0

            // the real V is waterV1 - revertV1.
            if (_revertSum != 0.0) {
                _forwardSum = (_forwardSum ?: 0.0) + (_revertSum ?: 0.0)
            }
        }
    }

    /**
     * 信号强度
     */
    var rssi: Int? = null

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    override fun toString(): String {
        return String.format(
                "{meterId:%s, extId:%s, meterName:%s, location:%s, sampleTime:%s, endTime:%s, durationSecond:%s, waterV1:%s (m3), revertV1:%s, pressure1:%s, pressureReading:%s," + " waterQ1:%s (m3/h), baseAll:%s (p), forwardReading:%s (p), revertReading:%s, literPulse:%s, vQ0:%s, vQ1:%s, vQ2:%s, vQ3:%s, vQ4:%s, firmId:%s, flapped:%s}",
                meterId, extId, meterName, location, sampleTime, endTime, durationSecond,
                forwardSum, revertSum, pressure, pressureDigits,
                avgFlow, baseDigits, forwardDigits, revertDigits, literPulse,
                q0Sum, q1Sum, q2Sum, q3Sum, q4Sum,
                firmId, flapped)
    }

    companion object {
        /**
         *
         */
        private val serialVersionUID = -6532101135403247475L

        val MIN_V = 1E-6
    }
}
