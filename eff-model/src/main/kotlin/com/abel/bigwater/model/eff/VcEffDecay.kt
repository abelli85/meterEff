package com.abel.bigwater.model.eff

import com.abel.bigwater.model.BwBase

/**
 * 该品牌、型号、口径水表对应行度下的老化计量效率
 */
class VcEffDecay : BwBase() {
    /**
     * serial
     */
    var wid: Long? = null

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
}