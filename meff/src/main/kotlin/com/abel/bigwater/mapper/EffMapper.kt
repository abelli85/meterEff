package com.abel.bigwater.mapper

import com.abel.bigwater.api.EffParam
import com.abel.bigwater.model.DataRange
import com.abel.bigwater.model.eff.EffMeter
import com.abel.bigwater.model.eff.EffMeterPoint
import com.abel.bigwater.model.eff.EffTask
import com.abel.bigwater.model.eff.VcEffDecay
import org.apache.ibatis.annotations.Mapper

@Mapper
//@Component(value = "meterMapper")
interface EffMapper {
    fun listEffTask(p: EffParam): List<EffTask>

    fun createEffTask(t: EffTask): Int

    fun deleteEffTask(p: EffParam): Int

    fun listEffMeter(p: EffParam): List<EffMeter>

    fun listEffFailure(p: EffParam): List<EffMeter>

    fun matchMeter(p: EffParam): List<EffMeter>

    fun buildEffMeterMonth(p: EffParam): Int

    fun buildEffMeterYear(p: EffParam): Int

    fun listEffRange(p: EffParam): List<DataRange>

    fun insertEffMeter(p: EffParam): Int

    fun insertEffMeterSingle(eff: EffMeter): Int

    fun insertEffFailureSingle(eff: EffMeter): Int

    fun updateEffMeter(p: EffParam): Int

    fun deleteEffMeter(p: EffParam): Int

    fun deleteEffFailure(p: EffParam): Int

    fun deleteEffMeterWithTask(p: EffParam): Int

    fun listEffPoint(p: EffParam): List<EffMeterPoint>

    fun statEffPointManual(p: EffParam): List<EffMeterPoint>

    fun insertEffPoint(p: EffParam): Int

    fun buildEffPointMonth(p: EffParam): Int

    fun buildEffPointYear(p: EffParam): Int

    fun deleteEffPoint(p: EffParam): Int

    fun deleteEffPointWithTask(p: EffParam): Int

    fun selectEffDecay(p: EffParam): List<VcEffDecay>

    fun insertEffDecay(p: EffParam): Int

    fun insertEffDecaySingle(decay: VcEffDecay): Int

    fun deleteEffDecay(p: EffParam): Int

    fun deleteEffDecaySingle(p: EffParam): Int
}