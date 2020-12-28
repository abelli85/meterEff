package com.abel.bigwater.impl

import com.abel.bigwater.api.EffParam
import com.abel.bigwater.mapper.EffMapper
import com.abel.bigwater.model.PowerType
import com.abel.bigwater.model.eff.EffMeter
import com.abel.bigwater.model.stat.StdErrHelper
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class StdValidatorImpl : StdValidator {
    @Autowired
    var effMapper: EffMapper? = null

    /**
     * 超出2倍均值或3倍方差
     * 日水量可统计, 则写入日水量表, 否则写入失败表
     */
    override fun validate(eff: EffMeter): StdErrHelper? {
        if (eff.powerTypeObj == PowerType.MANUAL || eff.taskStart == null) {
            return null
        }

        val dlist = effMapper!!.listEffMeter(EffParam().apply {
            meterId = eff.meterId
            taskStart = DateTime(eff.taskStart).minusDays(10).toDate()
            taskEnd = DateTime(eff.taskStart).minusDays(1).toDate()
        })

        dlist.filter { it.meterWater ?: 0.0 > 1.0E-3 && it.stdDays ?: 0.0 > 0.5 }.also { vlist ->
            if (vlist.size < 7) return null
            return StdErrHelper(vlist.map { it.meterWater!!.div(it.stdDays!!) })
        }
    }
}