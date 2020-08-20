package com.abel.bmserver.mapper

import com.abel.bigwater.api.DataParam
import com.abel.bigwater.api.MeterParam
import com.abel.bigwater.model.BwRtu
import com.abel.bigwater.model.BwRtuLog
import org.apache.ibatis.annotations.Options

interface RtuMapper {

    /**
     * <pre>
     * <parameterMap type="map" id="rtuLogMap">
     * <parameter property="logTime1"></parameter>
     * <parameter property="logTime2"></parameter>
     * <parameter property="rtuId"></parameter>
     * <parameter property="meterId"></parameter>
     * <parameter property="index"></parameter>
     * <parameter property="rows"></parameter>
     * <parameter property="firmId"></parameter>
    </parameterMap> *
    </pre> *
     *
     * @param map
     * @return
     */
    fun selectRtuLog(dp: DataParam): List<BwRtuLog>

    @Options(useGeneratedKeys = true, keyColumn = "log_id", keyProperty = "logId")
    fun addRtuLog(rlog: BwRtuLog): Int

    fun deleteRtuLog(dp: DataParam): Int

    fun selectRtuList(mp: MeterParam): List<BwRtu>

    fun addRtu(rtu: BwRtu): Int

    fun updateRtu(rtu: BwRtu): Int

    fun deleteRtu(mp: MeterParam): Int

}