package com.abel.bigwater.impl

import com.abel.bigwater.api.*
import com.abel.bigwater.mapper.DataMapper
import com.abel.bigwater.model.*
import com.alibaba.fastjson.JSON
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service("dataService")
class DataServiceImpl : DataService {

    @Autowired
    private var loginManager: LoginManager? = null

    @Autowired
    private var dataMapper: DataMapper? = null

    /**
     * 实时数据列表
     * 填充 extId，不要填充 meterId 字段。
     * the list is employed.
     */
    override fun listRealtime(holder: BwHolder<DataParam>): BwResult<BwData> {
        if (holder.lr?.sessionId.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        val dp = holder.single!!
        lgr.info("${holder.lr?.userId} try to list realtime: ${JSON.toJSONString(dp)}")

        val rightName = DataService.BASE_PATH + DataService.PATH_LIST_REALTIME
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            if (!dp.firmId.orEmpty().startsWith(login.single!!.firmId!!)) {
                dp.firmId = login.single!!.firmId!!.plus("%")
            }

            // last 3 days or not
            if (dp.lastDays ?: 0 > 0) {
                val drange = dataMapper!!.realtimeDateRange(dp)
                val lastTime = drange.filter { it.maxTime != null }.maxOfOrNull { it.maxTime!! }
                if (lastTime != null) {
                    dp.also {
                        it.sampleTime2 = lastTime
                        it.sampleTime1 = DateTime(lastTime).minusDays(3).toDate()
                    }
                }
            }

            val list = when (dp.duration) {
                DataDuration.DURATION_DAY ->
                    dataMapper!!.selectRealtimeDay(dp)

                DataDuration.DURATION_MONTH ->
                    dataMapper!!.selectRealtimeMonth(dp)

                DataDuration.DURATION_0 ->
                    dataMapper!!.selectRealtime(dp)

                DataDuration.DURATION_30_MIN ->
                    dataMapper!!.selectRealtimeHalf(dp)

                DataDuration.DURATION_15_MIN ->
                    dataMapper!!.selectRealtimeQuarter(dp)

                // DataDuration.DURATION_60_MIN
                else ->
                    dataMapper!!.selectRealtimeHourly(dp)
            }

            return BwResult(list).apply {
                error = "数据条数： ${list.size}"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 实时数据列表
     * 填充 extId，不要填充 meterId 字段。
     * the list is employed.
     */
    override fun listRealtimeReverse(holder: BwHolder<DataParam>): BwResult<BwData> {
        TODO("Not yet implemented")
    }

    /**
     * 实时数据范围.
     * 填充 extId可获得更好的性能, 也可填充 meterId 字段.
     * there will be 2 rows in the list. 1st: start date-time;
     * 2nd: end date-time.
     */
    override fun realtimeDateRange(holder: BwHolder<DataParam>): BwResult<DataRange> {
        if (holder.lr?.sessionId.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        val dp = holder.single!!
        lgr.info("${holder.lr?.userId} try to list data range: ${JSON.toJSONString(dp)}")

        val rightName = DataService.BASE_PATH + DataService.PATH_REALTIME_DATE_RANGE
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            val list = dataMapper!!.realtimeDateRange(dp.refineFirmId<DataParam>(login.single!!))
            return BwResult(list).apply {
                error = "数据条数： ${list.size}"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 实时数据统计
     * The list is employed.
     */
    override fun realtimeListStat(holder: BwHolder<DataParam>): BwResult<BwData> {
        TODO("Not yet implemented")
    }

    /**
     * 批量增加数据, 白名单
     */
    override fun addRealtime(holder: BwHolder<BwData>): BwResult<BwData> {
        TODO("Not yet implemented")
    }

    /**
     * 批量增加数据, 权限控制
     */
    override fun addRealtimeUser(holder: BwHolder<BwData>): BwResult<BwData> {
        if (holder.lr?.sessionId.isNullOrBlank() || (
                        holder.single?.extId.isNullOrBlank()
                                && holder.list.isNullOrEmpty())) {
            return BwResult(2, ERR_PARAM)
        }

        var list = if (holder.single?.extId.isNullOrBlank())
            holder.list.orEmpty()
        else
            holder.list.orEmpty().plus(holder.single!!)
        list.forEach {
            if (it.extId.isNullOrBlank() || it.sampleTime == null) {
                return BwResult(2, "通道/时间不能为空: ${it.meterId}")
            }
        }

        lgr.info("${holder.lr?.userId} try to add data: ${JSON.toJSONString(holder.single)}")

        var cnt = 0
        val rightName = DataService.BASE_PATH + DataService.PATH_ADD_REALTIME_USER
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            do {
                list.take(100).also { subList ->
                    // 只能更新 所在机构及分公司
                    subList.forEach {
                        if (!it.firmId.orEmpty().startsWith(login.single!!.firmId!!)) {
                            it.firmId = login.single!!.firmId
                        }
                    }

                    dataMapper!!.checkDuplicateData(DataParam().apply {
                        dataList = subList
                    }).also {
                        if (it.isNotEmpty()) {
                            return BwResult(2, "数据重复: ${it.joinToString { d1 -> LocalDateTime(d1.sampleTime).toString() }}")
                        }
                    }

                    cnt += dataMapper!!.insertRealtime(DataParam().apply {
                        dataList = subList
                    })
                }

                list = list.drop(100)
            } while (list.isNotEmpty())

            return BwResult(0, "增加数据： ${cnt}")
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "增加数据: $cnt, 内部错误: ${ex.message}")
        }
    }

    /**
     * 批量删除数据
     */
    override fun deleteRealtime(holder: BwHolder<DataParam>): BwResult<BwData> {
        if (holder.lr?.sessionId.isNullOrBlank() || (
                        holder.single?.extId.isNullOrBlank()
                                && holder.list.isNullOrEmpty())) {
            return BwResult(2, ERR_PARAM)
        }

        val list = if (holder.single?.extId.isNullOrBlank())
            holder.list.orEmpty()
        else
            holder.list.orEmpty().plus(holder.single!!)
        list.forEach {
            if (it.extId.isNullOrBlank() || it.sampleTime == null) {
                return BwResult(2, "通道/时间不能为空: ${it.meterId}")
            }
        }

        lgr.info("${holder.lr?.userId} try to delete data: ${JSON.toJSONString(holder.single)}")

        val rightName = DataService.BASE_PATH + DataService.PATH_DELETE_REALTIME
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            list.forEach {
                // 只能更新 所在机构及分公司
                if (!it.firmId.orEmpty().startsWith(login.single!!.firmId!!)) {
                    it.firmId = login.single!!.firmId
                }

                dataMapper!!.deleteRealtime(it)
            }

            return BwResult(0, "删除数据： ${list.size}")
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 获取全部水表的最后行度及其时间。
     */
    override fun scadaMeterList(holder: BwHolder<DataParam>): BwResult<BwData> {
        TODO("Not yet implemented")
    }

    /**
     * 获取分区水表的最后行度及其时间。
     * 如果zoneId为NULL或者空字符串，返回没有片区的水表的最后行度及时间。
     */
    override fun scadaMeterListZone(holder: BwHolder<DataParam>): BwResult<BwData> {
        TODO("Not yet implemented")
    }

    /**
     * 列出水表的数据的日条数. 填写水表ID/列表、数据标识/列表、时段, 否则可能响应时间缓慢,
     * 或数据量较大导致返回不全.
     * 如下字段可选:
     * @see DataParam.meterId 或
     * @see DataParam.meterIdList
     * @see DataParam.extId 或
     * @see DataParam.extIdList
     *
     * @see DataParam.sampleTime1
     * @see DataParam.sampleTime2
     */
    override fun listMeterDayCount(holder: BwHolder<DataParam>): BwResult<MeterDayDataCount> {
        if (holder.lr?.sessionId.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        val dp = holder.single!!
        lgr.info("${holder.lr?.userId} try to list realtime: ${JSON.toJSONString(dp)}")

        val rightName = DataService.BASE_PATH + DataService.PATH_LIST_REALTIME
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            val list = dataMapper!!.listMeterDayCount(dp.refineFirmId<DataParam>(login.single!!))

            return BwResult(list).apply {
                error = "数据条数： ${list.size}"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 列出水表的数据的小时条数. 填写水表ID/列表、数据标识/列表、时段, 否则可能响应时间缓慢,
     * 或数据量较大导致返回不全.
     * 如下字段可选:
     * @see DataParam.meterId 或
     * @see DataParam.meterIdList
     * @see DataParam.extId 或
     * @see DataParam.extIdList
     *
     * @see DataParam.sampleTime1
     * @see DataParam.sampleTime2
     */
    override fun listMeterHourCount(holder: BwHolder<DataParam>): BwResult<MeterHourDataCount> {
        if (holder.lr?.sessionId.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        val dp = holder.single!!
        lgr.info("${holder.lr?.userId} try to list realtime: ${JSON.toJSONString(dp)}")

        val rightName = DataService.BASE_PATH + DataService.PATH_LIST_REALTIME
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            if (!dp.firmId.orEmpty().startsWith(login.single!!.firmId!!)) {
                dp.firmId = login.single!!.firmId!!.plus("%")
            }
            val list = dataMapper!!.listMeterHourCount(dp)

            return BwResult(list).apply {
                error = "数据条数： ${list.size}"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 列举rtu-log.
     */
    override fun listRtuLog(holder: BwHolder<DataParam>): BwResult<BwRtuLog> {
        TODO("Not yet implemented")
    }

    /**
     * 批量增加rtu-log.白名单
     */
    override fun addRtuLog(holder: BwHolder<BwRtuLog>): BwResult<BwRtuLog> {
        TODO("Not yet implemented")
    }

    /**
     * 批量增加rtu-log.用户验证.
     */
    override fun addRtuLogUser(holder: BwHolder<BwRtuLog>): BwResult<BwRtuLog> {
        TODO("Not yet implemented")
    }

    /**
     * 批量删除rtu-log.
     */
    override fun deleteRtuLog(holder: BwHolder<DataParam>): BwResult<BwRtuLog> {
        TODO("Not yet implemented")
    }

    override fun listRtu(holder: BwHolder<MeterParam>): BwResult<BwRtu> {
        TODO("Not yet implemented")
    }

    /**
     * 批量删除rtu.
     */
    override fun deleteRtu(holder: BwHolder<MeterParam>): BwResult<BwRtu> {
        TODO("Not yet implemented")
    }

    companion object {

        private val lgr = LoggerFactory.getLogger(DataServiceImpl::class.java)

        const val ERR_PARAM = "参数不完整"
        const val ERR_INTERNAL = "内部错误:"

        const val EXPIRED_SESSION = "会话已失效，请注销后重新登录"

        const val WARN_NO_RIGHT = "无权执行该操作: "

        const val CANNOT_LOGIN_WEB = "抱歉，您不具有登录网页系统的权限！"

        /**
         * super user, can't be deleted.
         */
        const val USER_ADMIN = "admin"

        const val KEY_VALUE = "value"

        const val KEY_USER_ID = "userId"
    }
}