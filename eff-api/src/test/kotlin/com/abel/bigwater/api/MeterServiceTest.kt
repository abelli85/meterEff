package com.abel.bigwater.api

import com.alibaba.fastjson.JSON
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.jboss.resteasy.util.HttpResponseCodes
import org.slf4j.LoggerFactory
import kotlin.test.Test
import kotlin.test.assertEquals

class MeterServiceTest {
    companion object {
        private val lgr = LoggerFactory.getLogger(MeterServiceTest::class.java)
    }

    @Test
    fun testListMeter() {
        val ul = TestHelper.login()

        val meter = MeterParam().apply {
            meterId = "test-meterId"
            meterName = "测试水表01"
            userCode = "123456"
            meterCode = "12345678"
        }
        val holder = BwHolder(TestHelper.buildLoginRequest(ul.single!!), meter)

        val post = HttpPost(TestHelper.URL_BASE + MeterService.BASE_PATH + MeterService.PATH_LIST_ZONE_METER).apply {
            entity = StringEntity(JSON.toJSONString(holder), ContentType.APPLICATION_JSON)
        }

        val resp = HttpClients.createDefault().execute(post)
        assertEquals(HttpResponseCodes.SC_OK, resp.statusLine.statusCode)
        val json = resp.entity.content.reader().readText()
        lgr.info("list meter: {}", json)
    }
}