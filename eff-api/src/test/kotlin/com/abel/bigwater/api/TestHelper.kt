package com.abel.bigwater.api

import com.abel.bigwater.model.BwUserLogin
import com.alibaba.fastjson.JSON
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.junit.Assert
import org.slf4j.LoggerFactory
import org.springframework.util.DigestUtils

object TestHelper {
    const val URL_BASE = "http://bwsvr:8080/meff"
    const val URL_LOGIN = "$URL_BASE/user/login"
    private val lgr = LoggerFactory.getLogger(TestHelper::class.java)

    class BwUserLoginResult : BwResult<BwUserLogin>()

    fun buildLoginRequest(ul: BwUserLogin): LoginRequest {
        return LoginRequest().apply {
            userId = ul.userId
            devId = ul.devId
            sessionId = ul.sessionId
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            clientHash = DigestUtils.md5DigestAsHex((ul.sessionId + timestamp + ul.shareSalt).toByteArray())
        }
    }

    /**
     * 测试账号登录.
     */
    fun login(_userId: String? = "abel", _pass: String = "test"): BwResult<BwUserLogin> {
        val post = HttpPost(URL_LOGIN).apply {
            entity = StringEntity(JSON.toJSONString(LoginRequest().also {
                it.userId = _userId
                it.devId = "junit"
                it.timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
                it.clientHash = DigestUtils.md5DigestAsHex((DigestUtils.md5DigestAsHex(_pass.toByteArray()) + it.timestamp).toByteArray())
            }), ContentType.APPLICATION_JSON)
        }

        val resp = HttpClients.createDefault().execute(post)
        val json = resp.entity.content.reader().readText()
        lgr.info("login result from {}: {}...", URL_LOGIN, json.take(500))
        val ret = ObjectMapper().readValue(json, TestHelper.BwUserLoginResult::class.java)
        Assert.assertEquals(0, ret.code)
        return ret
    }
}