package com.abel.bigwater.mapper

import com.abel.bigwater.api.MeterParam
import com.abel.bigwater.model.BwUser
import com.alibaba.fastjson.JSON
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.util.*

@RunWith(SpringJUnit4ClassRunner::class)
@ContextConfiguration(locations = ["classpath:/spring-mybatis.xml"])
open class MapperTest {

    @Autowired
    var userMapper: UserMapper? = null

    @Autowired
    var configMapper: ConfigMapper? = null

    @Autowired
    var meterMapper: MeterMapper? = null

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
        val lst = meterMapper!!.selectMeterDma(mp)
        lgr.info("${mp.keywords} matched list (${lst.size}): {}", JSON.toJSONString(lst, true))
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(MapperTest::class.java)
    }
}