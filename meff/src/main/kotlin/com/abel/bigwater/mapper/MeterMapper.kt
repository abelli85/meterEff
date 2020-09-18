package com.abel.bigwater.mapper

import com.abel.bigwater.api.MeterParam
import com.abel.bigwater.model.BwDma
import com.abel.bigwater.model.BwMeterBrand
import com.abel.bigwater.model.BwRemoteBrand
import com.abel.bigwater.model.BwUser
import com.abel.bigwater.model.eff.VcMeterVerify
import com.abel.bigwater.model.eff.VcMeterVerifyPoint
import com.abel.bigwater.model.stat.MeterFirmStat
import com.abel.bigwater.model.stat.MeterSizeStat
import com.abel.bigwater.model.zone.ZoneMeter
import org.apache.ibatis.annotations.*

@Mapper
//@Component(value = "meterMapper")
interface MeterMapper {

    fun selectMeterBrand(): List<BwMeterBrand>

    fun selectRemoteBrand(): List<BwRemoteBrand>

    @Options(useGeneratedKeys = false)
    fun insertMeter(meter: ZoneMeter): Int

    fun deleteMeter(mp: MeterParam): Int

    fun updateMeter(meter: ZoneMeter): Int

    fun updateMeterLoc(meter: ZoneMeter): Int

    /**
     * <pre>
     * <parameterMap type="map" id="meterWhere">
     * <parameter property="id"></parameter>
     * <parameter property="name"></parameter>
     * <parameter property="dmaId"></parameter>
     * <parameter property="typeId"></parameter>
     * <parameter property="location"></parameter>
     * <parameter property="installDate1"></parameter>
     * <parameter property="installDate2"></parameter>
     * <parameter property="meterBrandId"></parameter>
     * <parameter property="remoteBrandId"></parameter>
     * <parameter property="firmId"></parameter>
    </parameterMap> *
     * typeId can be: CHECK/CHARGE/FIRE
    </pre> *
     *
     * @param map
     * @return
     */
    fun selectMeterDma(mp: MeterParam): List<ZoneMeter>

    fun selectMeterZone(mp: MeterParam): List<ZoneMeter>

    fun listMeterVerify(mp: MeterParam): List<VcMeterVerify>

    fun insertMeterVerify(mp: ZoneMeter): Int

    fun deleteMeterVerify(mp: MeterParam): Int

    fun listVerifyPoint(mp: MeterParam): List<VcMeterVerifyPoint>

    fun insertVerifyPoint(mp: ZoneMeter): Int

    fun deleteVerifyPoint(mp: MeterParam): Int

    fun selectMeterText(mp: MeterParam): List<ZoneMeter>

    fun selectDma(mp: MeterParam): List<BwDma>

    fun insertDma(dma: BwDma): Int

    fun updateDma(dma: BwDma): Int

    fun detachDmaUser(user: BwUser): Int

    /**
     * Attach the user to the DMA list.
     *
     * @param map
     * - userId, dmaIds - can be like "'DMA01', 'DMA02'".
     */
    fun attachDmaUser(user: BwUser): Int

    fun deleteDma(mp: MeterParam): Int

    /**
     * <pre>
     * <parameterMap type="map" id="linkMeterDmaMap">
     * <parameter property="dmaId"></parameter>
     * <parameter property="meterIds"></parameter>
    </parameterMap> *
    </pre> *
     */
    fun linkMeterDma(dma: BwDma): Int

    fun detachMeterDma(dma: BwDma): Int

    fun statMeterSize(dp: MeterParam): List<MeterSizeStat>

    fun statMeterFirm(dp: MeterParam): List<MeterFirmStat>

    fun statFirmSize(dp: MeterParam): List<MeterFirmStat>

}