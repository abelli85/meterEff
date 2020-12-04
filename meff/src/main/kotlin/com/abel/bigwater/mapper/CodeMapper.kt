package com.abel.bigwater.mapper

import com.abel.bigwater.api.CodeParam
import com.abel.bigwater.model.*
import org.apache.ibatis.annotations.Mapper

@Mapper
interface CodeMapper {
    fun listFactory(): List<VcFactory>

    fun insertFactory(fact: VcFactory): Int

    fun deleteFactory(fact: VcFactory): Int

    fun listCode(): List<VcCode>

    fun listValue(vc: VcCode): List<VcCodeValue>

    fun createValue(v: VcCodeValue): Int

    fun updateValue(v: VcCodeValue): Int

    fun updateValueOrder(p: CodeParam): Int

    fun disableValue(v: VcCodeValue): Int

    fun deleteValue(v: VcCodeValue): Int

    fun listMeterType(): List<VcMeterType>

    fun insertMeterType(mt: VcMeterType): Int

    fun deleteMeterType(mt: VcMeterType): Int

    fun listFactoryModel(fact: VcFactory): List<VcFactoryModel>

    fun insertFactoryModel(fm: VcFactoryModel): Int

    fun insertFactoryModelBatch(fm: VcFactory): Int

    fun deleteFactoryModel(fm: VcFactoryModel): Int

    fun insertWorkdayHoliday(wd: VcWorkdayHoliday): Int

    fun deleteWorkdayHoliday(wd: VcWorkdayHoliday): Int

    fun selectWorkdayHoliday(wd: VcWorkdayHoliday): List<VcWorkdayHoliday>


}