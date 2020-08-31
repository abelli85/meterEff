package com.abel.bigwater.impl

import com.abel.bigwater.api.EffService
import com.abel.bigwater.api.UserService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@ContextConfiguration(locations = ["classpath:/spring/rest-provider.xml", "classpath:/spring-mybatis.xml"])
@RunWith(SpringJUnit4ClassRunner::class)
class EffServiceImplTest {
    @Autowired
    var effService: EffService? = null

    @Autowired
    var loginService: UserService? = null

    @Test
    fun listEffTask() {
    }

    @Test
    fun fetchEffTask() {
    }

    @Test
    fun listMeterEff() {
    }

    @Test
    fun fetchMeterEff() {
    }
}