package com.abel.bigwater.model.eff

import com.abel.bigwater.model.BwBase

/**
 * 该品牌、型号、口径水表对应行度下的老化计量效率
 */
class VcEffDecay : BwBase() {
    /**
     * serial
     */
    var decayId: Long? = null

    /**
     * 水表品牌标示
     * the meterBrandId to set
     */
    var meterBrandId: String? = null

    /**
     * 水表品牌名称
     * the meterBrandId to set
     */
    var meterBrandName: String? = null

    /**
     * 口径
     * the size to set
     */
    var sizeId: Int? = null

    /**
     * 口径
     * the size to set
     */
    var sizeName: String? = null

    /**
     * 水表型号
     * the model to set
     */
    var modelSize: String? = null

    /**
     * 每100万立方米
     */
    var totalFwd: Double? = null

    /**
     * 每100万立方米计量效率衰减y%
     */
    var decayEff: Double? = null

    /**
     * can be used or not.
     */
    var deprecated: Boolean = false

    /**
     * 水表标准参数中的流量点及检定误差
     */
    var q1: Double? = null
    var q2: Double? = null
    var q3: Double? = null
    var q4: Double? = null
    var q1r: Double? = null
    var q2r: Double? = null
    var q3r: Double? = null
    var q4r: Double? = null

    /**
     * 水表测试的流量点及误差, 可空
     */
    var qs1: Double? = null
    var qs2: Double? = null
    var qs3: Double? = null
    var qs4: Double? = null
    var qs5: Double? = null
    var qs6: Double? = null
    var qs7: Double? = null
    var qs8: Double? = null
    var qs9: Double? = null
    var qs10: Double? = null
    var qs1r: Double? = null
    var qs2r: Double? = null
    var qs3r: Double? = null
    var qs4r: Double? = null
    var qs5r: Double? = null
    var qs6r: Double? = null
    var qs7r: Double? = null
    var qs8r: Double? = null
    var qs9r: Double? = null
    var qs10r: Double? = null
}