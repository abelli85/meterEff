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

        const val PATH_LIST_TASK = "/listEffTask"

        const val PATH_FETCH_TASK = "/fetchEffTask"

        const val PATH_LIST_METER_EFF = "/listMeterEff"
    }

    /**
     * 列出计量效率分析任务
     */
    @POST
    @Path(PATH_LIST_TASK)
    fun listEffTask(holder: BwHolder<EffParam>): BwResult<EffTask>

    /**
     * 获取单个计量效率分析任务的详情
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
     * 获取单个水表的分析详情
     */
    @POST
    @Path(PATH_LIST_METER_EFF)
    fun fetchMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter>
}