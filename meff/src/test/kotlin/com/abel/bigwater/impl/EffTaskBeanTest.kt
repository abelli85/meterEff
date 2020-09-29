package com.abel.bigwater.impl

import com.abel.bigwater.model.BwFirm
import org.apache.dubbo.remoting.http.servlet.ServletManager
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.mybatis.spring.annotation.MapperScan
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockServletContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@ContextConfiguration(locations = ["classpath:/spring/rest-provider.xml", "classpath:/spring-mybatis.xml"])
@RunWith(SpringJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.DEFAULT)
@MapperScan(basePackages = ["com.abel.bigwater.mapper"])
class EffTaskBeanTest {

    @Autowired
    var bean: EffTaskBean? = null

    @Test
    fun testEffFirm() {
        bean!!.effFirm(BwFirm().apply {
            firmId = "76"
        })
    }

    @Test
    fun effMeter() {
    }

    @Test
    fun effMeterRange() {
    }

    @Test
    fun testEffAll() {
        bean!!.effAll()
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(EffTaskBeanTest::class.java)

        @BeforeClass
        @JvmStatic
        fun addServletContext() {
            lgr.warn("prepare servlet container...")
            ServletManager.getInstance().addServletContext(ServletManager.EXTERNAL_SERVER_PORT, MockServletContext())
        }
    }
}