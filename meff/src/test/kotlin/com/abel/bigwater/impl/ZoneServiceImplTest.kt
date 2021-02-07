package com.abel.bigwater.impl

import com.abel.bigwater.TestHelper
import com.abel.bigwater.api.BwHolder
import com.abel.bigwater.api.MeterParam
import com.abel.bigwater.api.UserService
import com.abel.bigwater.api.ZoneService
import com.abel.bigwater.mapper.MapperTest
import com.abel.bigwater.mapper.ZoneMapper
import com.abel.bigwater.model.zone.Zone
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
            firmId = "1"
            zoneId = "test"
            zoneName = "测试分区"
            zoneType = ZoneType.FAMILY.name
        }

        val login = TestHelper.login(us)
        zs!!.listZone(BwHolder(TestHelper.buildLoginRequest(login.single!!),
                MeterParam().apply {
                })).also {
            lgr.info("list zone : {}", JSON.toJSONString(it, true))
        }

        try {
            zoneMapper!!.insertZone(z1)
        } finally {
            zs!!.listZone(BwHolder(TestHelper.buildLoginRequest(login.single!!),
                    MeterParam().apply {
                    })).also {
                lgr.info("list zone (1) : {}", JSON.toJSONString(it, true))
                assertTrue(it.list!!.isNotEmpty())
            }

            lgr.info("delete test zone: {}",
                    zoneMapper!!.deleteZone(MeterParam().apply {
                        zoneId = z1.zoneId
                        firmId = z1.firmId
                    }))
        }
    }

    @Test
    fun fetchZone() {
    }

    @Test
    fun updateZone() {
    }

    @Test
    fun deleteZone() {
    }

    @Test
    fun attachZoneMeter() {
    }

    @Test
    fun detachZoneMeter() {
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