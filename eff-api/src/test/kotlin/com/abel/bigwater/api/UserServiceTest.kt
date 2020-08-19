package com.abel.bigwater.api

import com.abel.bigwater.model.BwUser
import com.alibaba.fastjson.JSON
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.junit.Assert
import org.junit.Test
import org.slf4j.LoggerFactory
import org.springframework.util.DigestUtils

class UserServiceTest {
    /**
     * 将该链接直接用浏览器打开，验证后台服务是否启动.
     * 注意：端口不能修改. (如希望修改端口, 需同时修改配置文件: /spring/rest-provider.xml.)
     */
    @Test
    fun testDummy() {
        val URL_GET = TestHelper.URL_BASE + "/user/11"
    }

    @Test
    fun testListLogin() {
        val post = HttpPost(TestHelper.URL_LOGIN).apply {
            entity = StringEntity(JSON.toJSONString(LoginRequest().also {
                it.userId = "abel"
                it.devId = "junit"
                it.timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
                it.clientHash = DigestUtils.md5DigestAsHex((DigestUtils.md5DigestAsHex("test".toByteArray()) + it.timestamp).toByteArray())
            }), ContentType.APPLICATION_JSON)
        }

        val resp = HttpClients.createDefault().execute(post)
        val json = resp.entity.content.reader().readText()
        val ret = ObjectMapper().readValue(json, TestHelper.BwUserLoginResult::class.java)
        Assert.assertEquals(0, ret.code)

        kotlin.run {
            val post2 = HttpPost(URL_USER_INFO).apply {
                entity = StringEntity(JSON.toJSONString(BwHolder<BwUser>().also {
                    it.lr = TestHelper.buildLoginRequest(ret.single!!)
                    it.single = BwUser().also { up ->
                        up.userId = ret.single?.userId
                    }
                }), ContentType.APPLICATION_JSON)
            }
            val resp2 = HttpClients.createDefault().execute(post2)
            val json2 = resp2.entity.content.reader().readText()
            lgr.info("user info: $json2")
        }

        kotlin.run {
            val post2 = HttpPost(URL_LIST_LOGIN).apply {
                entity = StringEntity(JSON.toJSONString(BwHolder<UserOperParam>().also {
                    it.lr = TestHelper.buildLoginRequest(ret.single!!)
                    it.single = UserOperParam().also { up ->
                        up.firmId = ret.single!!.firmId
                        up.userId = ret.single!!.userId
                    }
                }), ContentType.APPLICATION_JSON)
            }
            val resp2 = HttpClients.createDefault().execute(post2)
            val json2 = resp2.entity.content.reader().readText()
            lgr.info("login list: $json2")
        }
    }

    companion object {

        const val URL_LIST_LOGIN = TestHelper.URL_BASE + "/user/loginList"

        const val URL_USER_INFO = TestHelper.URL_BASE + "/user/info"

        private val lgr = LoggerFactory.getLogger(UserServiceTest::class.java)
    }
}