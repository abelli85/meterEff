package com.abel.bigwater.mapper

import com.abel.bigwater.api.MeterParam
import com.abel.bigwater.model.zone.Zone
import org.apache.ibatis.annotations.Mapper

@Mapper
interface ZoneMapper {
    fun listZone(mp: MeterParam): List<Zone>

    fun insertZone(z: Zone): Int

    fun updateZone(z: Zone): Int

    fun deleteZone(mp: MeterParam): Int

    fun attachZoneMeter(z: Zone): Int

    fun detachZoneMeter(z: Zone): Int
}