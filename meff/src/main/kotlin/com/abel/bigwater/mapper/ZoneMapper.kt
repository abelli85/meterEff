package com.abel.bigwater.mapper

import com.abel.bigwater.api.MeterParam
import com.abel.bigwater.model.zone.Zone
import com.abel.bigwater.model.zone.ZoneMeter
import org.apache.ibatis.annotations.Mapper

@Mapper
interface ZoneMapper {
    fun listZone(mp: MeterParam): List<Zone>

    fun insertZone(z: Zone): Int

    fun updateZone(z: Zone): Int

    fun deleteZone(mp: MeterParam): Int

    /**
     * 只返回分区和水表的关联, 不包含水表详情
     */
    fun listZoneMeterRel(mp: MeterParam): List<ZoneMeter>

    /**
     * rebind the zone-meter with same zoneId & meterId.
     */
    fun rebindZoneMeter(zp: MeterParam): Int

    fun attachZoneMeter(zp: MeterParam): Int

    fun detachZoneMeter(zp: MeterParam): Int
}