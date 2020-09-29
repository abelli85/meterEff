package com.abel.bigwater.impl

import com.abel.bigwater.api.*
import com.abel.bigwater.mapper.EffMapper
import com.abel.bigwater.mapper.MeterMapper
import com.abel.bigwater.model.eff.EffMeter
import com.abel.bigwater.model.eff.EffTask
import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("effService")
class EffServiceImpl : EffService {

    @Autowired
    private var loginManager: LoginManager? = null

    @Autowired
    private var effMapper: EffMapper? = null

    @Autowired
    private var effTaskBean: EffTaskBean? = null

    @Autowired
    private var meterMapper: MeterMapper? = null

    /**
     * 创建计量效率分析任务
     */
    override fun createEffTask(holder: BwHolder<EffTask>): BwResult<EffTask> {
        if (holder.lr?.sessionId.isNullOrBlank()
                || holder.single?.taskName.isNullOrBlank()
                || holder.single?.taskStart == null || holder.single?.taskEnd == null) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to list eff-task: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = EffService.BASE_PATH + EffService.PATH_LIST_TASK
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            dp.also {
                if (!it.firmId.orEmpty().startsWith(login.single!!.firmId!!)) {
                    it.firmId = login.single!!.firmId
                    it.firmName = login.single!!.firmName
                }
            }

            effMapper!!.createEffTask(dp)
            var list = dp.meterList.orEmpty().apply {
                forEach { it.taskId = dp.taskId }
            }
            while (list.isNotEmpty()) {
                val p = EffParam().apply {
                    meterList = list.take(50)
                }
                list = list.drop(50)

                effMapper!!.insertEffMeter(p)
            }
            return BwResult(dp).apply {
                error = "创建任务： ${dp.taskId}/${dp.meterList?.size}"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 列出计量效率分析任务
     */
    override fun listEffTask(holder: BwHolder<EffParam>): BwResult<EffTask> {
        if (holder.lr?.sessionId.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to list eff-task: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = EffService.BASE_PATH + EffService.PATH_LIST_TASK
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

            val ms = effMapper!!.listEffTask(dp)
            return BwResult(ms).apply {
                error = "任务列表： ${ms.size}"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 获取单个计量效率分析任务的详情
     * @see EffParam.taskId
     */
    override fun fetchEffTask(holder: BwHolder<EffParam>): BwResult<EffTask> {
        if (holder.lr?.sessionId.isNullOrBlank() || holder.single?.taskId == null) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to fetch eff-task: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = EffService.BASE_PATH + EffService.PATH_FETCH_TASK
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

            val ms = effMapper!!.listEffTask(dp).firstOrNull() ?: return BwResult(3, "计量效率任务不存在: ${dp.taskId}")
            ms.meterList = effMapper!!.listEffMeter(dp)
            return BwResult(ms).apply {
                error = "任务： ${ms.taskName}"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 列出水表的分析结果
     */
    override fun listMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter> {
        if (holder.lr?.sessionId.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to list eff-meter: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = EffService.BASE_PATH + EffService.PATH_LIST_METER_EFF
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

            val ms = effMapper!!.listEffMeter(dp)
            return BwResult(ms).apply {
                error = "水表计量效率列表： ${ms.size}"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 添加水表的分析结果
     * @see EffParam.meterList
     */
    override fun addMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter> {
        if (holder.lr?.sessionId.isNullOrBlank()
                || holder.single?.meterList.isNullOrEmpty()) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to add eff-meter: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        dp.meterList!!.forEach {
            if (it.taskId == null || it.meterId.isNullOrBlank()
                    || it.taskName.isNullOrBlank() || it.meterName.isNullOrBlank()
                    || it.taskStart == null || it.taskEnd == null
                    || it.sizeId == null || it.sizeName.isNullOrBlank()) {
                return BwResult(2, "任务ID/水表ID/任务名称/水表名称/起至日期/口径 不能为空: ${it.meterId}")
            }
        }

        val rightName = EffService.BASE_PATH + EffService.PATH_ADD_METER_EFF
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            var mlist = dp.meterList!!
            do {
                val bp = EffParam().apply {
                    meterList = mlist.take(50)
                }
                mlist = mlist.drop(50)

                effMapper!!.insertEffMeter(bp)
            } while (mlist.isNotEmpty())

            return BwResult(0, "添加水表计量效率： ${dp.meterList?.size}")
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 删除水表的分析结果
     * @see EffParam.taskId
     * @see EffParam.meterId 或
     * @see EffParam.meterIdList
     */
    override fun deleteMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter> {
        if (holder.lr?.sessionId.isNullOrBlank()
                || (holder.single?.meterId.isNullOrBlank() && holder.single?.meterIdList.isNullOrEmpty())) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to delete eff-meter: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = EffService.BASE_PATH + EffService.PATH_DELETE_METER_EFF
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            var mlist = if (dp.meterId.isNullOrBlank()) dp.meterIdList!!
            else dp.meterIdList.orEmpty().plus(dp.meterId!!)

            var cnt = 0
            do {
                val bp = EffParam().apply {
                    taskId = dp.taskId
                    meterIdList = mlist.take(500)
                }
                mlist = mlist.drop(500)

                cnt += effMapper!!.deleteEffMeter(bp)
            } while (mlist.isNotEmpty())

            return BwResult(0, "删除水表计量效率： ${cnt}")
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 修改水表的分析结果
     * @see EffParam.taskId
     * @see EffParam.meterList
     */
    override fun updateMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter> {
        if (holder.lr?.sessionId.isNullOrBlank()
                || holder.single?.taskId == null || holder.single?.meterList.isNullOrEmpty()) {
            return BwResult(2, ERR_PARAM)
        }
        holder.single?.meterList!!.forEach {
            if (it.taskId == null || it.meterId.isNullOrBlank()
                    || it.meterName.isNullOrBlank() || it.taskName.isNullOrBlank()
                    || it.taskStart == null || it.taskEnd == null
                    || it.sizeId == null || it.sizeName.isNullOrBlank()) {
                return BwResult(2, "$ERR_PARAM: ${it.meterId}")
            }
        }

        lgr.info("${holder.lr?.userId} try to update eff-meter: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = EffService.BASE_PATH + EffService.PATH_UPDATE_METER_EFF
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            var mlist = dp.meterList!!
            var cnt = 0
            do {
                val bp = EffParam().apply {
                    taskId = dp.taskId
                    meterList = mlist.take(50)
                    meterIdList = meterList!!.map { it.meterId!! }
                }
                mlist = mlist.drop(50)

                cnt += effMapper!!.deleteEffMeter(bp)
                effMapper!!.insertEffMeter(bp)
            } while (mlist.isNotEmpty())

            return BwResult(0, "修改水表计量效率： ${cnt}")
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 获取单个水表的分析详情
     * @see EffParam.taskId
     * @see EffParam.meterId
     */
    override fun fetchMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter> {
        if (holder.lr?.sessionId.isNullOrBlank()
                || holder.single?.taskId == null || holder.single?.meterId.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to fetch eff-meter: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = EffService.BASE_PATH + EffService.PATH_FETCH_METER_EFF
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

            val em = effMapper!!.listEffMeter(dp).firstOrNull()?.apply {
                pointEffList = effMapper!!.listEffPoint(dp)
            } ?: return BwResult(3, "水表计量效率未找到:${dp.taskId} / ${dp.meterId}")

            return BwResult(em)
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 修改单个水表的分析详情
     * @see EffParam.taskId
     * @see EffParam.meterId
     * @see EffParam.pointEffList
     */
    override fun replaceMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter> {
        if (holder.lr?.sessionId.isNullOrBlank()
                || holder.single?.taskId == null || holder.single?.meterId.isNullOrBlank()
                || holder.single?.pointEffList.isNullOrEmpty()) {
            return BwResult(2, ERR_PARAM)
        }

        lgr.info("${holder.lr?.userId} try to replace eff-meter point-list: ${JSON.toJSONString(holder.single)}")
        val dp = holder.single!!

        val rightName = EffService.BASE_PATH + EffService.PATH_REPLACE_METER_EFF
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            effMapper!!.listEffMeter(EffParam().apply {
                taskId = dp.taskId
                meterId = dp.meterId
            }).firstOrNull() ?: return BwResult(3, "水表计量效率不存在: ${dp.taskId}/${dp.meterId}")

            dp.also {
                if (!it.firmId.orEmpty().startsWith(login.single!!.firmId!!)) {
                    it.firmId = login.single!!.firmId
                }
                if (!it.firmId!!.endsWith("%")) {
                    it.firmId = it.firmId + "%"
                }
            }
            val cnt1 = effMapper!!.deleteEffPoint(dp)
            val cnt2 = effMapper!!.insertEffPoint(dp)
            return BwResult(0, "水表计量效率修改:$cnt1 / $cnt2 / ${dp.taskId} / ${dp.meterId}")
        } catch (ex: Exception) {
            lgr.error(ex.message, ex)
            return BwResult(1, "${ERR_INTERNAL} ${ex.message}")
        }
    }

    /**
     * 分析水表的计量效率, 分析一只或多只水表, 指定时段的计量效率.
     * @see EffParam.meterId
     * @see EffParam.meterIdList
     * @see EffParam.taskStart - can be null
     * @see EffParam.taskEnd - can be null
     */
    override fun buildMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter> {
        if (holder.lr?.sessionId.isNullOrBlank()
                || (holder.single?.meterId.isNullOrBlank() && holder.single?.meterIdList.isNullOrEmpty())
                || holder.single?.jodaTaskEnd?.isAfterNow == true
                || holder.single?.jodaTaskStart?.isAfterNow == true) {
            return BwResult(2, ERR_PARAM)
        }
        if (holder.single?.jodaTaskStart != null
                && holder.single?.jodaTaskEnd?.isBefore(holder.single?.jodaTaskStart!!) == true) {
            return BwResult(2, ERR_PARAM)
        }

        val param = holder.single!!
        val midList = if (param.meterId.isNullOrBlank())
            param.meterIdList!!
        else
            param.meterIdList.orEmpty().plus(param.meterId!!)

        val rightName = EffService.BASE_PATH + EffService.PATH_REPLACE_METER_EFF
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }
            val task = EffTask().apply {
                taskName = login.single?.userName
                createBy = login.single?.userId

                firmId = login.single!!.firmId
                firmName = login.single!!.firmName
                taskStart = param.taskStart ?: EffTaskBean.DUMMY_START.toDate()
                taskEnd = param.taskEnd ?: EffTaskBean.DUMMY_END.toDate()

                effMapper!!.createEffTask(this)
            }

            val effList = arrayListOf<EffMeter>()

            var cnt = 0
            meterMapper!!.selectMeterDma(MeterParam().apply {
                meterIdList = midList
            }).forEach {
                effTaskBean!!.fillPointList(it)

                effList.addAll(if (param.jodaTaskStart == null || param.jodaTaskEnd == null)
                    effTaskBean!!.effMeter(it, task, 31)
                else
                    effTaskBean!!.effMeterRange(it, param.jodaTaskStart!!, param.jodaTaskEnd!!, task)
                )

                ++cnt
            }

            return BwResult(effList.toList()).also {
                it.error = "分析计量效率: ${cnt}只水表"
            }
        } catch (ex: Exception) {
            lgr.error(ex.message, ex)
            return BwResult(1, "${ERR_INTERNAL} ${ex.message}")
        }
    }

    /**
     * 分析水表的计量效率, 可以是整个水司, 一只或多只水表, 不指定时段 或 指定时段的计量效率.
     * @see EffParam.taskStart
     * @see EffParam.taskEnd
     */
    override fun pushEffTask(holder: BwHolder<EffParam>): BwResult<EffTask> {
        TODO("Not yet implemented")
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(EffServiceImpl::class.java)

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