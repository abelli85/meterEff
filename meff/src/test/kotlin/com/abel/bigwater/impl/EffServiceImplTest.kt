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
import kotlin.jvm.Throws
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
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
    fun listFailureEff() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        try {
            effService!!.listEffFailure(BwHolder(TestHelper.buildLoginRequest(login), EffParam())).also {
                lgr.info("list failure-eff: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }

            effService!!.listEffFailure(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
                taskStart = LocalDate(2020, 7, 1).toDate()
                taskEnd = LocalDate(2020, 8, 1).toDate()
            })).also {
                lgr.info("list failure-eff: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }
        } finally {
            if (task.taskId ?: -1 > 0) {
                effMapper!!.deleteEffFailure(EffParam().apply {
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
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        effService!!.listMeterEff(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
            firmId = "27%"
        })).also {
            lgr.info("meter eff list: {}", JSON.toJSONString(it, true))
            assertEquals(0, it.code)
        }

        effService!!.listMeterEff(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
            firmId = "27%"
            meterIdList = listOf("164")
            periodTypeObj = EffPeriodType.Month
            taskStart = LocalDate(2020, 7, 1).toDate()
            taskEnd = LocalDate(2020, 8, 1).toDate()
        })).also {
            lgr.info("meter eff list: {}", JSON.toJSONString(it, true))
            assertEquals(0, it.code)
        }
    }

    @Test
    fun testReportMeterEff() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        try {
            effService!!.reportMeterEff(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
                taskStart = LocalDate(2020, 10, 1).toDate()
                taskEnd = LocalDate(2020, 11, 1).toDate()
            })).also {
                lgr.info("report meter-eff: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }
        } finally {
        }
    }

    @Test
    fun testListEffRange() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        effService!!.listEffRange(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
            firmId = "27%"
            meterId = "164"
        })).also {
            lgr.info("meter eff range: {}", JSON.toJSONString(it, true))
            assertEquals(0, it.code)
        }
    }

    @Test
    fun testMatchMeter() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        effService!!.matchMeter(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
            firmId = "27%"
            matchQ2v = 80.0
        })).also {
            lgr.info("match meter Day: {}", JSON.toJSONString(it, true))
            assertEquals(0, it.code)
        }

        effService!!.matchMeter(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
            firmId = "76%"
            periodTypeObj = EffPeriodType.Month
            matchQ2v = 10.0
        })).also {
            lgr.info("match meter Month: {}", JSON.toJSONString(it, true))
            assertEquals(0, it.code)
        }
    }

    @Test
    fun fetchMeterEff() {
        val login = TestHelper.login(loginService, "fuzhou", "test").single ?: fail("fail to login")

        effService!!.fetchMeterEff(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
            meterId = "fz-XTI01800001"
        })).also {
            lgr.info("fetch eff-meter: {}", JSON.toJSONString(it, true))
            assertEquals(0, it.code)
        }
    }

    @Test
    fun listEffPoint() {
        val login = TestHelper.login(loginService, "fuzhou", "test").single ?: fail("fail to login")

        effService!!.listEffPoint(BwHolder(TestHelper.buildLoginRequest(login), EffParam())).also {
            lgr.info("eff point list: {}/{}", it.list?.size, JSON.toJSONString(it, true))
            assertEquals(0, it.code)
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
    fun deleteEffFailure() {
        val login = TestHelper.login(loginService, "fuzhou", "test").single ?: fail("fail to login")

        try {
            effService!!.deleteEffFailure(BwHolder(TestHelper.buildLoginRequest(login), EffParam().apply {
                meterId = "fz-%"
            })).also {
                lgr.info("delete eff-failure: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }
        } finally {
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
                            it.effId = 0
                            it.taskStart = LocalDate(2020, 1, 1).toDate()
                            it.taskEnd = LocalDate(2020, 1, 2).toDate()
                            it.pointTypeObj = EffPointType.EFF
                            it.periodTypeObj = EffPeriodType.Day
                            it.meterId = meterId
                            it.pointName = "Q1"
                            it.pointNo = 1
                            it.pointFlow = 1.25
                            it.pointDev = 0.03
                        },
                        EffMeterPoint().also {
                            it.taskId = task.taskId
                            it.effId = 0
                            it.taskStart = LocalDate(2020, 1, 1).toDate()
                            it.taskEnd = LocalDate(2020, 1, 2).toDate()
                            it.pointTypeObj = EffPointType.EFF
                            it.periodTypeObj = EffPeriodType.Day
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
    fun testBuildEffMeter() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")
        val dlist = listOf(VcEffDecay().also {
            it.meterBrandId = "0"
            it.sizeId = 0
            it.modelSize = "0"
            it.sizeName = "0"

            it.totalFwd = 1E6
            it.decayEff = 10.0
        })

        try {
            effMapper!!.insertEffDecay(EffParam().apply {
                decayList = dlist
            })

            kotlin.run {
                val r1 = effService!!.buildMeterEff(BwHolder(TestHelper.buildLoginRequest(login),
                        EffParam().apply {
                            meterIdList = listOf("JTHJ1900010")
                            taskStart = LocalDate(2020, 7, 6).toDate()
                        }))
                lgr.info("build eff result: {}", JSON.toJSONString(r1, true))
                assertEquals(0, r1.code)
            }

            kotlin.run {
                val r2 = effService!!.buildMeterEff(BwHolder(TestHelper.buildLoginRequest(login),
                        EffParam().apply {
                            meterId = "JTHJ1900010"
                            taskStart = LocalDate(2020, 7, 6).toDate()
                        }))
                lgr.info("build eff result: {}", JSON.toJSONString(r2, true))
                assertEquals(0, r2.code)
            }
        } finally {
            effMapper!!.deleteEffDecay(EffParam().also {
                it.decayList = dlist
            })
        }
    }

    @Test
    fun testBuildEffManual() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        effService!!.buildMeterEff(BwHolder(TestHelper.buildLoginRequest(login),
                EffParam().apply {
//                    meterId = "212590"
                    meterCode = "113540524101"
                })).also { r1 ->
            lgr.info("build eff result: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)
        }
    }

    @Test
    fun testBuildEffStdValidator() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        effService!!.buildMeterEff(BwHolder(TestHelper.buildLoginRequest(login),
                EffParam().apply {
//                    meterId = "125460"
                    meterCode = "111303001101"
                    taskStart = LocalDate(2020, 10, 17).toDate()
                    taskEnd = LocalDate(2020, 10, 19).toDate()
                })).also { r1 ->
            lgr.info("build eff result {} : {}", EffFailureType.EXCEED_2AVG_3STD, JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)
        }
    }

    @Test
    fun testBuildEffManualList() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        effService!!.buildMeterEff(BwHolder(TestHelper.buildLoginRequest(login),
                EffParam().apply {
                    meterIdList = listOf("212590")
                })).also { r1 ->
            lgr.info("build eff result: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)
        }
    }

    @Test
    fun testBuildEffFailure() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        try {
            kotlin.run {
                val r2 = effService!!.buildMeterEff(BwHolder(TestHelper.buildLoginRequest(login),
                        EffParam().apply {
                            meterId = "fz-JTIJ1900015"
                            taskStart = LocalDate(2020, 10, 1).toDate()
                            taskEnd = LocalDate(2020, 10, 2).toDate()
                        }))
                lgr.info("build eff failure result: {}", JSON.toJSONString(r2, true))
                assertEquals(0, r2.code)
            }
        } finally {
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
    fun testLearnModel() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        val r1 = effService!!.learnMeterModel(BwHolder(TestHelper.buildLoginRequest(login),
                EffParam().apply {
                    meterId = "399961"
                    taskStart = LocalDate(2020, 9, 1).toDate()
                    taskEnd = LocalDate(2020, 10, 1).toDate()
                })).also {
            lgr.info("decay list: {}", JSON.toJSONString(it, true))
            assertEquals(0, it.code)
        }
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
            effService!!.insertEffDecay(BwHolder(TestHelper.buildLoginRequest(login),
                    EffParam().also {
                        it.decayList = dlist
                    })).also {
                lgr.info("decay list: {}", JSON.toJSONString(dlist, true))
                assertEquals(0, it.code)
            }
        } finally {
            var cnt = effMapper!!.deleteEffDecay(EffParam().also {
                it.decayList = dlist
            })
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
            var cnt = effMapper!!.deleteEffDecay(EffParam().also {
                it.decayList = dlist
            })
            lgr.info("delete test decay: $cnt")
            assertEquals(0, cnt)
        }
    }

    @Test
    fun testDeleteDecayById() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")

        effService!!.deleteEffDecay(BwHolder(TestHelper.buildLoginRequest(login),
                EffParam().also {
                    it.decayList = listOf(VcEffDecay().apply {
                        decayId = -1
                    }, VcEffDecay().apply {
                        decayId = -2
                    })
                })).also {
            lgr.info("delete decay list: {}", JSON.toJSONString(it, true))
            assertEquals(0, it.code)
        }
    }

    @Test
    fun testConfigMeterDecayFail() {
        val login = TestHelper.login(loginService, "fuzhou", "test") ?: fail("fail to login")

        effService!!.configMeterDecay(BwHolder(TestHelper.buildLoginRequest(login.single!!), EffParam().also {
            it.decayId = 0
        })).also {
            lgr.info("none decay: {}", JSON.toJSONString(it, true))
            assertNotEquals(0, it.code)
        }
    }

    @Test
    @Throws
    fun testConfigMeterDecayMeter() {
        val login = TestHelper.login(loginService, "fuzhou", "test") ?: fail("fail to login")

        val decay = VcEffDecay().apply {
            meterBrandId = "SC"
            sizeId = 100
            sizeName = "DN100"
            modelSize = "MS"
        }

        try {
            effMapper!!.insertEffDecaySingle(decay)

            effService!!.configMeterDecay(BwHolder(TestHelper.buildLoginRequest(login.single!!), EffParam().also {
                it.decayId = decay.decayId
            })).also {
                lgr.info("config meter decay: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }
        } finally {
            assertEquals(1, effMapper!!.deleteEffDecaySingle(EffParam().apply {
                decayId = decay.decayId
                sizeName = null
            }))
        }
    }

    @Test
    @Throws
    fun testConfigMeterDecayClear() {
        val login = TestHelper.login(loginService, "fuzhou", "test") ?: fail("fail to login")

        val p = EffParam().apply {
            meterBrandId = "SC"
            sizeId = 100
            sizeName = "DN100"
            modelSize = "MS"
        }

        effService!!.configMeterDecay(BwHolder(TestHelper.buildLoginRequest(login.single!!), p)).also {
            lgr.info("clear meter decay: {}", JSON.toJSONString(it, true))
            assertEquals(0, it.code)
        }
    }

    @Test
    @Throws
    fun testConfigMeterDecayMeterId() {
        val login = TestHelper.login(loginService, "fuzhou", "test") ?: fail("fail to login")

        val decay = VcEffDecay().apply {
            meterBrandId = "SC"
            sizeId = 100
            sizeName = "DN100"
            modelSize = "MS"
        }

        try {
            effMapper!!.insertEffDecaySingle(decay)

            effService!!.configMeterDecay(BwHolder(TestHelper.buildLoginRequest(login.single!!), EffParam().also {
                it.decayId = decay.decayId
                it.meterId = "test-meter"
                it.meterIdList = listOf("hello", "world")
            })).also {
                lgr.info("config meterId decay: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }
        } finally {
            assertEquals(1, effMapper!!.deleteEffDecaySingle(EffParam().apply {
                decayId = decay.decayId
                sizeName = null
            }))
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

                        it.meterBrandId = "ZNSW"
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

                        it.meterBrandId = "ZNSW"
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

                        it.meterBrandId = "ZNSW"
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

                        it.meterBrandId = "ZNSW"
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

                        it.meterBrandId = "ZNSW"
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