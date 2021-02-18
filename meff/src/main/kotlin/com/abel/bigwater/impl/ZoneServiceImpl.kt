package com.abel.bigwater.impl

import com.abel.bigwater.Helper
import com.abel.bigwater.api.BwHolder
import com.abel.bigwater.api.BwResult
import com.abel.bigwater.api.MeterParam
import com.abel.bigwater.api.ZoneService
import com.abel.bigwater.mapper.EffMapper
import com.abel.bigwater.mapper.MeterMapper
import com.abel.bigwater.mapper.ZoneMapper
import com.abel.bigwater.model.zone.Zone
import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("zoneService")
class ZoneServiceImpl : ZoneService {
    @Autowired
    var meterMapper: MeterMapper? = null

    @Autowired
    var effMapper: EffMapper? = null

    @Autowired
    private var loginManager: LoginManager? = null

    @Autowired
    var zoneMapper: ZoneMapper? = null

    /**
     * 列出分区, 不包含水表
     */
    override fun listZone(holder: BwHolder<MeterParam>): BwResult<Zone> {
        if (holder.lr?.sessionId.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to list zone: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = ZoneService.BASE_PATH + ZoneService.PATH_LIST_ZONE
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            dp.refineFirmId(login.single!!)
            return BwResult(zoneMapper!!.listZone(dp))
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 创建分区, 包含其内部的总分表
     */
    override fun saveZone(holder: BwHolder<Zone>): BwResult<Zone> {
        if (holder.lr?.sessionId.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to save zone: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = ZoneService.BASE_PATH + ZoneService.PATH_SAVE_ZONE
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            dp.firmId = Helper.refineFirmId(dp.firmId, login.single!!.firmId!!, false)!!
            zoneMapper!!.insertZone(dp)

            var mlist = dp.meterList.orEmpty()
            while(mlist.isNotEmpty()) {
                val zp = MeterParam().apply {
                    zoneId = dp.zoneId
                    meterList = mlist.take(200)
                }
                zoneMapper!!.attachZoneMeter(zp)

                mlist = mlist.drop(200)
            }

            return BwResult(dp)
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 获取一个分区的详情, 必填:
     * @see MeterParam.zoneId
     */
    override fun fetchZone(holder: BwHolder<MeterParam>): BwResult<Zone> {
        if (holder.lr?.sessionId.isNullOrBlank()
                || holder.single?.zoneId.isNullOrBlank()
                || holder.single!!.zoneId.equals("%")) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to list zone: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = ZoneService.BASE_PATH + ZoneService.PATH_LIST_ZONE
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            dp.refineFirmId(login.single!!)
            val z = zoneMapper!!.listZone(dp).firstOrNull()?.also {
                it.meterList = meterMapper!!.selectMeterZone(dp)
                return BwResult(it)
            }
            return BwResult(0, "分区未找到: ${dp.zoneId}")
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 更新一个分区, 包含其内部的总分表
     */
    override fun updateZone(holder: BwHolder<Zone>): BwResult<Zone> {
        if (holder.lr?.sessionId.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to update zone: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = ZoneService.BASE_PATH + ZoneService.PATH_UPDATE_ZONE
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            dp.firmId = Helper.refineFirmId(dp.firmId, login.single!!.firmId!!, false)!!
            val rcnt = zoneMapper!!.updateZone(dp)
            if (rcnt == 0) {
                return BwResult(2, "分区不存在: ${dp.zoneId}/${dp.zoneName}")
            }

            return BwResult(dp).apply {
                error = "修改分区: ${dp.zoneId}/${dp.zoneName}"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 删除一个分区, 并解除分区跟水表的关联, 但不删除关联水表
     */
    override fun deleteZone(holder: BwHolder<Zone>): BwResult<Zone> {
        if (holder.lr?.sessionId.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to delete zone: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = ZoneService.BASE_PATH + ZoneService.PATH_DELETE_ZONE
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            dp.firmId = Helper.refineFirmId(dp.firmId, login.single!!.firmId!!, false)!!
            zoneMapper!!.detachZoneMeter(MeterParam().apply {
                zoneId = dp.zoneId
                firmId = dp.firmId
            })

            val rcnt = zoneMapper!!.deleteZone(MeterParam().apply {
                zoneId = dp.zoneId
                zoneName = dp.zoneName
                firmId = dp.firmId
            })

            return BwResult(0, "删除分区: $rcnt")
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 关联分区和水表. 要关联的水表应存在, 但不存在不会返回错误, 只是不关联.
     * 覆盖相同标识的已关联水表, 增加未关联的水表(存在的水表);
     * 对于已关联其他水表 无改变. 必填:
     * @see Zone.zoneId
     * @see Zone.meterList
     */
    override fun saveZoneMeter(holder: BwHolder<Zone>): BwResult<Zone> {
        if (holder.lr?.sessionId.isNullOrBlank()
                || holder.single?.zoneId.isNullOrBlank()
                || holder.single?.meterList.isNullOrEmpty()) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to save zone-meter: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = ZoneService.BASE_PATH + ZoneService.PATH_ATTACH_ZONE_METER
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            dp.firmId = Helper.refineFirmId(dp.firmId, login.single!!.firmId!!, false)!!
            zoneMapper!!.listZone(MeterParam().apply {
                zoneId = dp.zoneId
                firmId = dp.firmId
            }).firstOrNull() ?: return BwResult(2, "分区不存在或不属于本单位: ${dp.zoneId}/${dp.zoneName}")

            // attach then
            var rcnt = 0
            var mlist = dp.meterList.orEmpty()
            while(mlist.isNotEmpty()) {
                val zp = MeterParam().apply {
                    zoneId = dp.zoneId
                    meterList = mlist.take(200)
                }
                rcnt += zoneMapper!!.rebindZoneMeter(zp)
                rcnt += zoneMapper!!.attachZoneMeter(zp)

                mlist = mlist.drop(200)
            }

            return BwResult(0, "关联分区水表 ${dp.zoneId}/${dp.zoneName}: $rcnt/${dp.meterList?.size}")
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 解除关联分区和水表. 必填:
     * @see Zone.zoneId
     * @see Zone.meterList
     */
    override fun deleteZoneMeter(holder: BwHolder<Zone>): BwResult<Zone> {
        if (holder.lr?.sessionId.isNullOrBlank()
                || holder.single?.zoneId.isNullOrBlank()
                || holder.single?.meterList.isNullOrEmpty()) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to save zone-meter: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = ZoneService.BASE_PATH + ZoneService.PATH_ATTACH_ZONE_METER
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            dp.firmId = Helper.refineFirmId(dp.firmId, login.single!!.firmId!!, false)!!
            zoneMapper!!.listZone(MeterParam().apply {
                zoneId = dp.zoneId
                firmId = dp.firmId
            }).firstOrNull() ?: return BwResult(2, "分区不存在或不属于本单位: ${dp.zoneId}/${dp.zoneName}")

            var rcnt = 0

            // detach firstly
            var mlist = dp.meterList!!.map { it.meterId.orEmpty() }
            while (mlist.isNotEmpty()) {
                rcnt += zoneMapper!!.detachZoneMeter(MeterParam().apply {
                    zoneId = dp.zoneId
                    meterIdList = mlist.take(1000)
                })

                mlist = mlist.drop(1000)
            }
            return BwResult(0, "解除关联分区水表 ${dp.zoneId}/${dp.zoneName}: $rcnt/${dp.meterList?.size}")
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    companion object {

        const val ERR_PARAM = "参数不完整"
        const val ERR_INTERNAL = "内部错误:"

        const val EXPIRED_SESSION = "会话已失效，请注销后重新登录"

        const val WARN_NO_RIGHT = "无权执行该操作: "

        const val CANNOT_LOGIN_WEB = "抱歉，您不具有登录网页系统的权限！"

        private val lgr = LoggerFactory.getLogger(ZoneServiceImpl::class.java)
    }
}