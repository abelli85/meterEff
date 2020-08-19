package com.abel.bigwater.impl

import com.abel.bigwater.Helper
import com.abel.bigwater.api.BwHolder
import com.abel.bigwater.api.BwResult
import com.abel.bigwater.api.CodeService
import com.abel.bigwater.api.UserOperParam
import com.abel.bigwater.mapper.CodeMapper
import com.abel.bigwater.mapper.ConfigMapper
import com.abel.bigwater.model.*
import com.alibaba.fastjson.JSON
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("codeService")
open class CodeServiceImpl : CodeService {
    companion object {
        const val ERR_PARAM = "无效参数"
        const val ERR_PREINIT = "预置设备不允许删除"
        private val lgr = LoggerFactory.getLogger(CodeServiceImpl::class.java)
    }

    @Autowired
    var codeMapper: CodeMapper? = null

    @Autowired
    var loginManager: LoginManager? = null

    @Autowired
    private var configMapper: ConfigMapper? = null

    /**
     * 列出所有代码类
     */
    override fun listCode(holder: BwHolder<VcCode>): BwResult<VcCode> {
        return BwResult(codeMapper!!.listCode())
    }

    /**
     * 列出指定代码的值列表
     */
    override fun listValue(holder: BwHolder<VcCode>): BwResult<VcCodeValue> {
        return BwResult(codeMapper!!.listValue(holder.single!!))
    }

    /**
     * 创建代码值
     * @see BwHolder.list
     */
    override fun createValue(holder: BwHolder<VcCodeValue>): BwResult<VcCodeValue> {
        if (holder.lr == null || (holder.single == null && holder.list.isNullOrEmpty())) {
            return BwResult(1, ERR_PARAM)
        }

        val rn = CodeService.URL_BASE + CodeService.PATH_CREATE_VALUE
        try {
            val ul = loginManager!!.verifySession(holder.lr!!, rn, rn, JSON.toJSONString(holder.list))
            if (ul.code != 0) return BwResult(ul.code, ul.error!!)

            var cnt = 0
            val lst = if (holder.single == null) holder.list!! else listOf(holder.single!!)
            lst.forEach {
                cnt += codeMapper!!.createValue(it)
                codeMapper!!.disableValue(it)
            }

            return BwResult(0, "创建代码:$cnt")
        } catch (ex: Exception) {
            lgr.error("create value fail", ex)
            return BwResult(1, "内部错误:${ex.message?.take(100)}")
        }
    }

    /**
     * 修改代码值, 代码值:
     * @see BwHolder.list
     */
    override fun updateValue(holder: BwHolder<VcCodeValue>): BwResult<VcCodeValue> {
        if (holder.lr == null || (holder.single == null && holder.list.isNullOrEmpty())) {
            return BwResult(1, "无效参数")
        }

        val rn = CodeService.URL_BASE + CodeService.PATH_UPDATE_VALUE
        try {
            val ul = loginManager!!.verifySession(holder.lr!!, rn, rn, JSON.toJSONString(holder.list))
            if (ul.code != 0) return BwResult(ul.code, ul.error!!)

            var cnt = 0
            val lst = if (holder.single == null) holder.list!! else listOf(holder.single!!)
            lst.forEach {
                cnt += codeMapper!!.updateValue(it)
                cnt += codeMapper!!.disableValue(it)
            }

            return BwResult(0, "更新代码:$cnt")
        } catch (ex: Exception) {
            lgr.error("update value fail", ex)
            return BwResult(1, "内部错误:${ex.message?.take(100)}")
        }
    }

    /**
     * 删除代码值, 代码值:
     * @see BwHolder.list
     */
    override fun deleteValue(holder: BwHolder<VcCodeValue>): BwResult<VcCodeValue> {
        if (holder.lr == null || (holder.single == null && holder.list.isNullOrEmpty())) {
            return BwResult(1, "无效参数")
        }

        val rn = CodeService.URL_BASE + CodeService.PATH_DELETE_VALUE
        try {
            val ul = loginManager!!.verifySession(holder.lr!!, rn, rn, JSON.toJSONString(holder.list))
            if (ul.code != 0) return BwResult(ul.code, ul.error!!)

            var cnt = 0
            val lst = if (holder.single == null) holder.list!! else listOf(holder.single!!)
            lst.forEach {
                cnt += codeMapper!!.deleteValue(it)
            }

            return if (cnt > 0) BwResult(0, "删除代码:$cnt")
            else BwResult(4, ERR_PREINIT.plus(": ").plus(lst.joinToString { it.valueId.orEmpty() }))
        } catch (ex: Exception) {
            lgr.error("delete value fail", ex)
            return BwResult(1, "内部错误:${ex.message?.take(100)}")
        }
    }

    /**
     * 所有厂商
     */
    override fun listFactory(holder: BwHolder<VcFactory>): BwResult<VcFactory> {
        return BwResult(codeMapper!!.listFactory())
    }

    /**
     * 新增厂商
     */
    override fun insertFactory(holder: BwHolder<VcFactory>): BwResult<VcFactory> {
        if (holder.lr == null || holder.single == null) {
            return BwResult(1, "无效参数")
        }

        val rn = CodeService.URL_BASE + CodeService.PATH_CREATE_VALUE
        try {
            val ul = loginManager!!.verifySession(holder.lr!!, rn, rn, JSON.toJSONString(holder.list))
            if (ul.code != 0) return BwResult(ul.code, ul.error!!)

            var cnt = codeMapper!!.insertFactory(holder.single!!)

            return BwResult(0, "创建厂家:$cnt")
        } catch (ex: Exception) {
            lgr.error("create fact fail", ex)
            return BwResult(1, "内部错误:${ex.message?.take(100)}")
        }
    }

    /**
     * 移除厂商
     */
    override fun deleteFactory(holder: BwHolder<VcFactory>): BwResult<VcFactory> {
        if (holder.lr == null || holder.single == null) {
            return BwResult(1, "无效参数")
        }

        val rn = CodeService.URL_BASE + CodeService.PATH_DELETE_VALUE
        try {
            val ul = loginManager!!.verifySession(holder.lr!!, rn, rn, JSON.toJSONString(holder.list))
            if (ul.code != 0) return BwResult(ul.code, ul.error!!)

            var cnt = codeMapper!!.deleteFactory(holder.single!!)

            return if (cnt == 1) BwResult(0, "删除厂家:$cnt")
            else BwResult(3, "预置厂家无法删除: ${holder.single?.factId}")
        } catch (ex: Exception) {
            lgr.error("delete fact fail", ex)
            return BwResult(1, "内部错误:${ex.message?.take(100)}")
        }
    }

    /**
     * 水表类型
     */
    override fun listMeterType(holder: BwHolder<VcMeterType>): BwResult<VcMeterType> {
        return BwResult(codeMapper!!.listMeterType())
    }

    /**
     * 新增水表类型
     */
    override fun insertMeterType(holder: BwHolder<VcMeterType>): BwResult<VcMeterType> {
        if (holder.lr == null || holder.single == null) {
            return BwResult(1, "无效参数")
        }

        val rn = CodeService.URL_BASE + CodeService.PATH_CREATE_VALUE
        try {
            val ul = loginManager!!.verifySession(holder.lr!!, rn, rn, JSON.toJSONString(holder.list))
            if (ul.code != 0) return BwResult(ul.code, ul.error!!)

            var cnt = codeMapper!!.insertMeterType(holder.single!!)

            return BwResult(0, "创建类型($cnt): ${holder.single?.typeId}")
        } catch (ex: Exception) {
            lgr.error("create meter-type fail", ex)
            return BwResult(1, "内部错误:${ex.message?.take(100)}")
        }
    }

    /**
     * 移除水表类型
     */
    override fun deleteMeterType(holder: BwHolder<VcMeterType>): BwResult<VcMeterType> {
        if (holder.lr == null || holder.single == null) {
            return BwResult(1, "无效参数")
        }

        val rn = CodeService.URL_BASE + CodeService.PATH_DELETE_VALUE
        try {
            val ul = loginManager!!.verifySession(holder.lr!!, rn, rn, JSON.toJSONString(holder.list))
            if (ul.code != 0) return BwResult(ul.code, ul.error!!)

            var cnt = codeMapper!!.deleteMeterType(holder.single!!)

            return if (cnt == 1) BwResult(0, "删除类型($cnt): ${holder.single?.typeId}")
            else BwResult(3, "预置类型无法删除: ${holder.single?.typeId}")
        } catch (ex: Exception) {
            lgr.error("delete meter-type fail", ex)
            return BwResult(1, "内部错误:${ex.message?.take(100)}")
        }
    }

    /**
     * 厂家的水表型号
     */
    override fun listFactoryModel(holder: BwHolder<VcFactory>): BwResult<VcFactoryModel> {
        return BwResult(codeMapper!!.listFactoryModel(holder.single!!))
    }

    /**
     * 新增厂家的水表型号
     */
    override fun insertFactoryModel(holder: BwHolder<VcFactoryModel>): BwResult<VcFactoryModel> {
        if (holder.lr == null || holder.single == null) {
            return BwResult(1, "无效参数")
        }

        val rn = CodeService.URL_BASE + CodeService.PATH_CREATE_VALUE
        try {
            val ul = loginManager!!.verifySession(holder.lr!!, rn, rn, JSON.toJSONString(holder.list))
            if (ul.code != 0) return BwResult(ul.code, ul.error!!)

            var cnt = codeMapper!!.insertFactoryModel(holder.single!!)

            return BwResult(0, "创建型号规格($cnt): ${holder.single?.modelSize}")
        } catch (ex: Exception) {
            lgr.error("create fact-model fail", ex)
            return BwResult(1, "内部错误:${ex.message?.take(100)}")
        }
    }

    /**
     * 移除厂家的水表型号
     */
    override fun deleteFactoryModel(holder: BwHolder<VcFactoryModel>): BwResult<VcFactoryModel> {
        if (holder.lr == null || holder.single == null) {
            return BwResult(1, "无效参数")
        }

        val rn = CodeService.URL_BASE + CodeService.PATH_DELETE_VALUE
        try {
            val ul = loginManager!!.verifySession(holder.lr!!, rn, rn, JSON.toJSONString(holder.list))
            if (ul.code != 0) return BwResult(ul.code, ul.error!!)

            var cnt = codeMapper!!.deleteFactoryModel(holder.single!!)

            return if (cnt == 1) BwResult(0, "删除规格型号($cnt): ${holder.single?.modelSize}")
            else BwResult(3, "预置规格型号无法删除: ${holder.single?.modelSize}")
        } catch (ex: Exception) {
            lgr.error("delete fact-model fail", ex)
            return BwResult(1, "内部错误:${ex.message?.take(100)}")
        }
    }

    /**
     * 列出系统配置项
     */
    override fun configList(holder: BwHolder<UserOperParam>): BwResult<BwConfig> {
        lgr.info("${holder.lr?.userId} try to stat oper: ${JSON.toJSONString(holder.single)}")
        if (holder.single == null) return BwResult(2, "参数不能为空")

        val rightName = CodeService.URL_BASE + "/configList"
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            val _firmId = Helper.refineFirmId(holder.single?.firmId, login.single?.firmId!!)
            val clist = configMapper?.configList(_firmId + "%", null, null).orEmpty().toMutableList()
            CodeService.CONFIG_LIST.forEach { cfg ->
                if (clist.find { it.configId == cfg.configId } == null) {
                    clist.add(cfg.clone().apply { firmId = _firmId })
                }
            }

            return BwResult(clist)
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 更改系统配置项
     */
    override fun updateConfig(holder: BwHolder<BwConfig>): BwResult<BwConfig> {
        lgr.info("${holder.lr?.userId} try to stat oper: ${JSON.toJSONString(holder.single)}")
        if (holder.single == null) return BwResult(2, "参数不能为空")

        val rightName = CodeService.URL_BASE + "/updateConfig"
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            val cfg = holder.single!!
            val _firmId = Helper.refineFirmId(holder.single?.firmId, login.single?.firmId!!, false)
            val cnt = configMapper!!.updateConfig(cfg.apply {
                firmId = _firmId
                updateBy = login.single!!.userId
                createBy = login.single!!.userId
            })

            if (cnt ?: 0 < 1) {
                configMapper!!.insertConfig(cfg)
            }

            return BwResult(0, "修改配置项数量: $cnt")
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 插入工作日/节假日
     */
    override fun insertWorkdayHoliday(holder: BwHolder<VcWorkdayHoliday>): BwResult<VcWorkdayHoliday> {
        lgr.info("${holder.lr?.userId} try to stat oper: ${JSON.toJSONString(holder.single)}")
        if (holder.single == null) return BwResult(2, "参数不能为空")

        val rightName = CodeService.URL_BASE + CodeService.PATH_INSERT_WORKDAY_HOLIDAY
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            val t = holder.single!!
            val cnt = codeMapper!!.insertWorkdayHoliday(t)

            return BwResult(0, "增加工作日/节假日: $cnt")
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 删除工作日/节假日
     */
    override fun deleteWorkdayHoliday(holder: BwHolder<VcWorkdayHoliday>): BwResult<VcWorkdayHoliday> {
        lgr.info("${holder.lr?.userId} try to stat oper: ${JSON.toJSONString(holder.single)}")
        if (holder.single == null) return BwResult(2, "参数不能为空")

        val rightName = CodeService.URL_BASE + CodeService.PATH_INSERT_WORKDAY_HOLIDAY
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            val t = holder.single!!
            val cnt = codeMapper!!.deleteWorkdayHoliday(t)

            return BwResult(0, "删除工作日/节假日: $cnt")
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 列出工作日/节假日
     */
    override fun selectWorkdayHoliday(holder: BwHolder<VcWorkdayHoliday>): BwResult<VcWorkdayHoliday> {
        lgr.info("${holder.lr?.userId} try to stat oper: ${JSON.toJSONString(holder.single)}")
        if (holder.single == null) return BwResult(2, "参数不能为空")

        val rightName = CodeService.URL_BASE + CodeService.PATH_INSERT_WORKDAY_HOLIDAY
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            val t = holder.single!!
            val lst = codeMapper!!.selectWorkdayHoliday(t)

            return BwResult(lst)
        } catch (ex: Exception) {
            lgr.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }
}