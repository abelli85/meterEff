package com.abel.bigwater.api

import com.abel.bigwater.model.BwData
import com.alibaba.fastjson.JSON
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.jboss.resteasy.util.HttpResponseCodes
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.junit.Test
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals

class DataServiceTest {
    companion object {
        private val lgr = LoggerFactory.getLogger(MeterServiceTest::class.java)
    }

    @Test
    fun testRealtime() {
        val ul = TestHelper.login()

        val list = listOf(BwData().apply {
            extId = "test-1"
            sampleTime = LocalDateTime(2020, 8, 1, 10, 0).toDate()
            forwardDigits = 123.0
            literPulse = 100
        }, BwData().apply {
            extId = "test-1"
            sampleTime = LocalDateTime(2020, 8, 1, 10, 30).toDate()
            forwardDigits = 123.0
            literPulse = 100
        })

        try {
            BwHolder(TestHelper.buildLoginRequest(ul.single!!), list).also { holder ->
                val url = TestHelper.URL_BASE + DataService.BASE_PATH + DataService.PATH_ADD_REALTIME_USER

                val post = HttpPost(url).apply {
                    entity = StringEntity(JSON.toJSONString(holder), ContentType.APPLICATION_JSON)
                }
                val resp = HttpClients.createDefault().execute(post)
                lgr.info("post realtime to {}: {} / {}", url, resp.statusLine, resp.entity.content.reader().readText())
                assertEquals(HttpResponseCodes.SC_OK, resp.statusLine.statusCode)
            }

            // list
            BwHolder(TestHelper.buildLoginRequest(ul.single!!), DataParam(extId = "test-1").apply {
                sampleTime1 = LocalDate(2020, 8, 1).toDate()
                sampleTime2 = LocalDate(2020, 8, 2).toDate()
            }).also { holder ->
                val url = TestHelper.URL_BASE + DataService.BASE_PATH + DataService.PATH_LIST_REALTIME

                val post = HttpPost(url).apply {
                    entity = StringEntity(JSON.toJSONString(holder), ContentType.APPLICATION_JSON)
                }
                val resp = HttpClients.createDefault().execute(post)
                lgr.info("list realtime to {}: {} / {}", url, resp.statusLine, resp.entity.content.reader().readText())
                assertEquals(HttpResponseCodes.SC_OK, resp.statusLine.statusCode)
            }
        } finally {
            // clear data
            val url = TestHelper.URL_BASE + DataService.BASE_PATH + DataService.PATH_DELETE_REALTIME
            val holder = BwHolder(TestHelper.buildLoginRequest(ul.single!!), list.map {
                DataParam(extId = it.extId).apply {
                    sampleTime = it.sampleTime
                }
            })
            val post = HttpPost(url).apply {
                entity = StringEntity(ObjectMapper().writeValueAsString(holder), ContentType.APPLICATION_JSON)
            }
            val resp = HttpClients.createDefault().execute(post)
            lgr.info("clear realtime to {}: {} / {}", url, resp.statusLine, resp.entity.content.reader().readText())
        }
    }
}