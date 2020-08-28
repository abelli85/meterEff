package com.abel.bigwater.impl

import com.abel.bigwater.TestHelper
import com.abel.bigwater.api.BwHolder
import com.abel.bigwater.api.MeterParam
import com.abel.bigwater.api.StatService
import com.abel.bigwater.api.UserService
import com.abel.bigwater.mapper.MeterMapper
import com.abel.bigwater.model.zone.ZoneMeter
import com.alibaba.fastjson.JSON
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import kotlin.test.fail

@ContextConfiguration(locations = ["classpath:/spring/rest-provider.xml", "classpath:/spring-mybatis.xml"])
@RunWith(SpringJUnit4ClassRunner::class)
class StatServiceImplTest {
    @Autowired
    var service: StatService? = null

    @Autowired
    var loginService: UserService? = null

    @Autowired
    var meterMapper: MeterMapper? = null

    @Test
    fun testStatMeter() {
        val login = TestHelper.login(loginService).single ?: fail("fail to login")
        val holder = BwHolder(TestHelper.buildLoginRequest(login), MeterParam())

        service!!.statMeter(holder).also { r1 ->
            lgr.info("stat-meter list: {}", JSON.toJSONString(r1, true))
        }
    }

    companion object {
        open class HolderMeter : BwHolder<ZoneMeter>()

        private val lgr = LoggerFactory.getLogger(StatServiceImplTest::class.java)

        @BeforeClass
        @JvmStatic
        fun preload() {
            lgr.info("pre load bootstrap...")
        }
    }
}