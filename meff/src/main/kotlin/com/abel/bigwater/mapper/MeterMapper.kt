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

    fun updateMeterBatch(dp: MeterParam): Int

    fun updateMeterDecay(mp: MeterParam): Int

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
    fun selectMeter(mp: MeterParam): List<ZoneMeter>

    fun selectMeterZone(mp: MeterParam): List<ZoneMeter>

    /**
     * 列出DMA关联的水表
     */
    fun listDmaMeter(mp: MeterParam): List<ZoneMeter>

    fun listMeterVerify(mp: MeterParam): List<VcMeterVerify>

    /**
     * 必须为同一只水表的检定结果. 必填:
     * @see ZoneMeter.steelNo
     */
    fun insertMeterVerify(mp: ZoneMeter): Int

    fun deleteMeterVerify(mp: MeterParam): Int

    fun listVerifyPoint(mp: MeterParam): List<VcMeterVerifyPoint>

    fun listVerifyPointLast(mp: MeterParam): List<VcMeterVerifyPoint>

    /**
     * 必须为同一只水表的检定点. 必填:
     * @see ZoneMeter.steelNo
     */
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

    fun rebindDmaMeter(mp: MeterParam): Int

    /**
     * <pre>
     * <parameterMap type="map" id="linkMeterDmaMap">
     * <parameter property="dmaId"></parameter>
     * <parameter property="meterIds"></parameter>
    </parameterMap> *
    </pre> *
     */
    fun linkMeterDma(mp: MeterParam): Int

    fun detachMeterDma(mp: MeterParam): Int

    fun statMeterSize(dp: MeterParam): List<MeterSizeStat>

    fun statMeterFirm(dp: MeterParam): List<MeterFirmStat>

    fun statFirmSize(dp: MeterParam): List<MeterFirmStat>

}