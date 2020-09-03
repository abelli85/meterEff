package com.abel.bigwater.api

import com.abel.bigwater.model.eff.EffMeter
import com.abel.bigwater.model.eff.EffTask
import org.apache.dubbo.rpc.protocol.rest.support.ContentType
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("/eff")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8)
interface EffService {

    companion object {
        const val BASE_PATH = "/eff"

        const val PATH_CREATE_TASK = "/createEffTask"
        const val PATH_LIST_TASK = "/listEffTask"
        const val PATH_FETCH_TASK = "/fetchEffTask"

        const val PATH_LIST_METER_EFF = "/listMeterEff"
        const val PATH_ADD_METER_EFF = "/addMeterEff"
        const val PATH_DELETE_METER_EFF = "/deleteMeterEff"
        const val PATH_UPDATE_METER_EFF = "/updateMeterEff"

        const val PATH_FETCH_METER_EFF = "/fetchMeterEff"
        const val PATH_REPLACE_METER_EFF = "/replaceMeterEff"
    }

    /**
     * 创建计量效率分析任务
     */
    @POST
    @Path(PATH_CREATE_TASK)
    fun createEffTask(holder: BwHolder<EffTask>): BwResult<EffTask>

    /**
     * 列出计量效率分析任务
     */
    @POST
    @Path(PATH_LIST_TASK)
    fun listEffTask(holder: BwHolder<EffParam>): BwResult<EffTask>

    /**
     * 获取单个计量效率分析任务的详情
     * @see EffParam.taskId
     */
    @POST
    @Path(PATH_FETCH_TASK)
    fun fetchEffTask(holder: BwHolder<EffParam>): BwResult<EffTask>

    /**
     * 列出水表的分析结果
     */
    @POST
    @Path(PATH_LIST_METER_EFF)
    fun listMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter>

    /**
     * 添加水表的分析结果
     * @see EffParam.meterList
     */
    @POST
    @Path(PATH_ADD_METER_EFF)
    fun addMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter>

    /**
     * 删除水表的分析结果
     * @see EffParam.taskId
     * @see EffParam.meterId 或
     * @see EffParam.meterIdList
     */
    @POST
    @Path(PATH_DELETE_METER_EFF)
    fun deleteMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter>

    /**
     * 修改水表的分析结果
     * @see EffParam.taskId
     * @see EffParam.meterList
     */
    @POST
    @Path(PATH_UPDATE_METER_EFF)
    fun updateMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter>

    /**
     * 获取单个水表的分析详情
     * @see EffParam.taskId
     * @see EffParam.meterId
     */
    @POST
    @Path(PATH_FETCH_METER_EFF)
    fun fetchMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter>

    /**
     * 修改单个水表的分析详情
     * @see EffParam.taskId
     * @see EffParam.meterId
     * @see EffParam.pointEffList
     */
    @POST
    @Path(PATH_REPLACE_METER_EFF)
    fun replaceMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter>
}