package com.abel.bigwater.impl

import com.abel.bigwater.TestHelper
import com.abel.bigwater.api.BwHolder
import com.abel.bigwater.api.MeterParam
import com.abel.bigwater.api.MeterService
import com.abel.bigwater.api.UserService
import com.abel.bigwater.mapper.MeterMapper
import com.abel.bigwater.model.BwMeterType
import com.abel.bigwater.model.zone.MeterMonthFlow
import com.abel.bigwater.model.zone.ZoneMeter
import com.alibaba.fastjson.JSON
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import kotlin.test.assertEquals
import kotlin.test.fail

@ContextConfiguration(locations = ["classpath:/spring/rest-provider.xml", "classpath:/spring-mybatis.xml"])
@RunWith(SpringJUnit4ClassRunner::class)
class MeterServiceImplTest {
    @Autowired
    var meterService: MeterService? = null

    @Autowired
    var loginService: UserService? = null

    @Autowired
    var meterMapper: MeterMapper? = null

    @Test
    fun testMeterBrand() {
        val r1 = meterService!!.selectMeterBrand()
        lgr.info("meter brand: {}", JSON.toJSONString(r1, true))
    }

    @Test
    fun testRemoteBrand() {
        val r1 = meterService!!.selectRemoteBrand()
        lgr.info("meter brand: {}", JSON.toJSONString(r1, true))
    }

    @Test
    fun testInsertMeter() {
        val meter = ZoneMeter().apply {
            meterId = "test-meterId"
            meterName = "测试水表01"
            sizeId = "100"
            sizeName = "DN100"
        }
        try {
            val login = TestHelper.login(loginService).single ?: fail("fail to login")
            val holder = BwHolder(TestHelper.buildLoginRequest(login), meter)

            val r1 = meterService!!.insertMeter(holder)
            lgr.info("insert result: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)
        } finally {
            val cnt = meterMapper!!.deleteMeter(MeterParam(meterId = meter.meterId))
            lgr.info("cleared meter: {}, {}", cnt, meter.meterId)
        }
    }

    @Test
    fun testDeleteMeter() {
        val meter = ZoneMeter().apply {
            meterId = "test-meterId"
            meterName = "测试水表01"
            sizeId = "100"
            sizeName = "DN100"
        }
        try {
            val login = TestHelper.login(loginService).single ?: fail("fail to login")
            val holder = BwHolder(TestHelper.buildLoginRequest(login), meter)

            val r1 = meterService!!.deleteMeter(holder)
            lgr.info("delete result: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)

            meterService!!.insertMeter(holder).also {
                assertEquals(0, it.code)
            }

            meterService!!.deleteMeter(holder).also {
                assertEquals(0, it.code)
                lgr.info("delete again result: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }
        } finally {
            val cnt = meterMapper!!.deleteMeter(MeterParam(meterId = meter.meterId))
            lgr.info("cleared meter: {}, {}", cnt, meter.meterId)
        }
    }

    @Test
    fun testUpdateMeter() {
        val meter = ZoneMeter().apply {
            meterId = "test-meterId"
            meterName = "测试水表01"
            sizeId = "100"
            sizeName = "DN100"
        }
        try {
            val login = TestHelper.login(loginService).single ?: fail("fail to login")
            val holder = BwHolder(TestHelper.buildLoginRequest(login), meter)

            val r1 = meterService!!.updateMeter(holder)
            lgr.info("delete result: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)

            meterService!!.insertMeter(holder).also {
                assertEquals(0, it.code)
            }

            meter.apply {
                sizeId = "150"
                userCode = "123456"
                meterCode = "12345678"
            }
            meterService!!.updateMeter(holder).also {
                assertEquals(0, it.code)
                lgr.info("delete again result: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }

            meterService!!.listMeter(BwHolder(TestHelper.buildLoginRequest(login), MeterParam(
                    meterId = meter.meterId,
//                    typeId = BwMeterType.ALL.name
            ))).also {
                val m1 = it.list?.firstOrNull()
                assertEquals(meter.userCode, m1?.userCode)
                assertEquals(meter.meterCode, m1?.meterCode)
                assertEquals(meter.sizeId, m1?.sizeId)
            }
        } finally {
            val cnt = meterMapper!!.deleteMeter(MeterParam(meterId = meter.meterId))
            lgr.info("cleared meter: {}, {}", cnt, meter.meterId)
        }
    }

    @Test
    fun testUpdateMeterLoc() {
        val meter = ZoneMeter().apply {
            meterId = "test-meterId"
            meterName = "测试水表01"
            sizeId = "100"
            sizeName = "DN100"
            meterLoc = GeometryFactory().createPoint(Coordinate(22.1, 123.67)).toText()
        }
        try {
            val login = TestHelper.login(loginService).single ?: fail("fail to login")
            val holder = BwHolder(TestHelper.buildLoginRequest(login), meter)

            val r1 = meterService!!.updateMeter(holder)
            lgr.info("update result: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)

            meterService!!.insertMeter(holder).also {
                assertEquals(0, it.code)
            }

            meter.apply {
                sizeId = "150"
                userCode = "123456"
                meterCode = "12345678"
            }
            meterService!!.updateMeter(holder).also {
                assertEquals(0, it.code)
                lgr.info("delete again result: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }

            meterService!!.listMeter(BwHolder(TestHelper.buildLoginRequest(login), MeterParam(
                    meterId = meter.meterId,
//                    typeId = BwMeterType.ALL.name
            ))).also {
                val m1 = it.list?.firstOrNull()
                assertEquals(meter.userCode, m1?.userCode)
                assertEquals(meter.meterCode, m1?.meterCode)
                assertEquals(meter.sizeId, m1?.sizeId)
            }
        } finally {
            val cnt = meterMapper!!.deleteMeter(MeterParam(meterId = meter.meterId))
            lgr.info("cleared meter: {}, {}", cnt, meter.meterId)
        }
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(MeterServiceImplTest::class.java)

        @BeforeClass
        @JvmStatic
        fun preload() {
            lgr.info("pre load bootstrap...")
        }
    }
}