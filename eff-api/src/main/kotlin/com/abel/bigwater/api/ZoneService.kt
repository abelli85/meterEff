package com.abel.bigwater.api

import com.abel.bigwater.model.zone.Zone
import io.swagger.annotations.ApiOperation
import org.apache.dubbo.rpc.protocol.rest.support.ContentType
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces

/**
 * 分区, 包括一级分区、二级分区等, 不同于DMA.
 */
@Path("/zone")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8)
@ApiOperation("zoneService")
interface ZoneService {
    companion object {
        const val BASE_PATH = "/zone"

        const val PATH_LIST_ZONE = "/list"
        const val PATH_SAVE_ZONE = "/save"
        const val PATH_FETCH_ZONE = "/fetch"
        const val PATH_UPDATE_ZONE = "/update"
        const val PATH_DELETE_ZONE = "/delete"
        const val PATH_ATTACH_ZONE_METER = "/saveZoneMeter"
        const val PATH_DETACH_ZONE_METER = "/deleteZoneMeter"
    }

    /**
     * 列出分区, 不包含水表
     */
    @POST
    @Path(PATH_LIST_ZONE)
    fun listZone(holder: BwHolder<MeterParam>) : BwResult<Zone>

    /**
     * 创建分区, 包含其内部的总分表
     */
    @POST
    @Path(PATH_SAVE_ZONE)
    fun saveZone(holder: BwHolder<Zone>) : BwResult<Zone>

    /**
     * 获取一个分区的详情, 必填:
     * @see MeterParam.zoneId
     * @return 分区及包含的水表
     */
    @POST
    @Path(PATH_FETCH_ZONE)
    fun fetchZone(holder: BwHolder<MeterParam>): BwResult<Zone>

    /**
     * 更新一个分区, 包含其内部的总分表
     */
    @POST
    @Path(PATH_UPDATE_ZONE)
    fun updateZone(holder: BwHolder<Zone>): BwResult<Zone>

    /**
     * 删除一个分区, 并解除分区跟水表的关联, 但不删除关联水表
     */
    @POST
    @Path(PATH_DELETE_ZONE)
    fun deleteZone(holder: BwHolder<Zone>): BwResult<Zone>

    /**
     * 关联分区和水表. 要关联的水表应存在, 但不存在不会返回错误, 只是不关联.
     * 覆盖相同标识的已关联水表, 增加未关联的水表(存在的水表);
     * 对于已关联其他水表 无改变. 必填:
     * @see Zone.zoneId
     * @see Zone.meterList
     */
    @POST
    @Path(PATH_ATTACH_ZONE_METER)
    fun saveZoneMeter(holder: BwHolder<Zone>): BwResult<Zone>

    /**
     * 解除关联分区和水表. 必填:
     * @see Zone.zoneId
     * @see Zone.meterList - 选填, 为空时解除关联的所有水表
     */
    @POST
    @Path(PATH_DETACH_ZONE_METER)
    fun deleteZoneMeter(holder: BwHolder<Zone>): BwResult<Zone>
}