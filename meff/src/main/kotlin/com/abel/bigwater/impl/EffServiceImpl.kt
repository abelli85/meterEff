package com.abel.bigwater.impl

import com.abel.bigwater.api.BwHolder
import com.abel.bigwater.api.BwResult
import com.abel.bigwater.api.EffParam
import com.abel.bigwater.api.EffService
import com.abel.bigwater.mapper.EffMapper
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
        TODO("Not yet implemented")
    }

    /**
     * 列出水表的分析结果
     */
    override fun addMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter> {
        TODO("Not yet implemented")
    }

    /**
     * 列出水表的分析结果
     */
    override fun deleteMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter> {
        TODO("Not yet implemented")
    }

    /**
     * 列出水表的分析结果
     */
    override fun updateMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter> {
        TODO("Not yet implemented")
    }

    /**
     * 获取单个水表的分析详情
     */
    override fun fetchMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter> {
        TODO("Not yet implemented")
    }

    /**
     * 单个水表的分析详情
     */
    override fun replaceMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter> {
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