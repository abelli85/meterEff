package com.abel.bigwater.api

import com.abel.bigwater.model.*
import org.apache.dubbo.rpc.protocol.rest.support.ContentType
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces

/**
 * 代码接口
 */
@Path(CodeService.URL_BASE)
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8)
interface CodeService {

    companion object {
        const val URL_BASE = "/code"

        const val KEY_START_WORK_TIME = "START_WORK_TIME"
        const val KEY_END_MORNING_TIME = "END_MORNING_TIME"
        const val KEY_START_AFTERNOON_TIME = "START_AFTERNOON_TIME"
        const val KEY_END_WORK_TIME = "END_WORK_TIME"

        const val PATH_INSERT_WORKDAY_HOLIDAY = "/insertWorkdayHoliday"
        const val PATH_DELETE_WORKDAY_HOLIDAY = "/deleteWorkdayHoliday"
        const val PATH_SELECT_WORKDAY_HOLIDAY = "/selectWorkdayHoliday"

        const val PATH_CREATE_VALUE = "/createValue"
        const val PATH_UPDATE_VALUE = "/updateValue"
        const val PATH_DELETE_VALUE = "/deleteValue"

        const val PATH_LIST_FACTORY = "/listFactory"
        const val PATH_INSERT_FACTORY = "/insertFactory"
        const val PATH_DELETE_FACTORY = "/deleteFactory"

        const val PATH_LIST_METER_TYPE = "/listMeterType"
        const val PATH_INSERT_METER_TYPE = "/insertMeterType"
        const val PATH_DELETE_METER_TYPE = "/deleteMeterType"

        const val PATH_LIST_FACTORY_MODEL = "/listFactoryModel"
        const val PATH_INSERT_FACTORY_MODEL = "/insertFactoryModel"
        const val PATH_DELETE_FACTORY_MODEL = "/deleteFactoryModel"

        const val PATH_LIST_VERIFY_PLATFORM = "/listVerifyPlatform"
        const val PATH_ADD_VERIFY_PLATFORM = "/addVerifyPlatform"
        const val PATH_REMOVE_VERIFY_PLATFORM = "/removeVerifyPlatform"

        /**
         * 默认列表
         */
        val CONFIG_LIST = listOf<BwConfig>(
                BwConfig().apply {
                    configId = KEY_START_WORK_TIME
                    configName = "上班时间"
                    configType = "时间"
                    value = "8:00"
                },
                BwConfig().apply {
                    configId = KEY_END_WORK_TIME
                    configName = "下班时间"
                    configType = "时间"
                    value = "18:00"
                })
    }

    /**
     * 列出所有代码类
     */
    @POST
    @Path("listCode")
    fun listCode(holder: BwHolder<VcCode>): BwResult<VcCode>

    /**
     * 列出指定代码的值列表
     */
    @POST
    @Path("listValue")
    fun listValue(holder: BwHolder<VcCode>): BwResult<VcCodeValue>

    /**
     * 创建代码值, 代码存放在{holder.list}.
     */
    @POST
    @Path("createValue")
    fun createValue(holder: BwHolder<VcCodeValue>): BwResult<VcCodeValue>

    /**
     * 修改代码值, 代码值:
     * @see BwHolder.list
     */
    @POST
    @Path("updateValue")
    fun updateValue(holder: BwHolder<VcCodeValue>): BwResult<VcCodeValue>

    /**
     * 删除代码值
     */
    @POST
    @Path("deleteValue")
    fun deleteValue(holder: BwHolder<VcCodeValue>): BwResult<VcCodeValue>

    /**
     * 所有厂商
     */
    @POST
    @Path(PATH_LIST_FACTORY)
    fun listFactory(holder: BwHolder<VcFactory>): BwResult<VcFactory>

    /**
     * 新增厂商
     */
    @POST
    @Path(PATH_INSERT_FACTORY)
    fun insertFactory(holder: BwHolder<VcFactory>): BwResult<VcFactory>

    /**
     * 移除厂商
     */
    @POST
    @Path(PATH_DELETE_FACTORY)
    fun deleteFactory(holder: BwHolder<VcFactory>): BwResult<VcFactory>

    /**
     * 水表类型
     */
    @POST
    @Path(PATH_LIST_METER_TYPE)
    fun listMeterType(holder: BwHolder<VcMeterType>): BwResult<VcMeterType>

    /**
     * 新增水表类型
     */
    @POST
    @Path(PATH_INSERT_METER_TYPE)
    fun insertMeterType(holder: BwHolder<VcMeterType>): BwResult<VcMeterType>

    /**
     * 移除水表类型
     */
    @POST
    @Path(PATH_DELETE_METER_TYPE)
    fun deleteMeterType(holder: BwHolder<VcMeterType>): BwResult<VcMeterType>

    /**
     * 厂家的水表型号
     */
    @POST
    @Path(PATH_LIST_FACTORY_MODEL)
    fun listFactoryModel(holder: BwHolder<VcFactory>): BwResult<VcFactoryModel>

    /**
     * 新增厂家的水表型号
     */
    @POST
    @Path(PATH_INSERT_FACTORY_MODEL)
    fun insertFactoryModel(holder: BwHolder<VcFactoryModel>): BwResult<VcFactoryModel>

    /**
     * 移除厂家的水表型号
     */
    @POST
    @Path(PATH_DELETE_FACTORY_MODEL)
    fun deleteFactoryModel(holder: BwHolder<VcFactoryModel>): BwResult<VcFactoryModel>

    /**
     * 列出系统配置项
     */
    @POST
    @Path("configList")
    fun configList(holder: BwHolder<UserOperParam>): BwResult<BwConfig>

    /**
     * 更改系统配置项
     */
    @POST
    @Path("updateConfig")
    fun updateConfig(holder: BwHolder<BwConfig>): BwResult<BwConfig>

    /**
     * 插入工作日/节假日
     */
    @POST
    @Path(PATH_INSERT_WORKDAY_HOLIDAY)
    fun insertWorkdayHoliday(holder: BwHolder<VcWorkdayHoliday>): BwResult<VcWorkdayHoliday>

    /**
     * 删除工作日/节假日
     */
    @POST
    @Path(PATH_DELETE_WORKDAY_HOLIDAY)
    fun deleteWorkdayHoliday(holder: BwHolder<VcWorkdayHoliday>): BwResult<VcWorkdayHoliday>

    /**
     * 列出工作日/节假日
     */
    @POST
    @Path(PATH_SELECT_WORKDAY_HOLIDAY)
    fun selectWorkdayHoliday(holder: BwHolder<VcWorkdayHoliday>): BwResult<VcWorkdayHoliday>

}