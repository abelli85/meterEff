package com.abel.bigwater.impl

import com.abel.bigwater.api.*
import com.abel.bigwater.mapper.MeterMapper
import com.abel.bigwater.model.stat.MeterStat
import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("statService")
class StatServiceImpl : StatService {
    @Autowired
    var meterMapper: MeterMapper? = null

    @Autowired
    private var loginManager: LoginManager? = null

    /**
     * 统计水表
     * @return 口径统计; 机构统计.
     */
    override fun statMeter(holder: BwHolder<MeterParam>): BwResult<MeterStat> {
        if (holder.lr?.sessionId.isNullOrBlank()) {
            return BwResult(2, MeterServiceImpl.ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to stat meter: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = MeterService.BASE_PATH + MeterService.PATH_LIST_ZONE_METER
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            dp.also {
                if (!it.firmId.orEmpty().startsWith(login.single!!.firmId!!)) {
                    it.firmId = login.single!!.firmId
                }
                if (!it.firmId!!.endsWith("%")) {
                    it.firmId = it.firmId + "%"
                }
            }

            val ms = MeterStat().also {
                it.sizeList = meterMapper!!.statMeterSize(dp)
                it.firmList = meterMapper!!.statMeterFirm(dp)
            }
            return BwResult(ms).apply {
                error = "口径列表： ${ms.sizeList?.size}"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(StatServiceImpl::class.java)
    }
}