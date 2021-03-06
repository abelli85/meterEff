package com.abel.bigwater.api

import com.abel.bigwater.model.*
import com.abel.bigwater.model.eff.VcMeterVerifyPoint
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
        const val PATH_FETCH_ZONE_METER = "/fetchMeter"
        const val PATH_ADD_METER_POINT = "/addMeterPoint"
        const val PATH_REMOVE_METER_POINT = "/removeMeterPoint"
        const val PATH_UPDATE_METER_LOC = "/updateMeterLoc"

        const val PATH_LIST_DMA = "/listDma"
        const val PATH_LIST_DMA_LOC = "/listDmaLoc"
        const val PATH_INSERT_DMA = "/insertDma"
        const val PATH_FETCH_DMA = "/fetchDma"
        const val PATH_UPDATE_DMA = "/updateDma"
        const val PATH_UPDATE_DMA_LOC = "/updateDmaLoc"
        const val PATH_DELETE_DMA = "/deleteDma"

        const val PATH_LINK_DMA_METER = "/linkMeterDma"
        const val PATH_DETACH_DMA_METER = "/detachMeterDma"

        const val PATH_ATTACH_DMA_USER = "/attachDmaUser"
        const val PATH_DETACH_DMA_USER = "/detachDmaUser"
    }

    /**
     * 水表品牌列表
     */
    @GET
    @Path(PATH_LIST_METER_BRAND)
    fun selectMeterBrand(): BwResult<BwMeterBrand>

    /**
     * RTU品牌列表
     */
    @GET
    @Path(PATH_LIST_REMOTE_BRAND)
    fun selectRemoteBrand(): BwResult<BwRemoteBrand>

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
     * 增加检定点
     * holder#single holds the meter to be updated.
     * @see ZoneMeter.meterId
     * @see ZoneMeter.steelNo - 表码
     * @see ZoneMeter.verifyList - 可选
     * @see ZoneMeter.pointList
     * 其中, 如下字段需要填写：
     * @see VcMeterVerifyPoint.pointNo
     * @see VcMeterVerifyPoint.pointName
     * @see VcMeterVerifyPoint.pointFlow
     * @see VcMeterVerifyPoint.pointDev
     * @see VcMeterVerifyPoint.verifyDate
     * @see VcMeterVerifyPoint.highLimit - 可选
     * @see VcMeterVerifyPoint.lowLimit - 可选
     */
    @POST
    @Path(PATH_ADD_METER_POINT)
    fun addMeterPoint(holder: BwHolder<ZoneMeter>): BwResult<ZoneMeter>

    /**
     * 删除检定点
     * holder#single holds the meter to be updated.
     * @see ZoneMeter.verifyList
     * @see ZoneMeter.pointList
     */
    @POST
    @Path(PATH_REMOVE_METER_POINT)
    fun removeMeterPoint(holder: BwHolder<ZoneMeter>): BwResult<ZoneMeter>

    /**
     * 更新大表的坐标，设置meter#{id, meterLoc}即可。
     * meterLoc为点坐标的WKT格式，如：POINT (22.5 114)
     */
    @POST
    @Path(PATH_UPDATE_METER_LOC)
    fun updateMeterLoc(holder: BwHolder<ZoneMeter>): BwResult<ZoneMeter>

    /**
     * 大表列表.
     * 如果填充了如下字段, 则返回的水表包含DMA信息:
     * @see MeterParam.dmaId
     * @see MeterParam.dmaName
     * @see MeterParam.dmaIdList
     * 否则, 返回的水表包含片区信息(Zone)
     *
     * result#list holds the list of meters.
     */
    @POST
    @Path(PATH_LIST_ZONE_METER)
    fun listMeter(holder: BwHolder<MeterParam>): BwResult<ZoneMeter>

    /**
     * 返回单只大表的详情, 包括:
     * @see ZoneMeter.verifyList - 检定结果列表
     * @see ZoneMeter.pointList - 检定点列表
     * @see ZoneMeter.decayObj - 绑定的老化模板
     *
     * 如果填充了如下字段, 则返回的水表包含DMA信息:
     * @see MeterParam.dmaId
     * @see MeterParam.dmaName
     * @see MeterParam.dmaIdList
     * 否则, 返回的水表包含片区信息(Zone)
     *
     * result#list holds the list of meters.
     */
    @POST
    @Path(PATH_FETCH_ZONE_METER)
    fun fetchMeter(holder: BwHolder<MeterParam>): BwResult<ZoneMeter>

    /**
     * DMA列表
     */
    @POST
    @Path(PATH_LIST_DMA)
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
    @Path(PATH_LIST_DMA_LOC)
    @Deprecated("DMA包含坐标和边界")
    fun listDmaLoc(holder: BwHolder<LocParam>): BwResult<BwDmaLoc>

    /**
     * 创建DMA, 一次创建多个或一个
     * @see BwHolder.list - 一次创建多个
     * @see BwHolder.single - 一次创建一个
     * @see BwDma.meterList - 同时关联水表
     */
    @POST
    @Path(PATH_INSERT_DMA)
    fun insertDma(holder: BwHolder<BwDma>): BwResult<BwDma>

    /**
     * 获取一个DMA的详情, 必填:
     * @see MeterParam.dmaId
     * @return DMA及包含的水表
     */
    @POST
    @Path(PATH_FETCH_DMA)
    fun fetchDma(holder: BwHolder<MeterParam>): BwResult<BwDma>

    /**
     * 修改DMA, 一次修改多个或一个, 不修改关联水表
     * @see BwHolder.list - 一次修改多个
     * @see BwHolder.single - 一次修改一个
     */
    @POST
    @Path(PATH_UPDATE_DMA)
    fun updateDma(holder: BwHolder<BwDma>): BwResult<BwDma>

    /**
     * 更新DMA分区的坐标及边界，其中dmaLoc为点坐标的WKT格式，dmaRegion为多边形的WKT格式，如：
     * dmaLoc = POINT (22.5 114)
     * dmaRegion = POLYGON ((22.5 114, 23 112, 22.8 112, 22.5 114))
     */
    @POST
    @Path(PATH_UPDATE_DMA_LOC)
    @Deprecated("修改DMA时填充坐标和边界即可")
    fun updateDmaLoc(holder: BwHolder<BwDmaLoc>): BwResult<BwDmaLoc>

    /**
     * 删除DMA
     */
    @POST
    @Path(PATH_DELETE_DMA)
    fun deleteDma(holder: BwHolder<MeterParam>): BwResult<BwDma>

    /**
     * 分离DMA和账号
     * The string value holds userId.
     */
    @POST
    @Path(PATH_DETACH_DMA_USER)
    fun detachDmaUser(holder: BwHolder<BwUser>): BwResult<String>

    /**
     * 关联DMA和账号
     * holder#single#dmaIdList holds the list to be attached.
     */
    @POST
    @Path(PATH_ATTACH_DMA_USER)
    fun attachDmaUser(holder: BwHolder<BwUser>): BwResult<String>

    /**
     * 关联DMA和水表. 要关联的水表应存在, 但不存在不会返回错误, 只是不关联.
     * 覆盖相同标识的已关联水表, 增加未关联的水表(存在的水表);
     * 对于已关联其他水表 无改变. 必填:
     * @see MeterParam.dmaId
     * @see MeterParam.meterList
     */
    @POST
    @Path(PATH_LINK_DMA_METER)
    fun linkMeterDma(holder: BwHolder<MeterParam>): BwResult<BwDma>

    /**
     * 解除关联DMA和水表. 必填:
     * @see MeterParam.dmaId
     * @see MeterParam.meterIdList - 选填, 为空时解除关联的所有水表
     */
    @POST
    @Path(PATH_DETACH_DMA_METER)
    fun detachMeterDma(holder: BwHolder<MeterParam>): BwResult<BwDma>
}