package com.abel.bigwater.api

import com.abel.bigwater.model.VcCode
import com.alibaba.fastjson.JSON
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.jboss.resteasy.util.HttpResponseCodes
import org.slf4j.LoggerFactory
import kotlin.test.Test
import kotlin.test.assertEquals

class CodeServiceTest {
    companion object {
        private val lgr = LoggerFactory.getLogger(CodeServiceTest::class.java)
        const val URL_LIST_CODE = TestHelper.URL_BASE + CodeService.URL_BASE + CodeService.PATH_LIST_CODE
    }

    @Test
    fun testCodeList() {
        val ul = TestHelper.login()

        val holder = BwHolder<VcCode>(TestHelper.buildLoginRequest(ul.single!!), VcCode())
        val post = HttpPost(URL_LIST_CODE).apply {
            entity = StringEntity(JSON.toJSONString(holder), ContentType.APPLICATION_JSON)
        }

        val resp = HttpClients.createDefault().execute(post)
        assertEquals(HttpResponseCodes.SC_OK, resp.statusLine.statusCode)
        val json = resp.entity.content.reader().readText()
        lgr.info("response json: {}", json)
    }
}