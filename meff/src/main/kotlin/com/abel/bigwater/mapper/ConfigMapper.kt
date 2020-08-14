package com.abel.bigwater.mapper

import com.abel.bigwater.model.BwConfig
import com.abel.bigwater.model.BwFirm
import org.apache.ibatis.annotations.*

@Mapper
interface ConfigMapper {

    fun configList(@Param("firmId") firmId: String?, @Param("groupId") groupId: String?, @Param("configId") configId: String?): List<BwConfig>

    fun updateConfig(config: BwConfig): Int

    fun deleteConfig(config: BwConfig): Int

    fun insertConfig(config: BwConfig): Int

    /**
     * <pre>
     * <parameterMap type="map" id="firmMap">
     * <parameter property="firmId"></parameter>
    </parameterMap> *
    </pre> *
     *
     * @param map
     * @return
     */
    fun selectFirm(@Param("firmId") firmId: String?, @Param("keywords") keywords: String? = null): List<BwFirm>

    fun addFirm(firm: BwFirm): Int

    fun deleteFirm(@Param("firmId") firmId: String): Int

    fun updateFirm(firm: BwFirm): Int
}