package com.abel.bigwater.mapper

import com.abel.bigwater.api.DataParam
import com.abel.bigwater.model.BwData
import com.abel.bigwater.model.DataRange
import org.apache.ibatis.annotations.Mapper

@Mapper
//@Component(value = "dataMapper")
interface DataMapper {

    fun insertRealtime(dp: DataParam): Int

    fun updateRealtime(data: BwData): Int

    /**
     * ORDER BY sample_time [ASC].
     *
     * <pre>
     * <parameterMap type="map" id="realtimeMap">
     * <parameter property="meterId"></parameter>
     * <parameter property="sampleTime1"></parameter>
     * <parameter property="sampleTime2"></parameter>
     * <parameter property="dmaId"></parameter>
     * <parameter property="index"></parameter>
     * <parameter property="rows"></parameter>
     * <parameter property="firmId"></parameter>
    </parameterMap> *
    </pre> *
     *
     * @param map
     * @return
     */
    fun selectRealtime(dp: DataParam): List<BwData>

    fun checkDuplicateData(dp: DataParam): List<BwData>

    /**
     * 选取水表的最后多行数据
     */
    fun selectMeterRealtime(dp: DataParam): List<BwData>

    /**
     * 选取水表的最后多行数据
     */
    fun selectMeterRealtimeReverse(dp: DataParam): List<BwData>

    /**
     * select last 1000 rows from bw_realtime_data and join with other tables.
     *
     * @param map
     * @return
     */
    fun selectRealtimeLast(dp: DataParam): List<BwData>

    /**
     * This method will retrieve realtime data hourly.
     *
     * @param map
     * @return
     */
    fun selectRealtimeHourly(mp: DataParam): List<BwData>

    /**
     * This method will retrieve realtime data hourly.
     *
     * @param map
     * @return
     */
    fun selectRealtimeHalf(mp: DataParam): List<BwData>

    /**
     * This method will retrieve realtime data hourly.
     *
     * @param map
     * @return
     */
    fun selectRealtimeQuarter(mp: DataParam): List<BwData>

    /**
     * This method querys data list for specified zone.
     * order by meterId, extId, sampleTime (all ASC)
     *
     * @param map
     * @return
     */
    fun selectZoneRealtimeHourly(dp: DataParam): List<BwData>

    /**
     * Almost same as {[.selectRealtimeHourly] but order by 'sampleTime' firstly
     * then meterId.
     *
     * @param map
     * @return
     */
    fun selectDmaRealtimeHourly(dp: DataParam): List<BwData>

    /**
     * Count the rows before deletion.
     *
     * @param map
     * @return
     */
    fun countDeleteRealtime(dp: DataParam): Int?

    fun deleteRealtime(dp: DataParam): Int

    /**
     * <pre>
     * <parameterMap type="map" id="realtimeMap">
     * <parameter property="meterId"></parameter>
     * <parameter property="sampleTime1"></parameter>
     * <parameter property="sampleTime2"></parameter>
     * <parameter property="dmaId"></parameter>
     * <parameter property="firmId"></parameter>
    </parameterMap> *
    </pre> *
     *
     * @param map
     * @return - map: minDate, maxDate
     */
    fun realtimeDateRange(dp: DataParam): List<DataRange>

    /**
     * check realtime range for specified zone.
     *
     * @param map
     * @return
     */
    fun realtimeDateRangeZone(dp: DataParam): List<DataRange>

    /**
     * Gets date range for specified DMA.
     *
     * @param map
     * - must contain key 'dmaId' but no 'meterId'.
     * @return - map: minDate, maxDate
     */
    fun realtimeDateRangeDma(dp: DataParam): List<DataRange>
}
