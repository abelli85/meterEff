package com.abel.bigwater.api

import com.abel.bigwater.model.BwDma
import io.swagger.annotations.ApiOperation
import org.apache.dubbo.rpc.protocol.rest.support.ContentType
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces

/**
 * DMA.
 */
@Path("/dma")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8)
@ApiOperation("dmaService")
interface DmaService {
    companion object {
        const val BASE_PATH = "/dma"
        const val PATH_LIST_DMA = "/list"
        const val PATH_FETCH_DMA = "/fetch"
        const val PATH_UPDATE_DMA = "/update"
        const val PATH_DELETE_DMA = "/delete"
        const val PATH_ATTACH_DMA_METER = "/attachDmaMeter"
        const val PATH_DETACH_DMA_METER = "/detachDmaMeter"
    }

    /**
     * 列出DMA, 不包含水表
     */
    @POST
    @Path(PATH_LIST_DMA)
    fun listDma(mp: BwHolder<MeterParam>): BwResult<BwDma>

    /**
     * 获取一个DMA的详情
     */
    @POST
    @Path(PATH_FETCH_DMA)
    fun fetchDma(mp: BwHolder<MeterParam>): BwResult<BwDma>

    /**
     * 更新一个DMA, 包含其内部的总分表
     */
    @POST
    @Path(PATH_UPDATE_DMA)
    fun updateDma(mp: BwHolder<BwDma>): BwResult<BwDma>

    /**
     * 删除一个DMA, 并解除DMA跟水表的关联, 但不删除关联水表
     */
    @POST
    @Path(PATH_DELETE_DMA)
    fun deleteDma(mp: BwHolder<BwDma>): BwResult<BwDma>

    /**
     * 关联DMA和水表
     */
    @POST
    @Path(PATH_ATTACH_DMA_METER)
    fun attachDmaMeter(mp: BwHolder<BwDma>): BwResult<BwDma>

    /**
     * 解除关联DMA和水表
     */
    @POST
    @Path(PATH_DETACH_DMA_METER)
    fun detachDmaMeter(mp: BwHolder<BwDma>): BwResult<BwDma>
}