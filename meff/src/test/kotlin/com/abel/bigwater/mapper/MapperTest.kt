package com.abel.bigwater.mapper

import com.abel.bigwater.api.EffParam
import com.abel.bigwater.api.MeterParam
import com.abel.bigwater.model.BwUser
import com.abel.bigwater.model.eff.EffPeriodType
import com.abel.bigwater.model.zone.Zone
import com.abel.bigwater.model.zone.ZoneType
import com.alibaba.fastjson.JSON
import org.apache.dubbo.remoting.http.servlet.ServletManager
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockServletContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.util.*

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(locations = ["classpath:/spring/rest-provider.xml", "classpath:/spring-mybatis.xml"])
open class MapperTest {

    @Autowired
    var userMapper: UserMapper? = null

    @Autowired
    var configMapper: ConfigMapper? = null

    @Autowired
    var meterMapper: MeterMapper? = null

    @Autowired
    var effMapper: EffMapper? = null

    @Autowired
    var zoneMapper: ZoneMapper? = null

    @Test
    fun testConfig() {
        val cfgList = configMapper!!.configList("1", null, null)
        lgr.info("config list: ${JSON.toJSONString(cfgList, true)}")
    }

    @Test
    fun testFirm() {
        val lst = configMapper!!.selectFirm(null, null)
        lgr.info("firm list: ${JSON.toJSONString(lst, true)}")
    }

    @Test
    fun testUser() {
        val lst = userMapper!!.listUser("1", null, null)
        lgr.info("user list: ${JSON.toJSONString(lst, true)}")
    }

    @Test
    fun testUpdateUser() {
        val u = BwUser().apply {
            userId = "abel"
            emailToken = UUID.randomUUID().toString().take(32)
        }

        val cnt = userMapper!!.updateUser(u)
        lgr.info("update user: $cnt")
    }

    @Test
    fun testListLogin() {
        val lst = userMapper!!.listUserLogin("11")
        lgr.info("login list: ${JSON.toJSONString(lst)}")

        val cnt = userMapper!!.kickUserLogin("11", "123456")
        lgr.info("kick login: $cnt")
    }

    @Test
    fun testMeterText() {
        val mp = MeterParam().apply {
            keywords = "福州|中国|花园"
            firmId = "76%"
        }
        val lst = meterMapper!!.selectMeter(mp)
        lgr.info("${mp.keywords} matched list (${lst.size}): {}", JSON.toJSONString(lst, true))
    }

    @Test
    fun buildMonthlyPoint() {
        effMapper!!.deleteEffPoint(EffParam().also {
            it.periodTypeObj = EffPeriodType.Month
        })
        effMapper!!.deleteEffMeter(EffParam().also {
            it.periodTypeObj = EffPeriodType.Month
        })

        effMapper!!.buildEffMeterMonth(EffParam())
        effMapper!!.buildEffPointMonth(EffParam())
    }

    @Test
    fun listZone() {
        val z1 = Zone().apply {
            firmId = "1"
            zoneId = "test"
            zoneName = "测试分区"
            zoneType = ZoneType.FAMILY.name
        }

        zoneMapper!!.listZone(MeterParam().apply {
            firmId = "%"
        }).also {
            lgr.info("list zone: {}", JSON.toJSONString(it, true))
        }

        try {
            zoneMapper!!.insertZone(z1)
            zoneMapper!!.listZone(MeterParam().apply {
                firmId = z1.firmId
            }).also {
                lgr.info("list zone: {}", JSON.toJSONString(it, true))
            }
        } finally {
            lgr.info("delete test zone: {}",
                    zoneMapper!!.deleteZone(MeterParam().apply {
                        zoneId = z1.zoneId
                        firmId = z1.firmId
                    }))
        }
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(MapperTest::class.java)

        @BeforeClass
        @JvmStatic
        fun addServletContext() {
            lgr.warn("prepare servlet container...")
            ServletManager.getInstance().addServletContext(ServletManager.EXTERNAL_SERVER_PORT, MockServletContext())
        }
    }
}