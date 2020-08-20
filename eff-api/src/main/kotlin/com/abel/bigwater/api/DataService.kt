package com.abel.bigwater.api

import com.abel.bigwater.model.BwData
import com.abel.bigwater.model.BwRtu
import com.abel.bigwater.model.BwRtuLog
import com.abel.bigwater.model.DataRange
import org.apache.dubbo.rpc.protocol.rest.support.ContentType
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("/data")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8)
interface DataService {

    companion object {
        const val BASE_PATH = "/data"

        /**
         * 列出历史数据.
         */
        const val PATH_LIST_REALTIME = "/listRealtime"

        /**
         * 添加历史数据.
         */
        const val PATH_ADD_REALTIME_USER = "/addRealtimeUser"

        const val PATH_DELETE_REALTIME = "/deleteRealtime"

        const val PATH_LIST_REALTIME_REVERSE = "/listRealtimeReverse"
        const val PATH_REALTIME_DATE_RANGE = "/realtimeDateRange"
        const val PATH_REALTIME_LIST_STAT = "/realtimeListStat"
    }

    /**
     * 实时数据列表
     * 填充 extId，不要填充 meterId 字段。
     * the list is employed.
     */
    @POST
    @Path(PATH_LIST_REALTIME)
    fun listRealtime(holder: BwHolder<DataParam>): BwResult<BwData>

    /**
     * 实时数据列表
     * 填充 extId，不要填充 meterId 字段。
     * the list is employed.
     */
    @POST
    @Path(PATH_LIST_REALTIME_REVERSE)
    fun listRealtimeReverse(holder: BwHolder<DataParam>): BwResult<BwData>

    /**
     * 实时数据范围.
     * 填充 extId，不要填充 meterId 字段。
     * there will be 2 rows in the list. 1st: start date-time;
     * 2nd: end date-time.
     */
    @POST
    @Path(PATH_REALTIME_DATE_RANGE)
    fun realtimeDateRange(holder: BwHolder<DataParam>): BwResult<DataRange>

    /**
     * 实时数据统计
     * The list is employed.
     */
    @POST
    @Path(PATH_REALTIME_LIST_STAT)
    fun realtimeListStat(holder: BwHolder<DataParam>): BwResult<BwData>

    /**
     * 批量增加数据, 白名单
     */
    @POST
    @Path("addRealtime")
    fun addRealtime(holder: BwHolder<BwData>): BwResult<BwData>

    /**
     * 批量增加数据, 权限控制
     */
    @POST
    @Path(PATH_ADD_REALTIME_USER)
    fun addRealtimeUser(holder: BwHolder<BwData>): BwResult<BwData>

    /**
     * 批量删除数据
     */
    @POST
    @Path(PATH_DELETE_REALTIME)
    fun deleteRealtime(holder: BwHolder<DataParam>): BwResult<BwData>

    /**
     * 获取全部水表的最后行度及其时间。
     */
    @POST
    @Path("scadaMeterList")
    fun scadaMeterList(holder: BwHolder<DataParam>): BwResult<BwData>

    /**
     * 获取分区水表的最后行度及其时间。
     * 如果zoneId为NULL或者空字符串，返回没有片区的水表的最后行度及时间。
     */
    @POST
    @Path("scadaMeterListZone")
    fun scadaMeterListZone(holder: BwHolder<DataParam>): BwResult<BwData>

    /**
     * 列举rtu-log.
     */
    @POST
    @Path("listRtuLog")
    fun listRtuLog(holder: BwHolder<DataParam>): BwResult<BwRtuLog>

    /**
     * 批量增加rtu-log.白名单
     */
    @POST
    @Path("addRtuLog")
    fun addRtuLog(holder: BwHolder<BwRtuLog>): BwResult<BwRtuLog>

    /**
     * 批量增加rtu-log.用户验证.
     */
    @POST
    @Path("addRtuLogUser")
    fun addRtuLogUser(holder: BwHolder<BwRtuLog>): BwResult<BwRtuLog>

    /**
     * 批量删除rtu-log.
     */
    @POST
    @Path("deleteRtuLog")
    fun deleteRtuLog(holder: BwHolder<DataParam>): BwResult<BwRtuLog>

    @POST
    @Path("listRtu")
    fun listRtu(holder: BwHolder<MeterParam>): BwResult<BwRtu>

    /**
     * 批量删除rtu.
     */
    @POST
    @Path("deleteRtu")
    fun deleteRtu(holder: BwHolder<MeterParam>): BwResult<BwRtu>
}