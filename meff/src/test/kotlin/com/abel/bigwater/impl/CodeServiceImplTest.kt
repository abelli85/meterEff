package com.abel.bigwater.impl

import com.abel.bigwater.TestHelper
import com.abel.bigwater.api.*
import com.abel.bigwater.mapper.CodeMapper
import com.abel.bigwater.mapper.ConfigMapper
import com.abel.bigwater.model.*
import com.alibaba.fastjson.JSON
import org.apache.dubbo.remoting.http.servlet.ServletManager
import org.joda.time.DateTime
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockServletContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import kotlin.test.assertEquals

@ContextConfiguration(locations = ["classpath:/spring/rest-provider.xml", "classpath:/spring-mybatis.xml"])
@RunWith(SpringJUnit4ClassRunner::class)
class CodeServiceImplTest {

    var ul: BwUserLogin? = null

    @Autowired
    var bean: CodeService? = null

    @Autowired
    var loginService: UserService? = null

    @Autowired
    var codeMapper: CodeMapper? = null

    @Autowired
    var configMapper: ConfigMapper? = null

    @Before
    fun login() {
        if (ul != null) return
        ul = TestHelper.login(loginService!!).single
    }

    @Test
    fun listCode() {
        val lst = bean!!.listCode(BwHolder(LoginRequest(), VcCode()))
        lgr.info(JSON.toJSONString(lst))
        assertTrue(lst.list?.size ?: 0 > 0)
    }

    @Test
    fun listValue() {
        val lst = bean!!.listValue(BwHolder(LoginRequest(), VcCode(codeId = "SIZE")))
        lgr.info(JSON.toJSONString(lst, true))
        assertTrue(lst.list?.size ?: 0 > 5)
    }

    @Test
    fun createValue() {
        var holder = BwHolder(TestHelper.buildLoginRequest(ul!!), VcCodeValue().apply {
            codeId = "SIZE"
            valueId = "20"
        })

        val ret = bean?.createValue(holder)

        lgr.info(JSON.toJSONString(ret, true))
        assertEquals(1, ret!!.code)

        // create DN600
        try {
            holder = BwHolder(TestHelper.buildLoginRequest(ul!!), VcCodeValue().apply {
                codeId = "SIZE"
                valueId = "96"
                valueName = "DN600"
                valueOrder = 96
                valueType = VcCodeValue.TYPE_INT
            })
            codeMapper!!.deleteValue(holder.single!!)

            val r2 = bean?.createValue(holder)

            lgr.info(JSON.toJSONString(r2, true))
            assertEquals(0, r2!!.code)
        } finally {
            codeMapper!!.deleteValue(VcCodeValue().apply {
                codeId = "SIZE"
                valueId = "96"
            })
        }

    }

    @Test
    fun updateValue() {
        val c1 = VcCode(codeId = "SIZE")
        val v1 = VcCodeValue().apply {
            codeId = c1.codeId
            valueId = "20"
            disabled = true
        }

        try {
            kotlin.run {
                val r1 = bean!!.listValue(BwHolder(TestHelper.buildLoginRequest(ul!!), c1))
                lgr.info("value list for {}: {}", c1.codeId, JSON.toJSONString(r1, true).take(200))
                assertEquals(0, r1.code)
                assertEquals(false, r1.list?.find { it.valueId == v1.valueId }?.disabled)
            }

            // 禁用
            val r2 = bean!!.updateValue(BwHolder(TestHelper.buildLoginRequest(ul!!), listOf(v1)))
            lgr.info("update value: {}", JSON.toJSONString(r2, true))
            assertEquals(0, r2.code)

            // 再次列出
            kotlin.run {
                val r1 = bean!!.listValue(BwHolder(TestHelper.buildLoginRequest(ul!!), c1))
                assertEquals(0, r1.code)
                assertEquals(true, r1.list?.find { it.valueId == v1.valueId }?.disabled)
            }
        } finally {
            // 改回启用
            codeMapper!!.disableValue(v1.apply { disabled = false })
        }
    }

    /**
     * 删除代码值
     */
    @Test
    fun deleteValue() {
        val v1 = VcCodeValue().apply {
            codeId = "SIZE"
            valueId = "96"
            valueName = "DN900"
            disabled = false
            valueOrder = 99
            valueType = VcCodeValue.TYPE_INT
        }
        val holder = BwHolder(TestHelper.buildLoginRequest(ul!!), v1)
        codeMapper!!.deleteValue(v1)

        val c1 = VcCode()

        kotlin.run {
            val r2 = bean?.createValue(holder)

            lgr.info(JSON.toJSONString(r2, true))
            assertEquals(0, r2!!.code)
        }

        // list
        kotlin.run {
            val r1 = bean!!.listValue(BwHolder(TestHelper.buildLoginRequest(ul!!), c1))
            lgr.info("value list for {}: {}", c1.codeId, JSON.toJSONString(r1, true).take(200))
            assertEquals(0, r1.code)
            assertEquals(false, r1.list?.find { it.codeId == v1.codeId && it.valueId == v1.valueId }?.disabled)
        }

        // delete
        kotlin.run {
            val r1 = bean!!.deleteValue(BwHolder(TestHelper.buildLoginRequest(ul!!), v1))
            lgr.info("delete value {}: {}", c1.codeId, JSON.toJSONString(r1, true).take(200))
            assertEquals(0, r1.code)
        }

        // list
        kotlin.run {
            val r1 = bean!!.listValue(BwHolder(TestHelper.buildLoginRequest(ul!!), c1))
            lgr.info("value list for {}: {}", c1.codeId, JSON.toJSONString(r1, true).take(200))
            assertEquals(0, r1.code)
            assertEquals(null, r1.list?.find { it.codeId == v1.codeId && it.valueId == v1.valueId })
        }
    }

    @Test
    fun listFactory() {
        val lst = bean!!.listFactory(BwHolder(LoginRequest(), VcFactory()))
        lgr.info(JSON.toJSONString(lst))
        assertTrue(lst.list?.size ?: 0 > 5)
    }

    @Test
    fun test21InsertFact() {
        val r1 = bean!!.insertFactory(BwHolder(TestHelper.buildLoginRequest(ul!!), VcFactory().apply {
            factId = "10"
            factName = "test 厂家1"
        }))

        lgr.info("create fact: {}", JSON.toJSONString(r1, true))
        assertEquals(0, r1.code)

        // list
        val lst = bean!!.listFactory(BwHolder(LoginRequest(), VcFactory()))
        lgr.info("list fact: {}", JSON.toJSONString(lst, true))

        // delete
        val r2 = bean!!.deleteFactory(BwHolder(TestHelper.buildLoginRequest(ul!!), VcFactory().apply {
            factId = "10"
        }))
        lgr.info("delete fact: {}", JSON.toJSONString(r2, true))
        assertEquals(0, r2.code)
    }

    @Test
    fun test22DeleteFact() {
        val r2 = bean!!.deleteFactory(BwHolder(TestHelper.buildLoginRequest(ul!!), VcFactory().apply {
            factId = "01"
        }))
        lgr.info("delete fact: {}", JSON.toJSONString(r2, true))
        assertTrue(r2.code > 0)
    }

    @Test
    fun listMeterType() {
        val lst = bean!!.listMeterType(BwHolder(LoginRequest(), VcMeterType()))
        lgr.info(JSON.toJSONString(lst))
        assertTrue(lst.list?.size ?: 0 > 1)
    }

    @Test
    fun test31InsertMeterType() {
        val r1 = bean!!.insertMeterType(BwHolder(TestHelper.buildLoginRequest(ul!!), VcMeterType("3", "test 水表类型")))
        lgr.info("new meter-type: {}", JSON.toJSONString(r1, true))
        assertEquals(0, r1.code)

        kotlin.run {
            val r2 = bean!!.listMeterType(BwHolder(TestHelper.buildLoginRequest(ul!!), VcMeterType()))
            lgr.info("meter-type list: {}", JSON.toJSONString(r2, true))
            assertEquals(0, r2.code)
            assertTrue(r2.list?.find { it.typeId == "3" } != null)
        }

        kotlin.run {
            val r2 = bean!!.deleteMeterType(BwHolder(TestHelper.buildLoginRequest(ul!!), VcMeterType(typeId = "3")))
            lgr.info("delete meter-type: {}", JSON.toJSONString(r2, true))
            assertEquals(0, r2.code)
        }
    }

    @Test
    fun test32DeleteMeterType() {
        kotlin.run {
            val r2 = bean!!.deleteMeterType(BwHolder(TestHelper.buildLoginRequest(ul!!), VcMeterType()))
            lgr.info("delete meter-type: {}", JSON.toJSONString(r2, true))
            assertEquals(true, r2.code > 0)
        }

        kotlin.run {
            val r2 = bean!!.deleteMeterType(BwHolder(TestHelper.buildLoginRequest(ul!!), VcMeterType(typeId = "1")))
            lgr.info("delete meter-type: {}", JSON.toJSONString(r2, true))
            assertEquals(true, r2.code > 0)
        }
    }

    @Test
    fun listFactoryModel() {
        var lst = bean!!.listFactoryModel(BwHolder(LoginRequest(), VcFactory()))
        lgr.info("all models: {}", JSON.toJSONString(lst, true))
        assertTrue(lst.list?.size ?: 0 > 5)

        lst = bean!!.listFactoryModel(BwHolder(LoginRequest(), VcFactory().apply { factId = "01" }))
        lgr.info("models for 01: {}", JSON.toJSONString(lst, true))
        assertTrue(lst.list!!.size >= 2)
    }

    @Test
    fun test41InsertFactModel() {
        val r1 = bean!!.insertFactoryModel(BwHolder(TestHelper.buildLoginRequest(ul!!), VcFactoryModel(
                factId = "01",
                typeId = "1",
                modelSize = "LXS")))
        lgr.info("new fact-model: {}", JSON.toJSONString(r1, true))
        assertEquals(true, r1.code > 0)

        kotlin.run {
            val r2 = bean!!.insertFactoryModel(BwHolder(TestHelper.buildLoginRequest(ul!!), VcFactoryModel(
                    factId = "10",
                    typeId = "1",
                    modelSize = "X123"
            )))
            lgr.info("new fact-model: {}", JSON.toJSONString(r2, true))
            assertEquals(0, r2.code)
        }

        kotlin.run {
            val r2 = bean!!.deleteFactoryModel(BwHolder(TestHelper.buildLoginRequest(ul!!), VcFactoryModel(
                    factId = "10",
                    typeId = "1",
                    modelSize = "X123"
            )))
            lgr.info("delete fact-model: {}", JSON.toJSONString(r2, true))
            assertEquals(0, r2.code)
        }
    }

    @Test
    fun test42DeleteFactModel() {
        kotlin.run {
            val r2 = bean!!.deleteFactoryModel(BwHolder(TestHelper.buildLoginRequest(ul!!), VcFactoryModel(
                    factId = "01",
                    typeId = "1"
            )))
            lgr.info("delete fact-model: {}", JSON.toJSONString(r2, true))
            assertEquals(true, r2.code > 0)
        }

        kotlin.run {
            val r2 = bean!!.deleteFactoryModel(BwHolder(TestHelper.buildLoginRequest(ul!!), VcFactoryModel(
                    factId = "01",
                    typeId = "1",
                    modelSize = "LXS"
            )))
            lgr.info("delete fact-model: {}", JSON.toJSONString(r2, true))
            assertEquals(true, r2.code > 0)
        }
    }

    @Test
    fun configList() {
        ul!!

        val holder = BwHolder(TestHelper.buildLoginRequest(ul!!), UserOperParam())
        val r1 = bean!!.configList(holder)
        lgr.info("config list cascade: {}", JSON.toJSONString(r1, true))
    }

    @Test
    fun updateConfig() {
        configMapper!!.deleteConfig(BwConfig().apply {
            configId = "key_test"
            firmId = ul!!.firmId
        })

        ul!!

        val holder = BwHolder(TestHelper.buildLoginRequest(ul!!), BwConfig().apply {
            configId = "key_test"
            groupId = "test"
            configName = "测试"
            value = "测试值1"
        })
        val r1 = bean!!.updateConfig(holder)
        lgr.info("update config result: {}", JSON.toJSONString(r1, true))

        val cfg = configMapper!!.configList(ul!!.firmId, null, "key_test")
        assertEquals(1, cfg.size)
        configMapper!!.deleteConfig(BwConfig().apply {
            configId = "key_test"
            firmId = ul!!.firmId
        })
    }

    @Test
    fun testWorkday() {
        val h = BwHolder(TestHelper.buildLoginRequest(ul!!), VcWorkdayHoliday())
        val r1 = bean!!.selectWorkdayHoliday(h)
        lgr.info(JSON.toJSONString(r1, true))
        assertTrue(r1.list?.find { it.yr == 2017 } == null)

        // 插入
        kotlin.run {
            val h2 = BwHolder(TestHelper.buildLoginRequest(ul!!), VcWorkdayHoliday(yr = 2017,
                    holiday = true).apply {
                startDate = DateTime(2017, 1, 1, 0, 0).toDate()
                endDate = startDate
            })

            val r2 = bean!!.insertWorkdayHoliday(h2)
            assertEquals(0, r2.code)
        }

        val _wid: Long?
        // 列出
        kotlin.run {
            val h2 = BwHolder(TestHelper.buildLoginRequest(ul!!), VcWorkdayHoliday(yr = 2017))

            val r2 = bean!!.selectWorkdayHoliday(h2)
            lgr.info(JSON.toJSONString(r2, true))
            assertEquals(0, r2.code)
            assertEquals(1, r2.list?.count { it.yr == 2017 })
            _wid = r2.list?.find { it.yr == 2017 }?.wid
        }
        // 存在
        _wid!!

        // 删除
        kotlin.run {
            val h2 = BwHolder(TestHelper.buildLoginRequest(ul!!), VcWorkdayHoliday(wid = _wid))

            val r2 = bean!!.deleteWorkdayHoliday(h2)
            lgr.info(JSON.toJSONString(r2, true))
            assertEquals(0, r2.code)
        }

        // 列出
        kotlin.run {
            val h2 = BwHolder(TestHelper.buildLoginRequest(ul!!), VcWorkdayHoliday(yr = 2017))

            val r2 = bean!!.selectWorkdayHoliday(h2)
            lgr.info(JSON.toJSONString(r2, true))
            assertEquals(0, r2.code)
            assertEquals(0, r2.list?.count { it.yr == 2017 })
        }
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(CodeServiceImplTest::class.java)

        @BeforeClass
        @JvmStatic
        fun addServletContext() {
            lgr.warn("prepare servlet container...")
            ServletManager.getInstance().addServletContext(ServletManager.EXTERNAL_SERVER_PORT, MockServletContext())
        }
    }
}