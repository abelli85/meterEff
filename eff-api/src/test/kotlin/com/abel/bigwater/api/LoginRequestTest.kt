package com.abel.bigwater.api

import com.abel.bigwater.api.TestHelper.URL_LOGIN
import com.abel.bigwater.model.BwUserLogin
import com.alibaba.fastjson.JSON
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.junit.Assert.assertEquals
import org.junit.Test
import org.slf4j.LoggerFactory
import org.springframework.util.DigestUtils

class LoginRequestTest {
    @Test
    fun testLoginFail1() {
        val post = HttpPost(URL_LOGIN).apply {
            entity = StringEntity(JSON.toJSONString(LoginRequest().also { it.userId = "dummy" }), ContentType.APPLICATION_JSON)
        }

        val resp = HttpClients.createDefault().execute(post)
        val json = resp.entity.content.reader().readText()
        lgr.info("response status: ${resp.statusLine}\n${json}")
        val ret = ObjectMapper().readValue(json, BwResult<BwUserLogin>().javaClass)
        assertEquals(2, ret.code)
    }

    @Test
    fun testLoginFail2() {
        val post = HttpPost(URL_LOGIN).apply {
            entity = StringEntity(JSON.toJSONString(LoginRequest().also {
                it.userId = "dummy"
                it.devId = "junit"
                it.timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
                it.clientHash = "dummy-hash"
            }), ContentType.APPLICATION_JSON)
        }

        val resp = HttpClients.createDefault().execute(post)
        val json = resp.entity.content.reader().readText()
        lgr.info("response status: ${resp.statusLine}\n${json}")
        val ret = ObjectMapper().readValue(json, BwResult<BwUserLogin>().javaClass)
        assertEquals(3, ret.code)
    }

    /**
     * 首次登陆使用口令，后续使用会话ID登陆。
     */
    @Test
    fun testLoginFirst2nd() {
        val post = HttpPost(URL_LOGIN).apply {
            entity = StringEntity(JSON.toJSONString(LoginRequest().also {
                it.userId = "abel"
                it.devId = "junit"
                it.timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
                it.clientHash = DigestUtils.md5DigestAsHex((DigestUtils.md5DigestAsHex("test".toByteArray()) + it.timestamp).toByteArray())
            }), ContentType.APPLICATION_JSON)
        }

        val resp = HttpClients.createDefault().execute(post)
        val json = resp.entity.content.reader().readText()
        lgr.info("response status: ${resp.statusLine}\n${json}")
        val ret = ObjectMapper().readValue(json, TestHelper.BwUserLoginResult::class.java)
        assertEquals(0, ret.code)

        // 后续登陆使用会话ID
        val post2 = HttpPost(URL_LOGIN).apply {
            entity = StringEntity(JSON.toJSONString(TestHelper.buildLoginRequest(ret.single!!)), ContentType.APPLICATION_JSON)
        }

        val resp2 = HttpClients.createDefault().execute(post2)
        val json2 = resp2.entity.content.reader().readText()
        lgr.info("second response status: ${resp2.statusLine}\n${json2}")
        val ret2 = ObjectMapper().readValue(json2, TestHelper.BwUserLoginResult::class.java)
        assertEquals(0, ret2.code)
    }

    companion object {

        private val lgr = LoggerFactory.getLogger(LoginRequestTest::class.java)
    }
}