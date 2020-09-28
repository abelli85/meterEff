package com.abel.bigwater.mapper

import com.abel.bigwater.api.EffParam
import com.abel.bigwater.model.DataRange
import com.abel.bigwater.model.eff.EffMeter
import com.abel.bigwater.model.eff.EffMeterPoint
import com.abel.bigwater.model.eff.EffTask
import org.apache.ibatis.annotations.Mapper

@Mapper
//@Component(value = "meterMapper")
interface EffMapper {
    fun listEffTask(p: EffParam): List<EffTask>

    fun createEffTask(t: EffTask): Int

    fun deleteEffTask(p: EffParam): Int

    fun listEffMeter(p: EffParam): List<EffMeter>

    fun listEffRange(p: EffParam): List<DataRange>

    fun insertEffMeter(p: EffParam): Int

    fun updateEffMeter(p: EffParam): Int

    fun deleteEffMeter(p: EffParam): Int

    fun listEffPoint(p: EffParam): List<EffMeterPoint>

    fun insertEffPoint(p: EffParam): Int

    fun deleteEffPoint(p: EffParam): Int

}