package com.abel.bigwater.model

/**
 * 数据类型, 可以是: TOTAL/AVG/DELTA/REAL.
 */
enum class MeterDataType {
    /**
     * 累计值, 如累计行度, 填充:
     * @see BwData.forwardDigits
     * @see BwData.forwardReading
     */
    TOTAL,

    /**
     * 均值, 如平均流量, 填充:
     * @see BwData.avgFlow
     * @see BwData.durationSecond
     */
    AVG,

    /**
     * 区间水量, 填充:
     * @see BwData.forwardSum
     */
    DELTA,

    /**
     * 瞬时值, 如压力/瞬时流量/pH值/等, 填充:
     * @see BwData.pressure
     */
    REAL;

    companion object {
        fun findByCode(_code: String) = MeterDataType.values().firstOrNull { it.name == _code }
    }
}