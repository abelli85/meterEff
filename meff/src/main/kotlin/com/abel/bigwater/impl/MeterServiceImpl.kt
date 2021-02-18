package com.abel.bigwater.impl

import com.abel.bigwater.Helper
import com.abel.bigwater.api.*
import com.abel.bigwater.api.MeterService.Companion.BASE_PATH
import com.abel.bigwater.api.MeterService.Companion.PATH_FETCH_DMA
import com.abel.bigwater.mapper.EffMapper
import com.abel.bigwater.mapper.MeterMapper
import com.abel.bigwater.model.*
import com.abel.bigwater.model.eff.VcMeterVerifyPoint
import com.abel.bigwater.model.zone.ZoneMeter
import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("meterService")
open class MeterServiceImpl : MeterService {
    @Autowired
    var meterMapper: MeterMapper? = null

    @Autowired
    var effMapper: EffMapper? = null

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

        val list = if (holder.single?.meterId.isNullOrBlank())
            holder.list!!
        else
            holder.list.orEmpty().plus(holder.single!!)
        list.forEach {
            if (it.meterId.isNullOrBlank() || it.meterName.isNullOrBlank()
                    || it.sizeId == null || it.sizeName.isNullOrBlank()
                    || it.steelNo.isNullOrBlank()) {
                return BwResult(2, "水表ID/名称/口径/表码 不能为空: ${it.meterId}")
            }
        }

        lgr.info("${holder.lr?.userId} try to add meter: ${JSON.toJSONString(holder.single)}")

        val rightName = MeterService.BASE_PATH + MeterService.PATH_INSERT_ZONE_METER
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            // check duplicate meterId
            meterMapper!!.selectMeter(MeterParam().apply {
                meterIdList = list.map { it.meterId!! }
            }).also {
                if (it.isNotEmpty()) {
                    return BwResult(2, "水表档案已存在: ${it.joinToString { m1 -> m1.meterId!! }}")
                }
            }

            list.forEach {
                // 只能更新 所在机构及分公司
                if (!it.firmId.orEmpty().startsWith(login.single!!.firmId!!)) {
                    it.firmId = login.single!!.firmId
                }

                meterMapper!!.insertMeter(it)
                if (!it.verifyList.isNullOrEmpty()) meterMapper!!.insertMeterVerify(it)
                if (!it.pointList.isNullOrEmpty()) meterMapper!!.insertVerifyPoint(it)
            }

            return BwResult(list).apply {
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

        val list = if (holder.single?.meterId.isNullOrBlank())
            holder.list.orEmpty()
        else
            holder.list.orEmpty().plus(holder.single!!)
        list.forEach {
            if (it.meterId.isNullOrBlank()) {
                return BwResult(2, "水表ID不能为空")
            }
        }

        lgr.info("${holder.lr?.userId} try to add meter: ${JSON.toJSONString(holder.single)}")

        val rightName = MeterService.BASE_PATH + MeterService.PATH_DELETE_ZONE_METER
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            var cnt = 0
            list.forEach {
                // 只能更新 所在机构及分公司
                if (!it.firmId.orEmpty().startsWith(login.single!!.firmId!!)) {
                    it.firmId = login.single!!.firmId
                }

                meterMapper!!.deleteMeterVerify(MeterParam(meterId = it.meterId))
                meterMapper!!.deleteVerifyPoint(MeterParam(meterId = it.meterId))

                cnt += meterMapper!!.deleteMeter(MeterParam().apply {
                    meterId = it.meterId
                    firmId = it.firmId
                })
            }

            return BwResult(list).apply {
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

        val list = if (holder.single?.meterId.isNullOrBlank())
            holder.list.orEmpty()
        else
            holder.list.orEmpty().plus(holder.single!!)
        list.forEach {
            if (it.meterId.isNullOrBlank() || it.meterName.isNullOrBlank()
                    || it.sizeId == null || it.sizeName.isNullOrBlank()) {
                return BwResult(2, "水表ID/名称/口径不能为空: ${it.meterId}")
            }
        }

        lgr.info("${holder.lr?.userId} try to add meter: ${JSON.toJSONString(holder.single)}")

        val rightName = MeterService.BASE_PATH + MeterService.PATH_UPDATE_ZONE_METER
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            var cnt = 0
            list.forEach {
                // 只能更新 所在机构及分公司
                it.subFirmId = login.single!!.firmId!!.plus('%')
                if (!it.firmId.orEmpty().startsWith(login.single!!.firmId!!)) {
                    it.firmId = login.single!!.firmId
                }
                cnt += meterMapper!!.updateMeter(it)
            }

            return BwResult(list).apply {
                error = "更新水表： $cnt"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

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
    override fun addMeterPoint(holder: BwHolder<ZoneMeter>): BwResult<ZoneMeter> {
        lgr.info("${holder.lr?.userId} try to add verify: ${JSON.toJSONString(holder.single, true)}")

        if (holder.lr?.sessionId.isNullOrBlank()
                || holder.single?.meterId.isNullOrBlank()
                || holder.single?.steelNo.isNullOrBlank()
                || holder.single?.pointList.isNullOrEmpty()) {
            return BwResult(2, "$ERR_PARAM:水表ID、表码、检定点不能为空.")
        }
        holder.single!!.verifyList?.forEach {
            if (it.verifyDate == null) {
                return BwResult(2, "$ERR_PARAM: 检定结果的检定日期不能为空.")
            }
            if (it.batchId.isNullOrBlank()) {
                it.batchId = "n/a"
            }
        }
        holder.single!!.pointList!!.forEach {
            if (it.pointFlow == null || it.pointNo == null || it.pointName.isNullOrBlank()
                    || it.pointDev == null || it.verifyDate == null) {
                return BwResult(2, "$ERR_PARAM: 检定点的流量、编号、名称、误差、检定日期不能为空.")
            }
        }

        val rightName = MeterService.BASE_PATH + MeterService.PATH_ADD_METER_POINT
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            var cnt1 = 0
            var cnt2 = 0
            holder.single?.also {
                // 只能更新 所在机构及分公司
                if (!it.firmId.orEmpty().startsWith(login.single!!.firmId!!)) {
                    it.firmId = login.single!!.firmId
                }

                val meter = meterMapper!!.selectMeter(MeterParam().apply {
                    meterId = it.meterId
                    firmId = it.firmId
                }).firstOrNull() ?: return BwResult(3, "水表不存在: ${it.meterId}")
                if (meter.steelNo.isNullOrBlank()) {
                    return BwResult(2, "水表表码为空: ${meter.meterName} / ${meter.meterId}")
                }

                if (!it.verifyList.isNullOrEmpty()) {
                    cnt1 = meterMapper!!.insertMeterVerify(it)
                }
                if (!it.pointList.isNullOrEmpty()) {
                    cnt2 = meterMapper!!.insertVerifyPoint(it)
                }
            }

            return BwResult(holder.single!!).apply {
                error = "增加检定点： ${cnt1} / ${cnt2}"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 删除检定点
     * holder#single holds the meter to be updated.
     * @see ZoneMeter.verifyList
     * @see ZoneMeter.pointList
     */
    override fun removeMeterPoint(holder: BwHolder<ZoneMeter>): BwResult<ZoneMeter> {
        if (holder.lr?.sessionId.isNullOrBlank() || (
                        holder.single?.meterId.isNullOrBlank())) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to add verify: ${JSON.toJSONString(holder.single)}")

        val rightName = MeterService.BASE_PATH + MeterService.PATH_ADD_METER_POINT
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            var cnt1 = 0
            var cnt2 = 0
            holder.single?.also {
                // 只能更新 所在机构及分公司
                if (!it.firmId.orEmpty().startsWith(login.single!!.firmId!!)) {
                    it.firmId = login.single!!.firmId
                }

                val meter = meterMapper!!.selectMeter(MeterParam().apply {
                    meterId = it.meterId
                    firmId = it.firmId
                }).firstOrNull() ?: return BwResult(3, "水表不存在: ${it.meterId}")

                if (!it.pointList.isNullOrEmpty()) {
                    cnt2 = meterMapper!!.deleteVerifyPoint(MeterParam().apply {
                        meterId = it.meterId
                        pointIdList = it.pointList?.map { p -> p.pointId ?: 0 }
                    })
                }

                if (!it.verifyList.isNullOrEmpty()) {
                    cnt1 = meterMapper!!.deleteMeterVerify(MeterParam().apply {
                        meterId = it.meterId
                        verifyIdList = it.verifyList?.map { v -> v.verifyId ?: 0 }
                    })
                }
            }

            return BwResult(holder.single!!).apply {
                error = "移除检定点： ${cnt1} / ${cnt2}"
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
        if (holder.lr?.sessionId.isNullOrBlank() || (
                        holder.single?.meterId.isNullOrBlank()
                                && holder.list.isNullOrEmpty())) {
            return BwResult(2, ERR_PARAM)
        }

        val list = if (holder.single?.meterId.isNullOrBlank())
            holder.list.orEmpty()
        else
            holder.list.orEmpty().plus(holder.single!!)
        list.forEach {
            if (it.meterId.isNullOrBlank() || it.meterLoc.isNullOrBlank()) {
                return BwResult(2, "水表ID/坐标不能为空: ${it.meterId}")
            }
        }

        lgr.info("${holder.lr?.userId} try to add meter: ${JSON.toJSONString(holder.single)}")

        val rightName = MeterService.BASE_PATH + MeterService.PATH_UPDATE_METER_LOC
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            var cnt = 0
            list.forEach {
                // 只能更新 所在机构及分公司
                if (!it.firmId.orEmpty().startsWith(login.single!!.firmId!!)) {
                    it.firmId = login.single!!.firmId
                }
                cnt += meterMapper!!.updateMeterLoc(it)
            }

            return BwResult(list).apply {
                error = "更新水表： $cnt"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 大表列表
     * 如果填充了如下字段, 则返回的水表包含DMA信息:
     * @see MeterParam.dmaId
     * @see MeterParam.dmaName
     * @see MeterParam.dmaIdList
     * 否则, 返回的水表包含片区信息(Zone)
     *
     * result#list holds the list of meters.
     */
    override fun listMeter(holder: BwHolder<MeterParam>): BwResult<ZoneMeter> {
        if (holder.lr?.sessionId.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to list meter: ${JSON.toJSONString(holder.single)}")
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

                if (it.keywords?.contains(Regex("[a-zA-Z]+")) == true) {
                    it.pinyin = it.keywords
                    it.keywords = null
                } else {
                    val rx = Regex("\\s+")
                    it.keywords = it.keywords?.trim()?.replace(rx, "|")
                }
            }

            return BwResult(
                    if (dp.dmaId.isNullOrBlank() && dp.dmaName.isNullOrBlank() && dp.dmaIdList.isNullOrEmpty())
                        meterMapper!!.selectMeterZone(dp)
                    else
                        meterMapper!!.selectMeter(dp)
            ).apply {
                error = "水表数量： ${list?.size}"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

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
    override fun fetchMeter(holder: BwHolder<MeterParam>): BwResult<ZoneMeter> {
        if (holder.lr?.sessionId.isNullOrBlank()
                || holder.single?.meterId.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to fetch meter: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = MeterService.BASE_PATH + MeterService.PATH_FETCH_ZONE_METER
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

            val meter = if (dp.dmaId.isNullOrBlank() && dp.dmaName.isNullOrBlank() && dp.dmaIdList.isNullOrEmpty())
                meterMapper!!.selectMeterZone(dp).firstOrNull()
            else
                meterMapper!!.selectMeter(dp).firstOrNull() ?: return BwResult(3, "水表不存在: ${dp.meterId}")

            meter!!.verifyList = meterMapper!!.listMeterVerify(dp)
            meter.pointList = meterMapper!!.listVerifyPoint(dp)
            meter.decayObj = effMapper!!.selectEffDecay(EffParam().apply {
                decayId = meter.decayId
                meterBrandId = meter.meterBrandId
                sizeId = meter.sizeId
                sizeName = meter.sizeName
                modelSize = meter.modelSize?.substringBefore('-')
            }).firstOrNull()

            return BwResult(meter)
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * DMA列表
     */
    override fun listDma(holder: BwHolder<MeterParam>): BwResult<BwDma> {
        if (holder.lr?.sessionId.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to list meter: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = MeterService.BASE_PATH + MeterService.PATH_LIST_DMA
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            dp.refineFirmId(login.single!!)

            return BwResult(meterMapper!!.selectDma(dp)).apply {
                error = "DMA数量： ${list?.size}"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
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
     * 创建DMA, 一次创建多个或一个
     * @see BwHolder.list - 一次创建多个
     * @see BwHolder.single - 一次创建一个
     * @see BwDma.meterList - 同时关联水表
     */
    override fun insertDma(holder: BwHolder<BwDma>): BwResult<BwDma> {
        if (holder.lr?.sessionId.isNullOrBlank() || (
                        holder.single?.dmaId.isNullOrBlank()
                                && holder.list.isNullOrEmpty())) {
            return BwResult(2, ERR_PARAM)
        }

        val list = if (holder.single?.dmaId.isNullOrBlank())
            holder.list!!
        else
            holder.list.orEmpty().plus(holder.single!!)
        list.forEach {
            if (it.dmaId.isNullOrBlank() || it.dmaName.isNullOrBlank()) {
                return BwResult(2, "DMA-ID/名称不能为空: ${it.dmaId}")
            }
        }

        lgr.info("${holder.lr?.userId} try to add DMA: ${JSON.toJSONString(holder.single)}")

        val rightName = MeterService.BASE_PATH + MeterService.PATH_INSERT_DMA
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            var rcnt = 0
            list.forEach {
                // 只能更新 所在机构及分公司
                it.refineFirmId(login.single!!)

                meterMapper!!.insertDma(it)
                if (!it.meterList.isNullOrEmpty()) {
                    rcnt += meterMapper!!.linkMeterDma(MeterParam().apply {
                        dmaId = it.dmaId
                        meterList = it.meterList
                    })
                }
            }

            return BwResult(list).apply {
                error = "增加DMA： ${list.size} / $rcnt"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 获取一个DMA的详情, 必填:
     * @see MeterParam.dmaId
     * @return DMA及包含的水表
     */
    override fun fetchDma(holder: BwHolder<MeterParam>): BwResult<BwDma> {
        if (holder.lr?.sessionId.isNullOrBlank()
                || holder.single?.dmaId.isNullOrBlank()
                || holder.single!!.dmaId.equals("%")) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to fetch dma: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = BASE_PATH + PATH_FETCH_DMA
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            dp.refineFirmId(login.single!!)
            val z = meterMapper!!.selectDma(dp).firstOrNull()?.also {
                it.meterList = meterMapper!!.listDmaMeter(dp)
                return BwResult(it)
            }
            return BwResult(0, "DMA未找到: ${dp.dmaId}")
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 修改DMA, 一次修改多个或一个, 不修改关联水表
     * @see BwHolder.list - 一次修改多个
     * @see BwHolder.single - 一次修改一个
     */
    override fun updateDma(holder: BwHolder<BwDma>): BwResult<BwDma> {
        if (holder.lr?.sessionId.isNullOrBlank() || (
                        holder.single?.dmaId.isNullOrBlank()
                                && holder.list.isNullOrEmpty())) {
            return BwResult(2, ERR_PARAM)
        }

        val list = if (holder.single?.dmaId.isNullOrBlank())
            holder.list!!
        else
            holder.list.orEmpty().plus(holder.single!!)
        list.forEach {
            if (it.dmaId.isNullOrBlank() || it.dmaName.isNullOrBlank()) {
                return BwResult(2, "DMA-ID/名称不能为空: ${it.dmaId}")
            }
        }

        lgr.info("${holder.lr?.userId} try to update DMA: ${JSON.toJSONString(holder.single)}")

        val rightName = MeterService.BASE_PATH + MeterService.PATH_UPDATE_DMA
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            var rcnt = 0
            list.forEach {
                // 只能更新 所在机构及分公司
                it.refineFirmId(login.single!!)

                rcnt += meterMapper!!.updateDma(it)
            }

            return BwResult(list).apply {
                error = "更新DMA： $rcnt / ${list.size}"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
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
     * 删除DMA, 填充:
     * @see MeterParam.dmaId
     */
    override fun deleteDma(holder: BwHolder<MeterParam>): BwResult<BwDma> {
        if (holder.lr?.sessionId.isNullOrBlank() ||
                holder.single?.dmaId.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to delete DMA: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = MeterService.BASE_PATH + MeterService.PATH_DELETE_DMA
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            var rcnt = meterMapper!!.deleteDma(dp)

            return BwResult(BwDma().apply {
                dmaId = dp.dmaId
            }).apply {
                error = "删除DMA： ${dp.dmaId}/ $rcnt"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
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
     * 关联DMA和水表. 要关联的水表应存在, 但不存在不会返回错误, 只是不关联.
     * 覆盖相同标识的已关联水表, 增加未关联的水表(存在的水表);
     * 对于已关联其他水表 无改变. 必填:
     * @see MeterParam.dmaId
     * @see MeterParam.meterList
     */
    override fun linkMeterDma(holder: BwHolder<MeterParam>): BwResult<BwDma> {
        if (holder.lr?.sessionId.isNullOrBlank()
                || holder.single?.dmaId.isNullOrBlank()
                || holder.single?.meterList.isNullOrEmpty()) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to save dma-meter: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = MeterService.BASE_PATH + MeterService.PATH_LINK_DMA_METER
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            dp.firmId = Helper.refineFirmId(dp.firmId, login.single!!.firmId!!, false)!!
            val dma = meterMapper!!.selectDma(MeterParam().apply {
                zoneId = dp.zoneId
                firmId = dp.firmId
            }).firstOrNull() ?: return BwResult(2, "DMA不存在或不属于本单位: ${dp.zoneId}/${dp.zoneName}")

            // attach then
            var rcnt = 0
            var mlist = dp.meterList.orEmpty()
            while (mlist.isNotEmpty()) {
                val zp = MeterParam().apply {
                    dmaId = dp.dmaId
                    meterList = mlist.take(200)
                }
                rcnt += meterMapper!!.rebindDmaMeter(zp)
                rcnt += meterMapper!!.linkMeterDma(zp)

                mlist = mlist.drop(200)
            }

            return BwResult(dma).apply {
                error = "关联DMA水表 ${dp.dmaId}/${dp.dmaName}: $rcnt/${dp.meterList?.size}"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 解除关联DMA和水表. 必填:
     * @see MeterParam.dmaId
     * @see MeterParam.meterIdList - 选填, 为空时解除关联的所有水表
     */
    override fun detachMeterDma(holder: BwHolder<MeterParam>): BwResult<BwDma> {
        if (holder.lr?.sessionId.isNullOrBlank()
                || holder.single?.dmaId.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to detach dma-meter: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = MeterService.BASE_PATH + MeterService.PATH_DETACH_DMA_METER
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            dp.firmId = Helper.refineFirmId(dp.firmId, login.single!!.firmId!!, false)!!
            val dma = meterMapper!!.selectDma(MeterParam().apply {
                dmaId = dp.dmaId
                firmId = dp.firmId
            }).firstOrNull() ?: return BwResult(2, "DMA不存在或不属于本单位: ${dp.zoneId}/${dp.zoneName}")

            var rcnt = 0

            // detach firstly
            if (dp.meterIdList.isNullOrEmpty()) {
                rcnt = meterMapper!!.detachMeterDma(MeterParam().apply {
                    dmaId = dp.dmaId
                })
            } else {
                var mlist = dp.meterIdList!!
                while (mlist.isNotEmpty()) {
                    rcnt += meterMapper!!.detachMeterDma(MeterParam().apply {
                        dmaId = dp.dmaId
                        meterIdList = mlist.take(1000)
                    })

                    mlist = mlist.drop(1000)
                }
            }
            return BwResult(dma).apply {
                error = "解除关联DMA水表 ${dp.zoneId}/${dp.zoneName}: $rcnt/${dp.meterList?.size}"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
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