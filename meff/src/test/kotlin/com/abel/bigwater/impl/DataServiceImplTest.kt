package com.abel.bigwater.impl

import com.abel.bigwater.TestHelper
import com.abel.bigwater.api.BwHolder
import com.abel.bigwater.api.DataParam
import com.abel.bigwater.api.DataService
import com.abel.bigwater.api.UserService
import com.abel.bigwater.mapper.DataMapper
import com.abel.bigwater.model.BwData
import com.alibaba.fastjson.JSON
import org.joda.time.LocalDateTime
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import kotlin.test.assertTrue

@ContextConfiguration(locations = ["classpath:/spring/rest-provider.xml", "classpath:/spring-mybatis.xml"])
@RunWith(SpringJUnit4ClassRunner::class)
class DataServiceImplTest {
    @Autowired
    var dataService: DataService? = null

    @Autowired
    var dataMapper: DataMapper? = null

    @Autowired
    var loginService: UserService? = null

    @Before
    fun loginFirst() {
/*
        try {
            val ul = TestHelper.login(loginService)
        } catch (t: Throwable) {
            lgr.error("error: {}", t.message)
        }
*/
    }

    @Test
    fun listRealtime() {
        val ul = TestHelper.login(loginService)
        val r1 = dataService!!.listRealtime(BwHolder(TestHelper.buildLoginRequest(ul.single!!), DataParam()))

        lgr.info("realtime list: {}", JSON.toJSONString(r1))
        assertEquals(0, r1.code)
    }

    @Test
    fun listRealtimeReverse() {
    }

    @Test
    fun realtimeDateRange() {
        val ul = TestHelper.login(loginService)

        val list = listOf(BwData().apply {
            extId = "test-1"
            sampleTime = LocalDateTime(2020, 8, 1, 10, 0).toDate()
            forwardDigits = 123.0
            literPulse = 100
        }, BwData().apply {
            extId = "test-1"
            sampleTime = LocalDateTime(2020, 8, 1, 10, 30).toDate()
            forwardDigits = 123.0
            literPulse = 100
        })

        try {
            val r1 = dataService!!.addRealtimeUser(BwHolder(TestHelper.buildLoginRequest(ul.single!!), list))

            lgr.info("add realtime result: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)

            val r2 = dataService!!.realtimeDateRange(BwHolder(TestHelper.buildLoginRequest(ul.single!!),
                    DataParam(extId = list.first().extId)))
            lgr.info("range list: {}", JSON.toJSONString(r2, true))
            assertEquals(0, r2.code)
            assertEquals(1, r2.list?.size)
        } finally {
            list.forEach {
                dataMapper!!.deleteRealtime(DataParam(extId = it.extId).apply {
                    sampleTime = it.sampleTime
                })
            }
        }
    }

    @Test
    fun realtimeListStat() {
    }

    @Test
    fun addRealtime() {
    }

    @Test
    fun addRealtimeUser() {
        val ul = TestHelper.login(loginService)

        val list = listOf(BwData().apply {
            extId = "test-1"
            sampleTime = LocalDateTime(2020, 8, 1, 10, 0).toDate()
            forwardDigits = 123.0
            literPulse = 100
        }, BwData().apply {
            extId = "test-1"
            sampleTime = LocalDateTime(2020, 8, 1, 10, 30).toDate()
            forwardDigits = 123.0
            literPulse = 100
        })

        try {
            val r1 = dataService!!.addRealtimeUser(BwHolder(TestHelper.buildLoginRequest(ul.single!!), list))

            lgr.info("add realtime result: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)

            val r2 = dataService!!.listRealtime(BwHolder(TestHelper.buildLoginRequest(ul.single!!),
                    DataParam(extId = list.first().extId)))
            lgr.info("data list: {}", JSON.toJSONString(r2, true))
            assertEquals(0, r2.code)
            assertEquals(2, r2.list?.size)
        } finally {
            list.forEach {
                dataMapper!!.deleteRealtime(DataParam(extId = it.extId).apply {
                    sampleTime = it.sampleTime
                })
            }
        }
    }

    @Test
    fun deleteRealtime() {
        val ul = TestHelper.login(loginService)

        val list = listOf(BwData().apply {
            extId = "test-1"
            sampleTime = LocalDateTime(2020, 8, 1, 10, 0).toDate()
            forwardDigits = 123.0
            literPulse = 100
        }, BwData().apply {
            extId = "test-1"
            sampleTime = LocalDateTime(2020, 8, 1, 10, 30).toDate()
            forwardDigits = 123.0
            literPulse = 100
        })

        try {
            val r1 = dataService!!.addRealtimeUser(BwHolder(TestHelper.buildLoginRequest(ul.single!!), list))

            lgr.info("add realtime result: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)

            kotlin.run {
                val r2 = dataService!!.deleteRealtime(BwHolder(TestHelper.buildLoginRequest(ul.single!!),
                        DataParam(extId = list.first().extId).apply {
                            sampleTime = list.first().sampleTime
                        }))
                lgr.info("data deleted: {}", JSON.toJSONString(r2, true))
                assertEquals(0, r2.code)
            }

            kotlin.run {
                val r2 = dataService!!.listRealtime(BwHolder(TestHelper.buildLoginRequest(ul.single!!),
                        DataParam(extId = list.first().extId)))
                lgr.info("data list: {}", JSON.toJSONString(r2, true))
                assertEquals(0, r2.code)
                assertEquals(1, r2.list?.size)
            }
        } finally {
            list.forEach {
                dataMapper!!.deleteRealtime(DataParam(extId = it.extId).apply {
                    sampleTime = it.sampleTime
                })
            }
        }
    }

    @Test
    fun scadaMeterList() {
    }

    @Test
    fun scadaMeterListZone() {
    }

    @Test
    fun listRtuLog() {
    }

    @Test
    fun addRtuLog() {
    }

    @Test
    fun addRtuLogUser() {
    }

    @Test
    fun deleteRtuLog() {
    }

    @Test
    fun listRtu() {
    }

    @Test
    fun deleteRtu() {
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(DataServiceImplTest::class.java)
    }
}