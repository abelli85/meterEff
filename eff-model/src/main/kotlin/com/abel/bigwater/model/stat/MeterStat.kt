package com.abel.bigwater.model.stat

import com.abel.bigwater.model.BwBase

class MeterStat : BwBase() {
    /**
     * 水表口径统计
     */
    var sizeList: List<MeterSizeStat>? = null

    /**
     * 机构水表统计
     */
    var firmList: List<MeterFirmStat>? = null
}