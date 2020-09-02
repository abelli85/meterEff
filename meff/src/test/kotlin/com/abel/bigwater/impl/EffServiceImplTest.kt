package com.abel.bigwater.impl

import com.abel.bigwater.TestHelper
import com.abel.bigwater.api.BwHolder
import com.abel.bigwater.api.EffParam
import com.abel.bigwater.api.EffService
import com.abel.bigwater.api.UserService
import com.abel.bigwater.mapper.EffMapper
import com.abel.bigwater.model.eff.EffMeter
import com.abel.bigwater.model.eff.EffTask
import com.alibaba.fastjson.JSON
import org.joda.time.LocalDate
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import kotlin.test.assertEquals
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

    @Test
    fun createEffTask() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")
        val t = EffTask().apply {
            taskName = "季度分析任务"
            taskStart = LocalDate(2020, 7, 1).toDate()
            taskEnd = LocalDate(2020, 9, 30).toDate()
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
        val t = EffTask().apply {
            taskName = "季度分析任务"
            taskStart = LocalDate(2020, 7, 1).toDate()
            taskEnd = LocalDate(2020, 9, 30).toDate()
            meterList = listOf(
                    EffMeter().also {
                        it.meterId = "0123"
                        it.meterName = "test 3"
                        it.taskName = taskName
                        it.taskStart = taskStart
                        it.taskEnd = taskEnd

                        it.sizeId = "40"
                        it.sizeName = "DN40"
                    },
                    EffMeter().also {
                        it.meterId = "0127"
                        it.meterName = "test 7"
                        it.taskName = taskName
                        it.taskStart = taskStart
                        it.taskEnd = taskEnd

                        it.sizeId = "50"
                        it.sizeName = "DN5"
                    },
                    EffMeter().also {
                        it.meterId = "0129"
                        it.meterName = "test 9"
                        it.taskName = taskName
                        it.taskStart = taskStart
                        it.taskEnd = taskEnd

                        it.sizeId = "50"
                        it.sizeName = "DN5"
                    }
            )
        }

        try {
            effService!!.createEffTask(BwHolder(TestHelper.buildLoginRequest(login), t)).also {
                lgr.info("create task: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }
        } finally {
            if (t.taskId ?: -1 > 0) {
                effMapper!!.deleteEffMeter(EffParam().apply {
                    taskId = t.taskId
                })
                effMapper!!.deleteEffTask(EffParam().apply {
                    taskId = t.taskId
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
    }

    @Test
    fun fetchMeterEff() {
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(EffServiceImplTest::class.java)
    }
}