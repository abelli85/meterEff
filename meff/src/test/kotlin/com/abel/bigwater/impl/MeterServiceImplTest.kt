package com.abel.bigwater.impl

import com.abel.bigwater.TestHelper
import com.abel.bigwater.api.BwHolder
import com.abel.bigwater.api.MeterParam
import com.abel.bigwater.api.MeterService
import com.abel.bigwater.api.UserService
import com.abel.bigwater.mapper.MeterMapper
import com.abel.bigwater.model.BwDma
import com.abel.bigwater.model.zone.ZoneMeter
import com.alibaba.fastjson.JSON
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
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
            sizeId = 100
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
            sizeId = 100
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
            sizeId = 100
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
                sizeId = 150
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
            sizeId = 100
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
                sizeId = 150
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
    fun testListMeterZone() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")
        val holder = BwHolder(TestHelper.buildLoginRequest(login), MeterParam())

        meterService!!.listMeter(holder).also { r1 ->
            lgr.info("zone-meter list: {}", JSON.toJSONString(r1, true))
        }
    }

    @Test
    fun testListMeterDma() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")
        val holder = BwHolder(TestHelper.buildLoginRequest(login), MeterParam().also {
            it.dmaId = "%"
        })

        meterService!!.listMeter(holder).also { r1 ->
            lgr.info("dma-meter list: {}", JSON.toJSONString(r1, true))
        }
    }

    @Test
    fun insertMeterJson() {
        val json = """{"lr":{"userId":"abel","timestamp":"13:43:24","clientHash":"d31d71d44bf59f751bed1ffeb19b6c79","devId":"junit","sessionId":"e235c43d-7393-436b-8a58-8be23971a08b"},"param1":null,"param2":null,"single":{"zoneId":"123","flowOut":0,"meterId":"05600","userCode":"123","meterCode":"123123123","meterName":"123","meterOrder":null,"extId":null,"location":"123","installDate":"2020-08-20T00:00:00","meterPulse":null,"q1":null,"q2":null,"q3":null,"q4":null,"q1r":null,"q2r":null,"q3r":null,"q4r":null,"sizeId":"123","sizeName":"123","modelSize":"312","typeId":null,"userType":null,"waterPrice":null,"serviceArea":null,"servicePopulation":null,"contactNumber":null,"chargable":0,"inOutput":null,"dmaId":null,"firmId":"2705","meterBrandId":"123","steelNo":"123","remoteBrandId":"213","rtuId":"23","rtuCode":null,"rtuAddr":null,"rtuInstallDate":null,"rtuInstallPerson":null,"rtuContact":null,"commCard":null,"remoteModel":null,"remoteMemo":null,"commIsp":null,"pressureRange":null,"pressureMaxLimit":null,"pressureMinLimit":null,"powerType":0,"meterStatus":0,"adminMobile":null,"meterLoc":"213","lastCalib":null,"memo":"123","createBy":null,"createDate":null,"updateBy":null,"updateDate":null},"list":null}"""
        val h = ObjectMapper().readValue(json, HolderMeter::class.java)

        meterService!!.insertMeter(h).also { r1 ->
            lgr.info("insert meter result: {}", JSON.toJSONString(r1, true))
        }
    }

    @Test
    fun listDma() {
        val d = BwDma().apply {
            dmaId = "test-dma01"
            dmaName = "测试DMA01"
        }
        try {
            val login = TestHelper.login(loginService).single ?: fail("fail to login")
            val holder = BwHolder(TestHelper.buildLoginRequest(login), d)

            meterService!!.insertDma(holder).also {
                lgr.info("insert dma: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }

            meterService!!.listDma(BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
                dmaId = d.dmaId
            })).also {
                lgr.info("list dma: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }
        } finally {
            val cnt = meterMapper!!.deleteDma(MeterParam(dmaId = d.dmaId))
            lgr.info("cleared dma: {}, {}", cnt, d.dmaId)
        }

    }

    companion object {
        open class HolderMeter : BwHolder<ZoneMeter>()

        private val lgr = LoggerFactory.getLogger(MeterServiceImplTest::class.java)

        @BeforeClass
        @JvmStatic
        fun preload() {
            lgr.info("pre load bootstrap...")
        }
    }
}