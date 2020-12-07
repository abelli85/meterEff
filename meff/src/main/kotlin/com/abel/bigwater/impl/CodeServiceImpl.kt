package com.abel.bigwater.impl

import com.abel.bigwater.Helper
import com.abel.bigwater.api.*
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
        const val ERR_PARAM = "参数不完整"
        const val ERR_INTERNAL = "内部错误:"
        const val ERR_PREINIT = "预置设备不允许删除"

        const val EXPIRED_SESSION = "会话已失效，请注销后重新登录"

        const val WARN_NO_RIGHT = "无权执行该操作: "

        const val CANNOT_LOGIN_WEB = "抱歉，您不具有登录网页系统的权限！"

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

            lgr.info("修改代码值顺序:{}", codeMapper!!.updateValueOrder(CodeParam().apply {
                userId = ul.single?.userId
                valueList = lst
            }))

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
     * 所有厂商. 总是返回全部厂商。
     */
    override fun listFactory(holder: BwHolder<VcFactory>): BwResult<VcFactory> {
        return BwResult(codeMapper!!.listFactory())
    }

    /**
     * 新增厂商. 只能逐个添加，必填：
     * @see VcFactory.factId
     * @see VcFactory.factName
     */
    override fun insertFactory(holder: BwHolder<VcFactory>): BwResult<VcFactory> {
        if (holder.lr == null || holder.single?.factId.isNullOrBlank()
                || holder.single?.factName.isNullOrBlank()) {
            return BwResult(1, ERR_PARAM)
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
     * 移除厂商. 只能逐个删除，且预置厂商无法删除。必填：
     * @see VcFactory.factId
     */
    override fun deleteFactory(holder: BwHolder<VcFactory>): BwResult<VcFactory> {
        if (holder.lr == null || holder.single?.factId.isNullOrBlank()) {
            return BwResult(1, ERR_PARAM)
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
     * 默认为 '1'
     */
    override fun listMeterType(holder: BwHolder<VcMeterType>): BwResult<VcMeterType> {
        return BwResult(codeMapper!!.listMeterType())
    }

    /**
     * 新增水表类型
     * 默认为 '1'
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
     * 默认为 '1'
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
     * 厂家的水表型号. 如下字段可选：
     * @see VcFactory.factId - 不填写，返回全部厂家的全部类型；填写，返回指定厂家的类型。
     */
    override fun listFactoryModel(holder: BwHolder<VcFactory>): BwResult<VcFactoryModel> {
        return BwResult(codeMapper!!.listFactoryModel(holder.single!!))
    }

    /**
     * 新增厂家的水表型号, 可以填充:
     * @see BwHolder.single - 单个增加
     * @see BwHolder.list - 批量增加
     * 对每个型号，必须填充：
     * @see VcFactoryModel.factId
     * @see VcFactoryModel.modelSize
     * @see VcFactoryModel.sizeId
     * 型号的下列字段可不填充：
     * @see VcFactoryModel.valueOrder - 显示顺序，如果不填写，按自然顺序。
     * @see VcFactoryModel.typeId - 不填充的时候默认为 '1'
     * @see VcFactoryModel.preInit - 无需填写，后台自动填充为 false.
     */
    override fun insertFactoryModel(holder: BwHolder<VcFactoryModel>): BwResult<VcFactoryModel> {
        if (holder.lr == null || (holder.single == null && holder.list.isNullOrEmpty())) {
            return BwResult(1, ERR_PARAM);
        }

        val rn = CodeService.URL_BASE + CodeService.PATH_CREATE_VALUE
        try {
            val ul = loginManager!!.verifySession(holder.lr!!, rn, rn, JSON.toJSONString(holder.list))
            if (ul.code != 0) return BwResult(ul.code, ul.error!!)

            val lst = if (holder.single != null)
                listOf(holder.single!!)
            else
                holder.list!!
            lst.forEach {
                if (it.factId.isNullOrBlank() || it.modelSize.isNullOrBlank() || it.sizeId ?: 0 < 1) {
                    return BwResult(2, "$ERR_PARAM: [${it.factId}, ${it.modelSize}, ${it.sizeId}]")
                }
                if (it.typeId.isNullOrBlank()) it.typeId = "1"
            }

            val cnt = codeMapper!!.insertFactoryModelBatch(VcFactory().apply {
                modelList = lst
            })

            return BwResult(0, "创建型号规格($cnt): ${holder.single?.modelSize}")
        } catch (ex: Exception) {
            lgr.error("create fact-model fail", ex)
            return BwResult(1, "内部错误:${ex.message?.take(100)}")
        }
    }

    /**
     * 移除厂家的水表型号. 只能删除非预置的型号. 只能逐个厂家删除，必填：
     * @see VcFactoryModel.factId
     * @see VcFactoryModel.modelSize - 可选。不填写表示 删除这个厂家的所有型号的所有口径。
     * @see VcFactoryModel.sizeId - 可选。不填写表示 删除这个厂家的指定型号的所有口径。
     */
    override fun deleteFactoryModel(holder: BwHolder<VcFactoryModel>): BwResult<VcFactoryModel> {
        if (holder.lr == null || holder.single?.factId.isNullOrBlank()) {
            return BwResult(1, ERR_PARAM)
        }

        val rn = CodeService.URL_BASE + CodeService.PATH_DELETE_VALUE
        try {
            val ul = loginManager!!.verifySession(holder.lr!!, rn, rn, JSON.toJSONString(holder.list))
            if (ul.code != 0) return BwResult(ul.code, ul.error!!)

            var cnt = codeMapper!!.deleteFactoryModel(holder.single!!)

            val fm = holder.single!!
            return if (cnt > 0) BwResult(0, "删除规格型号($cnt): ${fm.factId}/${fm.modelSize}")
            else BwResult(3, "预置规格型号无法删除: ${fm.factId}/${fm.modelSize}")
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