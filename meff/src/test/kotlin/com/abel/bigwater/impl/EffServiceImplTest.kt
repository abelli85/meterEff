package com.abel.bigwater.impl

import com.abel.bigwater.TestHelper
import com.abel.bigwater.api.*
import com.abel.bigwater.mapper.EffMapper
import com.abel.bigwater.mapper.MeterMapper
import com.abel.bigwater.model.eff.*
import com.alibaba.fastjson.JSON
import org.apache.dubbo.remoting.http.servlet.ServletManager
import org.joda.time.LocalDate
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockServletContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.fail

@ContextConfiguration(locations = ["classpath:/spring/rest-provider.xml", "classpath:/spring-mybatis.xml"])
@RunWith(SpringJUnit4ClassRunner::class)
class EffServiceImplTest {
    @Autowired
    var effService: EffService? = null

    @Autowired
    var effMapper: EffMapper? = null

    @Autowired
    var loginService: UserService? = null

    @Autowired
    var meterMapper: MeterMapper? = null

    @Test
    fun createEffTask() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")
        val t = EffTask().apply {
            taskName = "季度分析任务"
            taskStart = LocalDate(2020, 7, 1).toDate()
            taskEnd = LocalDate(2020, 9, 30).toDate()
            periodTypeObj = EffPeriodType.Day
        }

        try {
            effService!!.createEffTask(BwHolder(TestHelper.buildLoginRequest(login), t)).also {
                lgr.info("create task: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }
        } finally {
            if (t.taskId ?: -1 > 0) {
                effMapper!!.deleteEffTask(EffParam().apply {
                    taskId = t.taskId
                })
            }
        }
    }

    @Test
    fun createEffTaskMeter() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        try {
            effService!!.createEffTask(BwHolder(TestHelper.buildLoginRequest(login), task)).also {
                lgr.info("create task: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }
        } finally {
            if (task.taskId ?: -1 > 0) {
                effMapper!!.deleteEffMeter(EffParam().apply {
                    taskId = task.taskId
                })
                effMapper!!.deleteEffTask(EffParam().apply {
                    taskId = task.taskId
                })
            }
        }
    }

    @Test
    fun listEffTask() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        effService!!.listEffTask(BwHolder(TestHelper.buildLoginRequest(login), EffParam())).also {
            lgr.info("list task: {}", JSON.toJSONString(it, true))
            assertEquals(0, it.code)
        }
    }

    @Test
    fun listEffTaskDemo() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        val t1 = EffTask(
                taskName = "test eff",
                firmId = "76",
                firmName = "test firm",
                meterCount = 1
        ).apply {
            taskStart = LocalDate(2020, 9, 1).toDate()
            taskEnd = LocalDate(2020, 9, 2).toDate()
            periodTypeObj = EffPeriodType.Day
        }
        effMapper!!.createEffTask(t1)

        try {

            effService!!.listEffTask(BwHolder(TestHelper.buildLoginRequest(login), EffParam())).also {
                lgr.info("list task: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }
        } finally {
            effMapper!!.deleteEffTask(EffParam().apply {
                taskId = t1.taskId ?: -1
            })
        }
    }

    @Test
    fun fetchEffTask() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        val t1 = EffTask(
                taskName = "test eff",
                firmId = "27",
                firmName = "test firm",
                meterCount = 1
        ).apply {
            taskStart = LocalDate(2020, 9, 1).toDate()
            taskEnd = LocalDate(2020, 9, 2).toDate()
            periodTypeObj = EffPeriodType.Day
        }
        effMapper!!.createEffTask(t1)

        try {

            effService!!.fetchEffTask(BwHolder(TestHelper.buildLoginRequest(login), EffParam().also {
                it.taskId = t1.taskId
            })).also {
                lgr.info("eff task: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }
        } finally {
            effMapper!!.deleteEffMeter(EffParam().apply {
                taskId = t1.taskId ?: -1
            })
            effMapper!!.deleteEffTask(EffParam().apply {
                taskId = t1.taskId ?: -1
            })
        }
    }

    @Test
    fun listMeterEff() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        try {
            effService!!.createEffTask(BwHolder(TestHelper.buildLoginRequest(login), task)).also {
                lgr.info("create task: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }

            effService!!.listMeterEff(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
                meterIdList = listOf("0123", "0129")
            })).also {
                lgr.info("list meter-eff: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
                assertEquals(2, it.list?.size)
            }
        } finally {
            if (task.taskId ?: -1 > 0) {
                effMapper!!.deleteEffMeter(EffParam().apply {
                    taskId = task.taskId
                })
                effMapper!!.deleteEffTask(EffParam().apply {
                    taskId = task.taskId
                })
            }
        }
    }

    @Test
    fun testListMeterEff() {
        val login = TestHelper.login(loginService, "fuzhou").single ?: fail("fail to login")

        effService!!.listMeterEff(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
            firmId = "76%"
        })).also {
            lgr.info("meter eff list: {}", JSON.toJSONString(it, true))
        }

        effService!!.listMeterEff(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
            firmId = "76%"
            periodTypeObj = EffPeriodType.Month
        })).also {
            lgr.info("meter eff list: {}", JSON.toJSONString(it, true))
        }
    }

    @Test
    fun fetchMeterEff() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        try {
            effService!!.createEffTask(BwHolder(TestHelper.buildLoginRequest(login), task)).also {
                lgr.info("create task: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }

            effService!!.fetchEffTask(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
                taskId = task.taskId
            })).also {
                lgr.info("fetch eff-task: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
                assertNotNull(it.single)
                assertEquals(task.meterList?.size, it.single?.meterList?.size)
            }
        } finally {
            if (task.taskId ?: -1 > 0) {
                effMapper!!.deleteEffMeter(EffParam().apply {
                    taskId = task.taskId
                })
                effMapper!!.deleteEffTask(EffParam().apply {
                    taskId = task.taskId
                })
            }
        }
    }

    @Test
    fun addMeterEff() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        try {
            effService!!.createEffTask(BwHolder(TestHelper.buildLoginRequest(login), task)).also {
                lgr.info("create task: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }

            task2.meterList!!.forEach {
                it.taskId = task.taskId
            }
            effService!!.addMeterEff(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
                meterList = task2.meterList
            }))

            effService!!.fetchEffTask(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
                taskId = task.taskId
            })).also {
                lgr.info("fetch eff-task: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
                assertNotNull(it.single)
                assertEquals(task.meterList!!.size + task2.meterList!!.size, it.single?.meterList?.size)
            }
        } finally {
            if (task.taskId ?: -1 > 0) {
                effMapper!!.deleteEffMeter(EffParam().apply {
                    taskId = task.taskId
                })
                effMapper!!.deleteEffTask(EffParam().apply {
                    taskId = task.taskId
                })
            }
        }
    }

    @Test
    fun deleteMeterEff() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        try {
            effService!!.createEffTask(BwHolder(TestHelper.buildLoginRequest(login), task)).also {
                lgr.info("create task: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }

            effService!!.deleteMeterEff(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
                taskId = task.taskId
                meterId = task.meterList?.firstOrNull()?.meterId
            }))

            effService!!.fetchEffTask(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
                taskId = task.taskId
            })).also {
                lgr.info("fetch eff-task: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
                assertNotNull(it.single)
                assertEquals(task.meterList!!.size - 1, it.single?.meterList?.size)
            }
        } finally {
            if (task.taskId ?: -1 > 0) {
                effMapper!!.deleteEffMeter(EffParam().apply {
                    taskId = task.taskId
                })
                effMapper!!.deleteEffTask(EffParam().apply {
                    taskId = task.taskId
                })
            }
        }
    }

    @Test
    fun updateMeterEff() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        try {
            effService!!.createEffTask(BwHolder(TestHelper.buildLoginRequest(login), task)).also {
                lgr.info("create task: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }

            effService!!.updateMeterEff(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
                taskId = task.taskId
                meterList = task.meterList!!.take(2).also {
                    it.forEach { m ->
                        m.meterEff = 0.99
                    }
                }
            })).also {
                lgr.info("update result: {}", JSON.toJSONString(it))
                assertEquals(0, it.code)
            }

            effService!!.fetchEffTask(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
                taskId = task.taskId
            })).also {
                lgr.info("fetch eff-task: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
                assertNotNull(it.single)
                assertEquals(task.meterList!!.size, it.single?.meterList?.size)
                Assert.assertEquals(0.99, it.single?.meterList?.first { m -> m.meterId == task.meterList?.firstOrNull()?.meterId }?.meterEff!!,
                        1.0E-5)
            }
        } finally {
            if (task.taskId ?: -1 > 0) {
                effMapper!!.deleteEffMeter(EffParam().apply {
                    taskId = task.taskId
                })
                effMapper!!.deleteEffTask(EffParam().apply {
                    taskId = task.taskId
                })
            }
        }
    }

    @Test
    fun testEffMeterPoint() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        try {
            effService!!.createEffTask(BwHolder(TestHelper.buildLoginRequest(login), task)).also {
                lgr.info("create task: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }

            effService!!.replaceMeterEff(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
                taskId = task.taskId
                meterId = task.meterList!!.first().meterId!!
                pointEffList = listOf(
                        EffMeterPoint().also {
                            it.taskId = task.taskId
                            it.meterId = meterId
                            it.pointName = "Q1"
                            it.pointNo = 1
                            it.pointFlow = 1.25
                            it.pointDev = 0.03
                        },
                        EffMeterPoint().also {
                            it.taskId = task.taskId
                            it.meterId = meterId
                            it.pointName = "Q3"
                            it.pointNo = 3
                            it.pointFlow = 100.0
                            it.pointDev = 0.02
                        })
            })).also {
                lgr.info("replace result: {}", JSON.toJSONString(it))
                assertEquals(0, it.code)
            }

            effService!!.fetchMeterEff(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
                taskId = task.taskId
                meterId = task.meterList!!.first().meterId!!
            })).also {
                lgr.info("fetch eff-point: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
                assertNotNull(it.single)
                assertEquals(2, it.single?.pointEffList?.size)
            }
        } finally {
            if (task.taskId ?: -1 > 0) {
                effMapper!!.deleteEffPoint(EffParam().apply {
                    taskId = task.taskId
                })
                effMapper!!.deleteEffMeter(EffParam().apply {
                    taskId = task.taskId
                })
                effMapper!!.deleteEffTask(EffParam().apply {
                    taskId = task.taskId
                })
            }
        }
    }

    @Test
    fun testEffMeter() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        try {
            effMapper!!.insertEffDecay(EffParam().apply {
                decayList = listOf(VcEffDecay().also {
                    it.sizeId = 0
                    it.modelSize = "0"
                    it.sizeName = "0"

                    it.totalFwd = 1E6
                    it.decayEff = 10.0
                })
            })

            kotlin.run {
                val r1 = effService!!.buildMeterEff(BwHolder(TestHelper.buildLoginRequest(login),
                        EffParam().apply {
                            meterIdList = listOf("hello", "world")
                        }))
                lgr.info("build eff result: {}", JSON.toJSONString(r1, true))
                assertEquals(0, r1.code)
            }

            kotlin.run {
                val r2 = effService!!.buildMeterEff(BwHolder(TestHelper.buildLoginRequest(login),
                        EffParam().apply {
                            meterId = "fz-JTIJ1900015"
                        }))
                lgr.info("build eff result: {}", JSON.toJSONString(r2, true))
                assertEquals(0, r2.code)
            }
        } finally {
            effMapper!!.deleteEffDecaySingle(VcEffDecay().apply {
                sizeId = 0
                modelSize = "0"
            })
        }
    }

    @Test
    fun testListDecay() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        val r1 = effService!!.selectEffDecay(BwHolder(TestHelper.buildLoginRequest(login),
                EffParam()))
        lgr.info("decay list: {}", JSON.toJSONString(r1, true))
    }

    @Test
    fun testInsertDecay() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")
        val dlist = listOf(VcEffDecay().apply {
            meterBrandId = "brand-1"
            meterBrandName = "brand-1"

            sizeId = 1
            sizeName = "test1"

            modelSize = "model1"
        }, VcEffDecay().apply {
            meterBrandId = "brand-2"
            meterBrandName = "brand-2"

            sizeId = 2
            sizeName = "test2"

            modelSize = "model2"
        })

        try {
            val r1 = effService!!.insertEffDecay(BwHolder(TestHelper.buildLoginRequest(login),
                    EffParam().also {
                        it.decayList = dlist
                    }))
            lgr.info("decay list: {}", JSON.toJSONString(dlist, true))
        } finally {
            var cnt = 0
            dlist.forEach {
                cnt += effMapper!!.deleteEffDecaySingle(it)
            }
            lgr.info("delete test decay: $cnt")
            assertEquals(2, cnt)
        }
    }

    @Test
    fun testDeleteDecay() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")
        val dlist = listOf(VcEffDecay().apply {
            meterBrandId = "brand-1"
            meterBrandName = "brand-1"

            sizeId = 1
            sizeName = "test1"

            modelSize = "model1"
        }, VcEffDecay().apply {
            meterBrandId = "brand-2"
            meterBrandName = "brand-2"

            sizeId = 2
            sizeName = "test2"

            modelSize = "model2"
        })

        try {
            kotlin.run {
                val r1 = effService!!.insertEffDecay(BwHolder(TestHelper.buildLoginRequest(login),
                        EffParam().also {
                            it.decayList = dlist
                        }))
                lgr.info("decay list: {}", JSON.toJSONString(r1, true))
            }

            val r1 = effService!!.selectEffDecay(BwHolder(TestHelper.buildLoginRequest(login),
                    EffParam()))
            val brandList = dlist.map { it.meterBrandId!! }
            r1.list = r1.list?.filter {
                brandList.contains(it.meterBrandId)
            }
            lgr.info("decay list: {}", JSON.toJSONString(r1, true))
            assertEquals(2, r1.list?.size)

            val r2 = effService!!.deleteEffDecay(BwHolder(TestHelper.buildLoginRequest(login),
                    EffParam().also {
                        it.decayList = dlist
                    }))
            lgr.info("decay list: {}", JSON.toJSONString(r2, true))
        } finally {
            var cnt = 0
            dlist.forEach {
                cnt += effMapper!!.deleteEffDecaySingle(it)
            }
            lgr.info("delete test decay: $cnt")
            assertEquals(0, cnt)
        }
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(EffServiceImplTest::class.java)
        private val task = EffTask().apply {
            taskName = "季度分析任务"
            taskStart = LocalDate(2020, 7, 1).toDate()
            taskEnd = LocalDate(2020, 9, 30).toDate()
            periodTypeObj = EffPeriodType.Day

            meterList = listOf(
                    EffMeter().also {
                        it.meterId = "0123"
                        it.meterName = "test 3"
                        it.taskName = taskName
                        it.taskStart = taskStart
                        it.taskEnd = taskEnd
                        it.periodTypeObj = EffPeriodType.Day

                        it.sizeId = 40
                        it.sizeName = "DN40"
                    },
                    EffMeter().also {
                        it.meterId = "0127"
                        it.meterName = "test 7"
                        it.taskName = taskName
                        it.taskStart = taskStart
                        it.taskEnd = taskEnd
                        it.periodTypeObj = EffPeriodType.Day

                        it.sizeId = 50
                        it.sizeName = "DN5"
                    },
                    EffMeter().also {
                        it.meterId = "0129"
                        it.meterName = "test 9"
                        it.taskName = taskName
                        it.taskStart = taskStart
                        it.taskEnd = taskEnd
                        it.periodTypeObj = EffPeriodType.Day

                        it.sizeId = 50
                        it.sizeName = "DN5"
                    }
            )
        }

        private val task2 = EffTask().apply {
            taskName = "季度分析任务"
            taskStart = LocalDate(2020, 7, 1).toDate()
            taskEnd = LocalDate(2020, 9, 30).toDate()
            periodTypeObj = EffPeriodType.Day

            meterList = listOf(
                    EffMeter().also {
                        it.meterId = "1123"
                        it.meterName = "test 13"
                        it.taskName = taskName
                        it.taskStart = taskStart
                        it.taskEnd = taskEnd
                        it.periodTypeObj = EffPeriodType.Day

                        it.sizeId = 40
                        it.sizeName = "DN40"
                    },
                    EffMeter().also {
                        it.meterId = "1127"
                        it.meterName = "test 17"
                        it.taskName = taskName
                        it.taskStart = taskStart
                        it.taskEnd = taskEnd
                        it.periodTypeObj = EffPeriodType.Day

                        it.sizeId = 50
                        it.sizeName = "DN5"
                    }
            )
        }

        @BeforeClass
        @JvmStatic
        fun addServletContext() {
            lgr.warn("prepare servlet container...")
            ServletManager.getInstance().addServletContext(ServletManager.EXTERNAL_SERVER_PORT, MockServletContext())
        }

    }
}