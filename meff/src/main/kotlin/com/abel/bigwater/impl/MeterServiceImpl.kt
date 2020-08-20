package com.abel.bigwater.impl

import com.abel.bigwater.api.*
import com.abel.bigwater.mapper.MeterMapper
import com.abel.bigwater.model.*
import com.abel.bigwater.model.zone.ZoneMeter
import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("meterService")
class MeterServiceImpl : MeterService {
    @Autowired
    var meterMapper: MeterMapper? = null

    @Autowired
    private var loginManager: LoginManager? = null

    /**
     * 水表品牌列表
     */
    override fun selectMeterBrand(): BwResult<BwMeterBrand> {
        return BwResult(meterMapper!!.selectMeterBrand())
    }

    /**
     * RTU品牌列表
     */
    override fun selectRemoteBrand(): BwResult<BwRemoteBrand> {
        return BwResult(meterMapper!!.selectRemoteBrand())
    }

    /**
     * 创建水表
     */
    override fun insertMeter(holder: BwHolder<ZoneMeter>): BwResult<ZoneMeter> {
        if (holder.lr?.sessionId.isNullOrBlank() || (
                        holder.single?.meterId.isNullOrBlank()
                                && holder.list.isNullOrEmpty())) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to add meter: ${JSON.toJSONString(holder.single)}")

        val rightName = MeterService.BASE_PATH + MeterService.PATH_INSERT_ZONE_METER
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            val list = if (holder.single?.meterId.isNullOrBlank())
                holder.list.orEmpty()
            else
                holder.list.orEmpty().plus(holder.single!!)

            list.forEach {
                // 只能更新 所在机构及分公司
                if (!it.firmId.orEmpty().startsWith(login.single!!.firmId!!)) {
                    it.firmId = login.single!!.firmId
                }

                meterMapper!!.insertMeter(it)
            }

            return BwResult(holder.single!!).apply {
                error = "增加水表： ${list.size}"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 删除大表
     * holder#list holds list of meters.
     */
    override fun deleteMeter(holder: BwHolder<ZoneMeter>): BwResult<ZoneMeter> {
        if (holder.lr?.sessionId.isNullOrBlank() || (
                        holder.single?.meterId.isNullOrBlank()
                                && holder.list.isNullOrEmpty())) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to add meter: ${JSON.toJSONString(holder.single)}")

        val rightName = MeterService.BASE_PATH + MeterService.PATH_INSERT_ZONE_METER
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            val list = if (holder.single?.meterId.isNullOrBlank())
                holder.list.orEmpty()
            else
                holder.list.orEmpty().plus(holder.single!!)

            var cnt = 0
            list.forEach {
                // 只能更新 所在机构及分公司
                if (!it.firmId.orEmpty().startsWith(login.single!!.firmId!!)) {
                    it.firmId = login.single!!.firmId
                }
                cnt += meterMapper!!.deleteMeter(MeterParam(
                        meterId = it.meterId,
                        firmId = it.firmId))
            }

            return BwResult(holder.single!!).apply {
                error = "删除水表： $cnt"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 修改大表
     * holder#single holds the meter to be updated.
     */
    override fun updateMeter(holder: BwHolder<ZoneMeter>): BwResult<ZoneMeter> {
        if (holder.lr?.sessionId.isNullOrBlank() || (
                        holder.single?.meterId.isNullOrBlank()
                                && holder.list.isNullOrEmpty())) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to add meter: ${JSON.toJSONString(holder.single)}")

        val rightName = MeterService.BASE_PATH + MeterService.PATH_INSERT_ZONE_METER
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            val list = if (holder.single?.meterId.isNullOrBlank())
                holder.list.orEmpty()
            else
                holder.list.orEmpty().plus(holder.single!!)

            var cnt = 0
            list.forEach {
                // 只能更新 所在机构及分公司
                if (!it.firmId.orEmpty().startsWith(login.single!!.firmId!!)) {
                    it.firmId = login.single!!.firmId
                }
                cnt += meterMapper!!.updateMeter(it)
            }

            return BwResult(holder.single!!).apply {
                error = "更新水表： $cnt"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 更新大表的坐标，设置meter#{id, meterLoc}即可。
     * meterLoc为点坐标的WKT格式，如：POINT (22.5 114)
     */
    override fun updateMeterLoc(holder: BwHolder<ZoneMeter>): BwResult<ZoneMeter> {
        TODO("Not yet implemented")
    }

    /**
     * 大表列表
     * result#list holds the list of meters.
     */
    override fun listMeter(holder: BwHolder<MeterParam>): BwResult<BwMeter> {
        if (holder.lr?.sessionId.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to list meter: ${JSON.toJSONString(holder.single)}")

        val rightName = MeterService.BASE_PATH + MeterService.PATH_LIST_ZONE_METER
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            holder.single?.also {
                if (!it.firmId.orEmpty().startsWith(login.single!!.firmId!!)) {
                    it.firmId = login.single!!.firmId
                }
            }
            return BwResult(meterMapper!!.selectMeter(holder.single!!).map { it as BwMeter }).apply {
                error = "水表数量： ${list?.size}"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * DMA列表
     */
    override fun listDma(holder: BwHolder<MeterParam>): BwResult<BwDma> {
        TODO("Not yet implemented")
    }

    /**
     * 查询DMA的坐标及边界，及最后漏损.
     *
     * 注：每个DMA包含两条结果，最后一日及前一日，最后一日由于RTU上传周期的问题，也许会出现“数据不全”的结果。
     * 这种情况下，lastLoss将为null，而前一日的漏损量应该是完整的，即lastLoss不为null。注意在显示漏损地图时
     * 应尽量使用非null值：如果最后一日lastLoss != null，则使用最后一日; 否则使用前一日的 lastLoss; 如果
     * 前一日的lastLoss 也是null, 则使用comments 字段的内容。
     */
    override fun listDmaLoc(holder: BwHolder<LocParam>): BwResult<BwDmaLoc> {
        TODO("Not yet implemented")
    }

    /**
     * 创建DMA
     */
    override fun insertDma(holder: BwHolder<BwDma>): BwResult<BwDma> {
        TODO("Not yet implemented")
    }

    /**
     * 修改DMA
     */
    override fun updateDma(holder: BwHolder<BwDma>): BwResult<BwDma> {
        TODO("Not yet implemented")
    }

    /**
     * 更新DMA分区的坐标及边界，其中dmaLoc为点坐标的WKT格式，dmaRegion为多边形的WKT格式，如：
     * dmaLoc = POINT (22.5 114)
     * dmaRegion = POLYGON ((22.5 114, 23 112, 22.8 112, 22.5 114))
     */
    override fun updateDmaLoc(holder: BwHolder<BwDmaLoc>): BwResult<BwDmaLoc> {
        TODO("Not yet implemented")
    }

    /**
     * 删除DMA
     */
    override fun deleteDma(holder: BwHolder<MeterParam>): BwResult<BwDma> {
        TODO("Not yet implemented")
    }

    /**
     * 分离DMA和账号
     * The string value holds userId.
     */
    override fun detachDmaUser(holder: BwHolder<BwUser>): BwResult<String> {
        TODO("Not yet implemented")
    }

    /**
     * 关联DMA和账号
     * holder#single#dmaIdList holds the list to be attached.
     */
    override fun attachDmaUser(holder: BwHolder<BwUser>): BwResult<String> {
        TODO("Not yet implemented")
    }

    /**
     * 关联DMA和大表
     * holder#single#dmaId - meterIdList
     */
    override fun linkMeterDma(holder: BwHolder<MeterParam>): BwResult<String> {
        TODO("Not yet implemented")
    }

    /**
     * 分离DMA和大表
     * holder#single#dmaId - meterIdList
     */
    override fun detachMeterDma(holder: BwHolder<MeterParam>): BwResult<String> {
        TODO("Not yet implemented")
    }

    companion object {

        private val lgr = LoggerFactory.getLogger(MeterServiceImpl::class.java)

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