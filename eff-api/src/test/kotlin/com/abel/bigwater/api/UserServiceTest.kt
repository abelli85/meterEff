package com.abel.bigwater.api

import com.abel.bigwater.model.BwFirm
import com.abel.bigwater.model.BwUser
import com.alibaba.fastjson.JSON
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.junit.Test
import org.slf4j.LoggerFactory

class UserServiceTest {
    /**
     * 将该链接直接用浏览器打开，验证后台服务是否启动.
     * 注意：端口不能修改. (如希望修改端口, 需同时修改配置文件: /spring/rest-provider.xml.)
     */
    @Test
    fun testDummy() {
        val URL_GET = TestHelper.URL_BASE + "/user/11"
    }

    /**
     * 列出登录会话
     */
    @Test
    fun testListLogin() {
        val ul = TestHelper.login()

        kotlin.run {
            val post2 = HttpPost(URL_USER_INFO).apply {
                entity = StringEntity(JSON.toJSONString(BwHolder<BwUser>().also {
                    it.lr = TestHelper.buildLoginRequest(ul.single!!)
                    it.single = BwUser().also { up ->
                        up.userId = ul.single?.userId
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
                    it.lr = TestHelper.buildLoginRequest(ul.single!!)
                    it.single = UserOperParam().also { up ->
                        up.firmId = ul.single!!.firmId
                        up.userId = ul.single!!.userId
                    }
                }), ContentType.APPLICATION_JSON)
            }
            val resp2 = HttpClients.createDefault().execute(post2)
            val json2 = resp2.entity.content.reader().readText()
            lgr.info("login list: $json2")
        }
    }

    /**
     * 机构列表
     */
    @Test
    fun testFirmList() {
        val ul = TestHelper.login()

        val post = HttpPost(URL_FIRM_LIST).apply {
            entity = StringEntity(JSON.toJSONString(BwHolder<String>().also {
                it.lr = TestHelper.buildLoginRequest(ul.single!!)
            }), ContentType.APPLICATION_JSON)
        }

        val resp = HttpClients.createDefault().execute(post)
        val json = resp.entity.content.reader().readText()
        lgr.info("firm list response: {}", json)

        val rf = ObjectMapper().readValue<BwResultFirm>(json, BwResultFirm::class.java)
        lgr.info("firm list: {}", rf.list?.map { it.title }?.joinToString("\r\n"))
    }

    companion object {

        const val URL_LIST_LOGIN = TestHelper.URL_BASE + "/user/loginList"

        const val URL_USER_INFO = TestHelper.URL_BASE + "/user/info"

        const val URL_FIRM_LIST = TestHelper.URL_BASE + UserService.BASE_PATH + UserService.PATH_FIRM_LIST

        private val lgr = LoggerFactory.getLogger(UserServiceTest::class.java)

        class BwResultFirm : BwResult<BwFirm>()
    }
}