package com.abel.bigwater.impl

import com.abel.bigwater.TestHelper
import com.abel.bigwater.api.BwHolder
import com.abel.bigwater.api.MeterParam
import com.abel.bigwater.api.MeterService
import com.abel.bigwater.api.UserService
import com.abel.bigwater.mapper.MeterMapper
import com.abel.bigwater.model.BwDma
import com.abel.bigwater.model.eff.VcMeterVerify
import com.abel.bigwater.model.eff.VcMeterVerifyPoint
import com.abel.bigwater.model.zone.MeterChildType
import com.abel.bigwater.model.zone.ZoneMeter
import com.alibaba.fastjson.JSON
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.dubbo.remoting.http.servlet.ServletManager
import org.joda.time.LocalDate
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.WKTWriter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockServletContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import kotlin.test.*

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
        try {
            val login = TestHelper.login(loginService).single ?: fail("fail to login")
            val holder = BwHolder(TestHelper.buildLoginRequest(login), meter)

            val r1 = meterService!!.insertMeter(holder)
            lgr.info("insert result: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)
        } finally {
            meterMapper!!.deleteVerifyPoint(MeterParam(meterId = meter.meterId))
            meterMapper!!.deleteMeterVerify(MeterParam(meterId = meter.meterId))
            val cnt = meterMapper!!.deleteMeter(MeterParam(meterId = meter.meterId))
            lgr.info("cleared meter: {}, {}", cnt, meter.meterId)
        }
    }

    @Test
    fun testInsertMeterList() {
        try {
            val login = TestHelper.login(loginService).single ?: fail("fail to login")
            val holder = BwHolder(TestHelper.buildLoginRequest(login), listOf(meter))

            val r1 = meterService!!.insertMeter(holder)
            lgr.info("insert result: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)
        } finally {
            meterMapper!!.deleteVerifyPoint(MeterParam(meterId = meter.meterId))
            meterMapper!!.deleteMeterVerify(MeterParam(meterId = meter.meterId))
            val cnt = meterMapper!!.deleteMeter(MeterParam(meterId = meter.meterId))
            lgr.info("cleared meter: {}, {}", cnt, meter.meterId)
        }
    }

    @Test
    fun testInsertMeterListDuplicate() {
        try {
            val login = TestHelper.login(loginService).single ?: fail("fail to login")
            val holder = BwHolder(TestHelper.buildLoginRequest(login), listOf(meter))

            val r1 = meterService!!.insertMeter(holder)
            lgr.info("insert result: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)

            meterService!!.insertMeter(holder).also {
                lgr.info("duplicate meter: {}", JSON.toJSONString(it, true))
                assertNotEquals(0, it.code)
            }
        } finally {
            meterMapper!!.deleteVerifyPoint(MeterParam(meterId = meter.meterId))
            meterMapper!!.deleteMeterVerify(MeterParam(meterId = meter.meterId))
            val cnt = meterMapper!!.deleteMeter(MeterParam(meterId = meter.meterId))
            lgr.info("cleared meter: {}, {}", cnt, meter.meterId)
        }
    }

    @Test
    fun testDeleteMeter() {
        val meter = ZoneMeter().apply {
            meterId = "test-meterId"
            meterName = "测试水表01"
            steelNo = "23456789"
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
            steelNo = "4567890123"
            sizeId = 100
            sizeName = "DN100"
        }
        try {
            val login = TestHelper.login(loginService).single ?: fail("fail to login")
            val holder = BwHolder(TestHelper.buildLoginRequest(login), meter)

            val r1 = meterService!!.updateMeter(holder)
            lgr.info("update firstly result: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)

            meterService!!.insertMeter(holder).also {
                assertEquals(0, it.code)
            }

            meter.apply {
                sizeId = 150
                userCode = "123456"
                meterCode = "12345678"
                firmId = "27123456"
            }
            meterService!!.updateMeter(holder).also {
                assertEquals(0, it.code)
                lgr.info("update meter again result: {}", JSON.toJSONString(it, true))
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
            steelNo = "5678901234"
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
        val holder = BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
            rows = 10
            meterIdList = listOf("02290200306822")
        })

        meterService!!.listMeter(holder).also { r1 ->
            lgr.info("zone-meter list: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)
        }
    }

    @Test
    fun testListMeterDma() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")
        val holder = BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
            dmaId = "%"
            meterCode = "02290200306822"
            rows = 10
        })

        meterService!!.listMeter(holder).also { r1 ->
            lgr.info("dma-meter list: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)
        }
    }

    @Test
    fun testListMeterZoneText() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")
        val holder = BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
            keywords = "中国 \t花园 街道申舒斯福州\r\n"
            firmId = "76%"
        })

        meterService!!.listMeter(holder).also { r1 ->
            lgr.info("zone-meter list: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)
        }
    }

    @Test
    fun testListMeterDmaText() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")
        val holder = BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
            keywords = "维也纳街道"
            firmId = "76%"
            dmaId = "%"
        })

        meterService!!.listMeter(holder).also { r1 ->
            lgr.info("dma-meter list: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)
        }
    }

    @Test
    fun testListMeterZonePinyin() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")
        val holder = BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
            keywords = "酒店\tWyn\r\n"
            firmId = "27%"
            rows = 10
        })

        meterService!!.listMeter(holder).also { r1 ->
            lgr.info("zone-meter list: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)
        }
    }

    @Test
    fun testListMeterDmaPinyin() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")
        val holder = BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
            keywords = "WeiYeNa"
            firmId = "27%"
            dmaId = "%"
            rows = 10
        })

        meterService!!.listMeter(holder).also { r1 ->
            lgr.info("dma-meter list: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)
        }
    }

    @Test
    fun testFetchMeter() {
        val meter = ZoneMeter().apply {
            meterId = "02290200306822"
        }

        val login = TestHelper.login(loginService).single ?: fail("fail to login")
        val holder = BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
            meterId = meter.meterId
        })

        meterService!!.fetchMeter(holder).also { r1 ->
            lgr.info("zone-meter detail: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)
        }
    }

    @Test
    fun testFetchMeterZone() {
        val meter = ZoneMeter().apply {
            meterId = "test-meterId"
            meterName = "测试水表01"
            steelNo = "6789012345"
            sizeId = 100
            sizeName = "DN100"
            decayId = 44
        }

        try {
            val login = TestHelper.login(loginService).single ?: fail("fail to login")
            meterMapper!!.insertMeter(meter.also { it.firmId = login.firmId })

            val holder = BwHolder(TestHelper.buildLoginRequest(login), MeterParam(meterId = meter.meterId))

            meterService!!.fetchMeter(holder).also { r1 ->
                lgr.info("zone-meter detail: {}", JSON.toJSONString(r1, true))
                assertEquals(0, r1.code)
            }
        } finally {
            meterMapper!!.deleteMeter(MeterParam(meterId = meter.meterId))
        }
    }

    @Test
    fun testFetchMeterDma() {
        val meter = ZoneMeter().apply {
            meterId = "test-meterId"
            meterName = "测试水表01"
            steelNo = "3456789"
            sizeId = 100
            sizeName = "DN100"
        }

        try {
            val login = TestHelper.login(loginService).single ?: fail("fail to login")
            meterMapper!!.insertMeter(meter.also { it.firmId = login.firmId })

            val holder = BwHolder(TestHelper.buildLoginRequest(login), MeterParam().also {
                it.meterId = meter.meterId
                it.dmaId = "%"
            })

            meterService!!.fetchMeter(holder).also { r1 ->
                lgr.info("dma-meter list: {}", JSON.toJSONString(r1, true))
                assertEquals(0, r1.code)
            }
        } finally {
            meterMapper!!.deleteMeter(MeterParam(meterId = meter.meterId))
        }
    }

    @Test
    fun insertMeterJson() {
        val json = """{"lr":{"userId":"abel","timestamp":"13:43:24","clientHash":"d31d71d44bf59f751bed1ffeb19b6c79","devId":"junit","sessionId":"e235c43d-7393-436b-8a58-8be23971a08b"},"param1":null,"param2":null,"single":{"zoneId":"123","flowOut":0,"meterId":"05600","userCode":"123","meterCode":"123123123","meterName":"123","meterOrder":null,"extId":null,"location":"123","installDate":"2020-08-20T00:00:00","meterPulse":null,"q1":null,"q2":null,"q3":null,"q4":null,"q1r":null,"q2r":null,"q3r":null,"q4r":null,"sizeId":"123","sizeName":"123","modelSize":"312","typeId":null,"userType":null,"waterPrice":null,"serviceArea":null,"servicePopulation":null,"contactNumber":null,"chargable":0,"dmaId":null,"firmId":"2705","meterBrandId":"123","steelNo":"123","remoteBrandId":"213","rtuId":"23","rtuCode":null,"rtuAddr":null,"rtuInstallDate":null,"rtuInstallPerson":null,"rtuContact":null,"commCard":null,"remoteModel":null,"remoteMemo":null,"commIsp":null,"pressureRange":null,"pressureMaxLimit":null,"pressureMinLimit":null,"powerType":0,"meterStatus":0,"adminMobile":null,"meterLoc":"213","lastCalib":null,"memo":"123","createBy":null,"createDate":null,"updateBy":null,"updateDate":null},"list":null}"""
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
            lgr.info("cleared dma: {}/{}, {}",
                    meterMapper!!.detachMeterDma(MeterParam().apply {
                        dmaId = d.dmaId
                    }),
                    meterMapper!!.deleteDma(MeterParam().apply {
                        dmaId = d.dmaId
                        firmId = "%"
                    }),
                    d.dmaId)
        }

    }

    @Test
    fun testInsertDma() {
        val d = BwDma().apply {
            dmaId = "test-dma01"
            dmaName = "测试DMA01"
            meterList = listOf(
                    ZoneMeter().also {
                        it.meterId = "test1"
                        it.flowOut = 0
                        it.childTypeObj = MeterChildType.PARENT
                    },
                    ZoneMeter().also {
                        it.meterId = "test2"
                        it.flowOut = 0
                        it.childTypeObj = MeterChildType.PARENT
                    },
                    ZoneMeter().apply {
                        meterId = "223290"
                        meterName = "下沙六坊4号"
                        meterCode = "113540354301"
                    },
                    ZoneMeter().apply {
                        meterId = "60323"
                        meterName = "深圳市鲲田物业管理有限公司"
                        meterCode = "111106000601"
                    })
        }

        try {
            val login = TestHelper.login(loginService).single ?: fail("fail to login")
            val holder = BwHolder(TestHelper.buildLoginRequest(login), d)

            meterService!!.insertDma(holder).also {
                lgr.info("insert dma: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }

            meterService!!.fetchDma(BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
                dmaId = d.dmaId
            })).also {
                lgr.info("fetch dma: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
                assertEquals(2, it.single!!.meterList?.size)
            }
        } finally {
            lgr.info("cleared dma: {}/{}, {}",
                    meterMapper!!.detachMeterDma(MeterParam().apply {
                        dmaId = d.dmaId
                    }),
                    meterMapper!!.deleteDma(MeterParam().apply {
                        dmaId = d.dmaId
                        firmId = "%"
                    }),
                    d.dmaId)
        }
    }

    @Test
    fun testFetchDma() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")
        meterService!!.fetchDma(BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
            dmaId = "test-dma-01"
        })).also {
            lgr.info("fetch dma: {}", JSON.toJSONString(it, true))
            assertEquals(0, it.code)
            assertNull(it.single, "should dma not exist")
        }
    }

    @Test
    fun testUpdateDma() {
        val d = BwDma().apply {
            dmaId = "test-dma01"
            dmaName = "测试DMA01"
            meterList = listOf(
                    ZoneMeter().also {
                        it.meterId = "test1"
                        it.flowOut = 0
                        it.childTypeObj = MeterChildType.PARENT
                    },
                    ZoneMeter().also {
                        it.meterId = "test2"
                        it.flowOut = 0
                        it.childTypeObj = MeterChildType.PARENT
                    },
                    ZoneMeter().apply {
                        meterId = "223290"
                        meterName = "下沙六坊4号"
                        meterCode = "113540354301"
                    },
                    ZoneMeter().apply {
                        meterId = "60323"
                        meterName = "深圳市鲲田物业管理有限公司"
                        meterCode = "111106000601"
                    })
        }

        try {
            val login = TestHelper.login(loginService).single ?: fail("fail to login")
            val holder = BwHolder(TestHelper.buildLoginRequest(login), d)

            meterService!!.insertDma(holder).also {
                lgr.info("insert dma: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }

            meterService!!.fetchDma(BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
                dmaId = d.dmaId
            })).also {
                lgr.info("fetch dma: {}", JSON.toJSONString(it, true))
                assertEquals(d.dmaName, it.single?.dmaName)
                assertEquals(0, it.code)
                assertEquals(2, it.single!!.meterList?.size)
            }

            meterService!!.updateDma(BwHolder(TestHelper.buildLoginRequest(login), d.apply {
                dmaName = "~!@#$%^&*"
            })).also {
                lgr.info("update dma: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }

            meterService!!.fetchDma(BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
                dmaId = d.dmaId
            })).also {
                lgr.info("fetch dma: {}", JSON.toJSONString(it, true))
                assertEquals(d.dmaName, it.single?.dmaName)
                assertEquals(0, it.code)
                assertEquals(2, it.single!!.meterList?.size)
            }
        } finally {
            lgr.info("cleared dma: {}/{}, {}",
                    meterMapper!!.detachMeterDma(MeterParam().apply {
                        dmaId = d.dmaId
                    }),
                    meterMapper!!.deleteDma(MeterParam().apply {
                        dmaId = d.dmaId
                        firmId = "%"
                    }),
                    d.dmaId)
        }
    }

    @Test
    fun testLinkDmaMeter() {
        val m1 = ZoneMeter().apply {
            meterId = "223290"
            meterName = "下沙六坊4号"
            meterCode = "113540354301"
        }
        val m2 = ZoneMeter().apply {
            meterId = "329008"
            meterName = "史亚振"
            meterCode = "168340647301"
            childTypeObj = MeterChildType.CHILD
        }
        val d = BwDma().apply {
            dmaId = "test-dma01"
            dmaName = "测试DMA01"
            meterList = listOf(
                    ZoneMeter().also {
                        it.meterId = "test1"
                        it.flowOut = 0
                        it.childTypeObj = MeterChildType.PARENT
                    },
                    ZoneMeter().also {
                        it.meterId = "test2"
                        it.flowOut = 0
                        it.childTypeObj = MeterChildType.PARENT
                    },
                    m1,
                    ZoneMeter().apply {
                        meterId = "60323"
                        meterName = "深圳市鲲田物业管理有限公司"
                        meterCode = "111106000601"
                    })
        }

        try {
            val login = TestHelper.login(loginService).single ?: fail("fail to login")
            val holder = BwHolder(TestHelper.buildLoginRequest(login), d)

            meterService!!.insertDma(holder).also {
                lgr.info("insert dma: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }

            meterService!!.fetchDma(BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
                dmaId = d.dmaId
            })).also {
                lgr.info("fetch dma: {}", JSON.toJSONString(it, true))
                assertEquals(d.dmaName, it.single?.dmaName)
                assertEquals(0, it.code)
                assertEquals(2, it.single!!.meterList?.size)
            }

            m1.apply {
                flowOut = 1
            }
            meterService!!.linkMeterDma(BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
                dmaId = d.dmaId
                meterList = d.meterList?.plus(m2)
            })).also {
                lgr.info("link dma: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }

            meterService!!.fetchDma(BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
                dmaId = d.dmaId
            })).also {
                lgr.info("fetch dma after link: {}", JSON.toJSONString(it, true))
                assertEquals(d.dmaName, it.single?.dmaName)
                assertEquals(0, it.code)
                assertEquals(3, it.single!!.meterList?.size)
                assertEquals(1, it.single!!.meterList?.first { m -> m.meterId == m1.meterId }?.flowOut)
                assertEquals(MeterChildType.CHILD, it.single!!.meterList?.first { m -> m.meterId == m2.meterId }?.childTypeObj)
            }
        } finally {
            lgr.info("cleared dma: {}/{}, {}",
                    meterMapper!!.detachMeterDma(MeterParam().apply {
                        dmaId = d.dmaId
                    }),
                    meterMapper!!.deleteDma(MeterParam().apply {
                        dmaId = d.dmaId
                        firmId = "%"
                    }),
                    d.dmaId)
        }
    }

    @Test
    fun testDetachDmaMeter() {
        val m1 = ZoneMeter().apply {
            meterId = "223290"
            meterName = "下沙六坊4号"
            meterCode = "113540354301"
        }
        val m2 = ZoneMeter().apply {
            meterId = "329008"
            meterName = "史亚振"
            meterCode = "168340647301"
            childTypeObj = MeterChildType.CHILD
        }
        val d = BwDma().apply {
            dmaId = "test-dma01"
            dmaName = "测试DMA01"
            meterList = listOf(
                    ZoneMeter().also {
                        it.meterId = "test1"
                        it.flowOut = 0
                        it.childTypeObj = MeterChildType.PARENT
                    },
                    ZoneMeter().also {
                        it.meterId = "test2"
                        it.flowOut = 0
                        it.childTypeObj = MeterChildType.PARENT
                    },
                    m1,
                    ZoneMeter().apply {
                        meterId = "60323"
                        meterName = "深圳市鲲田物业管理有限公司"
                        meterCode = "111106000601"
                    })
        }

        try {
            val login = TestHelper.login(loginService).single ?: fail("fail to login")
            val holder = BwHolder(TestHelper.buildLoginRequest(login), d)

            meterService!!.insertDma(holder).also {
                lgr.info("insert dma: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }

            meterService!!.fetchDma(BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
                dmaId = d.dmaId
            })).also {
                lgr.info("fetch dma: {}", JSON.toJSONString(it, true))
                assertEquals(d.dmaName, it.single?.dmaName)
                assertEquals(0, it.code)
                assertEquals(2, it.single!!.meterList?.size)
            }

            meterService!!.detachMeterDma(BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
                dmaId = d.dmaId
            })).also {
                lgr.info("link dma: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }

            meterService!!.fetchDma(BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
                dmaId = d.dmaId
            })).also {
                lgr.info("fetch dma after link: {}", JSON.toJSONString(it, true))
                assertEquals(d.dmaName, it.single?.dmaName)
                assertEquals(0, it.code)
                assertEquals(0, it.single!!.meterList?.size)
            }
        } finally {
            lgr.info("cleared dma: {}/{}, {}",
                    meterMapper!!.detachMeterDma(MeterParam().apply {
                        dmaId = d.dmaId
                    }),
                    meterMapper!!.deleteDma(MeterParam().apply {
                        dmaId = d.dmaId
                        firmId = "%"
                    }),
                    d.dmaId)
        }

    }

    @Test
    fun changeMeterPoint() {
        try {
            val login = TestHelper.login(loginService).single ?: fail("fail to login")
            val holder = BwHolder(TestHelper.buildLoginRequest(login), meter)

            meterService!!.insertMeter(holder).also { r1 ->
                lgr.info("insert result: {}", JSON.toJSONString(r1, true))
                assertEquals(0, r1.code)
            }

            meterService!!.addMeterPoint(BwHolder(TestHelper.buildLoginRequest(login), ZoneMeter().also {
                it.meterId = meter.meterId
                it.steelNo = meter.steelNo
                it.verifyList = listOf(VcMeterVerify().apply {
                    meterId = meter.meterId
                    batchId = meter.meterId
                    verifyDate = LocalDate(2020, 8, 1).toDate()
                })

                it.pointList = listOf(
                        VcMeterVerifyPoint().apply {
                            meterId = meter.meterId
                            pointNo = 1
                            pointName = "Q1"
                            pointFlow = 2.0
                            pointDev = 1.2
                            verifyDate = LocalDate(2020, 8, 1).toDate()
                        },
                        VcMeterVerifyPoint().apply {
                            meterId = meter.meterId
                            pointNo = 2
                            pointName = "Q2"
                            pointFlow = 3.2
                            pointDev = -1.7
                            verifyDate = LocalDate(2020, 8, 1).toDate()
                        }
                )
            })).also {
                lgr.info("add meter point: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }

            meterService!!.fetchMeter(BwHolder(TestHelper.buildLoginRequest(login), MeterParam().apply {
                meterId = meter.meterId
            })).also {
                lgr.info("meter detail: {}", JSON.toJSONString(it))
                assertEquals(0, it.code)

                meterService!!.removeMeterPoint(BwHolder(TestHelper.buildLoginRequest(login), ZoneMeter().apply {
                    meterId = meter.meterId
                    verifyList = it.single?.verifyList
                    pointList = it.single?.pointList
                })).also { r2 ->
                    lgr.info("remove point: {}", JSON.toJSONString(r2, true))
                    assertEquals(0, r2.code)
                }
            }
        } finally {
            meterMapper!!.deleteVerifyPoint(MeterParam(meterId = meter.meterId))
            meterMapper!!.deleteMeterVerify(MeterParam(meterId = meter.meterId))
            val cnt = meterMapper!!.deleteMeter(MeterParam(meterId = meter.meterId))
            lgr.info("cleared meter: {}, {}", cnt, meter.meterId)
        }
    }

    companion object {
        open class HolderMeter : BwHolder<ZoneMeter>()

        private val lgr = LoggerFactory.getLogger(MeterServiceImplTest::class.java)

        private val meter = ZoneMeter().apply {
            meterId = "test-meterId"
            meterName = "测试水表01"
            steelNo = "0123456789"
            sizeId = 100
            sizeName = "DN100"
            meterLoc = WKTWriter().write(GeometryFactory().createPoint(Coordinate(122.1, 44.6)))

            verifyList = listOf(
                    VcMeterVerify().also {
                        it.meterId = meterId
                        it.batchId = meterId
                        it.verifyDate = LocalDate(2020, 7, 1).toDate()
                    }
            )

            pointList = listOf(
                    VcMeterVerifyPoint().also {
                        it.meterId = meterId
                        it.pointFlow = 1.25
                        it.verifyDate = LocalDate(2020, 7, 1).toDate()
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