package com.abel.bigwater.impl

import com.abel.bigwater.TestHelper
import com.abel.bigwater.api.BwHolder
import com.abel.bigwater.api.MeterParam
import com.abel.bigwater.api.UserService
import com.abel.bigwater.api.ZoneService
import com.abel.bigwater.mapper.MapperTest
import com.abel.bigwater.mapper.ZoneMapper
import com.abel.bigwater.model.zone.MeterChildType
import com.abel.bigwater.model.zone.Zone
import com.abel.bigwater.model.zone.ZoneMeter
import com.abel.bigwater.model.zone.ZoneType
import com.alibaba.fastjson.JSON
import org.apache.dubbo.remoting.http.servlet.ServletManager
import org.junit.Test

import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockServletContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@ContextConfiguration(locations = ["classpath:/spring/rest-provider.xml", "classpath:/spring-mybatis.xml"])
@RunWith(SpringJUnit4ClassRunner::class)
class ZoneServiceImplTest {

    @Autowired
    var us: UserService? = null

    @Autowired
    var zs: ZoneService? = null

    @Autowired
    var zoneMapper: ZoneMapper? = null

    @Test
    fun listZone() {
        val z1 = Zone().apply {
            zoneId = "test"
            zoneName = "测试分区"
            zoneType = ZoneType.FAMILY.name
        }

        val login = TestHelper.login(us)
        z1.firmId = login.single!!.firmId!!

        zs!!.listZone(BwHolder(TestHelper.buildLoginRequest(login.single!!),
                MeterParam().apply {
                })).also {
            lgr.info("list zone : {}", JSON.toJSONString(it, true))
        }

        try {
            zoneMapper!!.insertZone(z1)

            zs!!.listZone(BwHolder(TestHelper.buildLoginRequest(login.single!!),
                    MeterParam().apply {
                    })).also {
                lgr.info("list zone (1) : {}", JSON.toJSONString(it, true))
                assertTrue(it.list!!.isNotEmpty())
            }
        } finally {
            lgr.info("delete test zone: {}",
                    zoneMapper!!.deleteZone(MeterParam().apply {
                        zoneId = z1.zoneId
                        firmId = z1.firmId
                    }))
        }
    }

    @Test
    fun saveZone() {
        val z1 = Zone().apply {
            zoneId = "test"
            zoneName = "测试分区"
            zoneType = ZoneType.FAMILY.name
            meterList = listOf(
                    ZoneMeter().apply {
                        meterId = "meter1"
                        meterName = "水表1"
                        meterCode = "meter-code-1"
                    },
                    ZoneMeter().apply {
                        meterId = "meter2"
                        meterName = "水表2"
                        meterCode = "meter-code-2"
                    }
            )
        }

        val login = TestHelper.login(us)
        z1.firmId = login.single!!.firmId!!

        try {
            zs!!.saveZone(BwHolder(TestHelper.buildLoginRequest(login.single!!), z1)).also {
                lgr.info("save zone (1) : {}", JSON.toJSONString(it, true))
                assertNotNull(it.single)
                assertNull(it.list)
            }
        } finally {
            lgr.info("delete test zone: {}/{}",
                    zoneMapper!!.detachZoneMeter(Zone().apply {
                        zoneId = z1.zoneId
                    }),
                    zoneMapper!!.deleteZone(MeterParam().apply {
                        zoneId = z1.zoneId
                        firmId = z1.firmId
                    }))
        }
    }

    @Test
    fun fetchZone() {
        val z1 = Zone().apply {
            zoneId = "test"
            zoneName = "测试分区"
            zoneType = ZoneType.FAMILY.name
        }

        val login = TestHelper.login(us)
        z1.firmId = login.single!!.firmId!!

        zs!!.fetchZone(BwHolder(TestHelper.buildLoginRequest(login.single!!),
                MeterParam().apply {
                    zoneId = "test"
                })).also {
            lgr.info("fetch zone (1) : {}", JSON.toJSONString(it, true))
            assertNull(it.single)
            assertEquals(0, it.list?.size ?: 0)
        }

        zoneMapper!!.insertZone(z1)
        try {
            zs!!.fetchZone(BwHolder(TestHelper.buildLoginRequest(login.single!!),
                    MeterParam().apply {
                        zoneId = "test"
                    })).also {
                lgr.info("fetch zone (1) : {}", JSON.toJSONString(it, true))
                assertNotNull(it.single)
                assertNull(it.list)
            }
        } finally {
            lgr.info("delete test zone: {}",
                    zoneMapper!!.deleteZone(MeterParam().apply {
                        zoneId = z1.zoneId
                        firmId = z1.firmId
                    }))
        }
    }

    @Test
    fun updateZone() {
        val z1 = Zone().apply {
            zoneId = "test"
            zoneName = "测试分区"
            zoneType = ZoneType.FAMILY.name
        }

        val login = TestHelper.login(us)
        z1.firmId = login.single!!.firmId!!

        zoneMapper!!.insertZone(z1)
        try {
            zs!!.updateZone(BwHolder(TestHelper.buildLoginRequest(login.single!!), z1.apply {
                zoneName = "test zone 2"
            })).also {
                lgr.info("update test zone: {}", JSON.toJSONString(it, true))
            }

            zoneMapper!!.listZone(MeterParam(zoneId = z1.zoneId).apply {
                firmId = login.single!!.firmId
            }).first().also {
                assertEquals(z1.zoneId, it.zoneId)
                assertEquals(z1.zoneName, it.zoneName)
            }
        } finally {
            lgr.info("delete test zone: {}",
                    zoneMapper!!.deleteZone(MeterParam().apply {
                        zoneId = z1.zoneId
                        firmId = z1.firmId
                    }))
        }
    }

    @Test
    fun deleteZone() {
        val z1 = Zone().apply {
            zoneId = "test"
            zoneName = "测试分区"
            zoneType = ZoneType.FAMILY.name
        }

        val login = TestHelper.login(us)
        z1.firmId = login.single!!.firmId!!

        zoneMapper!!.insertZone(z1)
        try {
            zs!!.deleteZone(BwHolder(TestHelper.buildLoginRequest(login.single!!), z1.apply {
            })).also {
                lgr.info("delete test zone: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }
        } finally {
            lgr.info("delete test zone: {}",
                    zoneMapper!!.deleteZone(MeterParam().apply {
                        zoneId = z1.zoneId
                        firmId = z1.firmId
                    }))
        }
    }

    @Test
    fun attachZoneMeter() {
        val z1 = Zone().apply {
            zoneId = "test"
            zoneName = "测试分区"
            zoneType = ZoneType.FAMILY.name
        }

        val login = TestHelper.login(us)
        z1.firmId = login.single!!.firmId!!

        zoneMapper!!.insertZone(z1)
        try {
            zs!!.saveZoneMeter(BwHolder(TestHelper.buildLoginRequest(login.single!!), z1.apply {
                meterList = listOf(ZoneMeter().also {
                    it.zoneId = z1.zoneId
                    it.meterId = "test1"
                    it.flowOut = 0
                    it.childTypeObj = MeterChildType.PARENT
                },
                        ZoneMeter().also {
                            it.zoneId = z1.zoneId
                            it.meterId = "test2"
                            it.flowOut = 0
                            it.childTypeObj = MeterChildType.PARENT
                        })
            })).also {
                lgr.info("save zone-meter: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }
        } finally {
            lgr.info("delete test zone: {}, {}",
                    zoneMapper!!.detachZoneMeter(Zone().apply {
                        zoneId = z1.zoneId
                    }),
                    zoneMapper!!.deleteZone(MeterParam().apply {
                        zoneId = z1.zoneId
                        firmId = z1.firmId
                    }))
        }
    }

    @Test
    fun detachZoneMeter() {
        val z1 = Zone().apply {
            zoneId = "test"
            zoneName = "测试分区"
            zoneType = ZoneType.FAMILY.name
        }

        val login = TestHelper.login(us)
        z1.firmId = login.single!!.firmId!!

        zoneMapper!!.insertZone(z1)
        try {
            zs!!.deleteZoneMeter(BwHolder(TestHelper.buildLoginRequest(login.single!!), z1.apply {
                meterList = listOf(ZoneMeter().also {
                    it.zoneId = z1.zoneId
                    it.meterId = "test1"
                    it.flowOut = 0
                    it.childTypeObj = MeterChildType.PARENT
                },
                        ZoneMeter().also {
                            it.zoneId = z1.zoneId
                            it.meterId = "test2"
                            it.flowOut = 0
                            it.childTypeObj = MeterChildType.PARENT
                        })
            })).also {
                lgr.info("delete zone-meter: {}", JSON.toJSONString(it, true))
                assertEquals(0, it.code)
            }
        } finally {
            lgr.info("delete test zone: {}, {}",
                    zoneMapper!!.detachZoneMeter(Zone().apply {
                        zoneId = z1.zoneId
                    }),
                    zoneMapper!!.deleteZone(MeterParam().apply {
                        zoneId = z1.zoneId
                        firmId = z1.firmId
                    }))
        }
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(ZoneServiceImplTest::class.java)

        @BeforeClass
        @JvmStatic
        fun addServletContext() {
            lgr.warn("prepare servlet container...")
            ServletManager.getInstance().addServletContext(ServletManager.EXTERNAL_SERVER_PORT, MockServletContext())
        }
    }
}