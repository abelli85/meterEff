package com.abel.bigwater.impl

import com.abel.bigwater.model.eff.EffMeter
import com.abel.bigwater.model.stat.StdErrHelper

interface StdValidator {
    /**
     * 超出2倍均值或3倍方差
     * 日水量可统计, 则写入日水量表, 否则写入失败表
     */
    fun validate(eff: EffMeter): StdErrHelper?
}
