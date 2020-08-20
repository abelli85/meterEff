package com.abel.bigwater.api

import com.abel.bigwater.model.*
import com.abel.bigwater.model.zone.ZoneMeter
import org.apache.dubbo.rpc.protocol.rest.support.ContentType
import javax.ws.rs.*

@Path("/meter")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8)
interface MeterService {

    companion object {
        const val BASE_PATH = "/meter"
        const val PATH_LIST_METER_BRAND = "/meterBrand"
        const val PATH_LIST_REMOTE_BRAND = "/remoteBrand"

        const val PATH_INSERT_ZONE_METER = "/insert"
        const val PATH_DELETE_ZONE_METER = "/delete"
        const val PATH_UPDATE_ZONE_METER = "/update"
        const val PATH_LIST_ZONE_METER = "/list"

        const val PATH_UPDATE_METER_LOC = "/updateMeterLoc"
    }

    /**
     * 水表品牌列表
     */
    @GET
    @Path(PATH_LIST_METER_BRAND)
    fun selectMeterBrand(): List<BwMeterBrand>

    /**
     * RTU品牌列表
     */
    @GET
    @Path(PATH_LIST_REMOTE_BRAND)
    fun selectRemoteBrand(): List<BwRemoteBrand>

    /**
     * 创建水表
     */
    @POST
    @Path(PATH_INSERT_ZONE_METER)
    fun insertMeter(holder: BwHolder<ZoneMeter>): BwResult<ZoneMeter>

    /**
     * 删除大表
     * holder#list holds list of meters.
     */
    @POST
    @Path(PATH_DELETE_ZONE_METER)
    fun deleteMeter(holder: BwHolder<ZoneMeter>): BwResult<ZoneMeter>

    /**
     * 修改大表
     * holder#single holds the meter to be updated.
     */
    @POST
    @Path(PATH_UPDATE_ZONE_METER)
    fun updateMeter(holder: BwHolder<ZoneMeter>): BwResult<ZoneMeter>

    /**
     * 更新大表的坐标，设置meter#{id, meterLoc}即可。
     * meterLoc为点坐标的WKT格式，如：POINT (22.5 114)
     */
    @POST
    @Path(PATH_UPDATE_METER_LOC)
    fun updateMeterLoc(holder: BwHolder<ZoneMeter>): BwResult<ZoneMeter>

    /**
     * 大表列表
     * result#list holds the list of meters.
     */
    @POST
    @Path(PATH_LIST_ZONE_METER)
    fun listMeter(holder: BwHolder<MeterParam>): BwResult<BwMeter>

    /**
     * DMA列表
     */
    @POST
    @Path("listDma")
    fun listDma(holder: BwHolder<MeterParam>): BwResult<BwDma>

    /**
     * 查询DMA的坐标及边界，及最后漏损.
     *
     * 注：每个DMA包含两条结果，最后一日及前一日，最后一日由于RTU上传周期的问题，也许会出现“数据不全”的结果。
     * 这种情况下，lastLoss将为null，而前一日的漏损量应该是完整的，即lastLoss不为null。注意在显示漏损地图时
     * 应尽量使用非null值：如果最后一日lastLoss != null，则使用最后一日; 否则使用前一日的 lastLoss; 如果
     * 前一日的lastLoss 也是null, 则使用comments 字段的内容。
     */
    @POST
    @Path("listDmaLoc")
    fun listDmaLoc(holder: BwHolder<LocParam>): BwResult<BwDmaLoc>

    /**
     * 创建DMA
     */
    @POST
    @Path("insertDma")
    fun insertDma(holder: BwHolder<BwDma>): BwResult<BwDma>

    /**
     * 修改DMA
     */
    @POST
    @Path("updateDma")
    fun updateDma(holder: BwHolder<BwDma>): BwResult<BwDma>

    /**
     * 更新DMA分区的坐标及边界，其中dmaLoc为点坐标的WKT格式，dmaRegion为多边形的WKT格式，如：
     * dmaLoc = POINT (22.5 114)
     * dmaRegion = POLYGON ((22.5 114, 23 112, 22.8 112, 22.5 114))
     */
    @POST
    @Path("updateDmaLoc")
    fun updateDmaLoc(holder: BwHolder<BwDmaLoc>): BwResult<BwDmaLoc>

    /**
     * 删除DMA
     */
    @POST
    @Path("deleteDma")
    fun deleteDma(holder: BwHolder<MeterParam>): BwResult<BwDma>

    /**
     * 分离DMA和账号
     * The string value holds userId.
     */
    @POST
    @Path("detachDmaUser")
    fun detachDmaUser(holder: BwHolder<BwUser>): BwResult<String>

    /**
     * 关联DMA和账号
     * holder#single#dmaIdList holds the list to be attached.
     */
    @POST
    @Path("attachDmaUser")
    fun attachDmaUser(holder: BwHolder<BwUser>): BwResult<String>

    /**
     * 关联DMA和大表
     * holder#single#dmaId - meterIdList
     */
    @POST
    @Path("linkMeterDma")
    fun linkMeterDma(holder: BwHolder<MeterParam>): BwResult<String>

    /**
     * 分离DMA和大表
     * holder#single#dmaId - meterIdList
     */
    @POST
    @Path("detachMeterDma")
    fun detachMeterDma(holder: BwHolder<MeterParam>): BwResult<String>
}