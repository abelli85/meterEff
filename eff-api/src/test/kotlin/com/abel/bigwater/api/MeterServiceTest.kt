package com.abel.bigwater.api

import com.abel.bigwater.model.zone.ZoneMeter
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
        open class HolderMeter : BwHolder<ZoneMeter>()

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
        val holder = BwHolder(TestHelper.buildLoginRequest(ul.single!!), MeterParam())

        val post = HttpPost(TestHelper.URL_BASE + MeterService.BASE_PATH + MeterService.PATH_LIST_ZONE_METER).apply {
            entity = StringEntity(JSON.toJSONString(holder), ContentType.APPLICATION_JSON)
        }

        val resp = HttpClients.createDefault().execute(post)
        assertEquals(HttpResponseCodes.SC_OK, resp.statusLine.statusCode)
        val json = resp.entity.content.reader().readText()
        lgr.info("list meter: {}", json)
    }

    @Test
    fun testInsertMeterJson() {
        val post = HttpPost(TestHelper.URL_BASE + MeterService.BASE_PATH + MeterService.PATH_INSERT_ZONE_METER).apply {
            val json = """{"lr":{"userId":"abel","timestamp":"13:43:24","clientHash":"d31d71d44bf59f751bed1ffeb19b6c79","devId":"junit","sessionId":"e235c43d-7393-436b-8a58-8be23971a08b"},"param1":null,"param2":null,"single":{"zoneId":"123","flowOut":0,"meterId":"05600","userCode":"123","meterCode":"123123123","meterName":"123","meterOrder":null,"extId":null,"location":"123","installDate":"2020-08-20T00:00:00","meterPulse":null,"q1":null,"q2":null,"q3":null,"q4":null,"q1r":null,"q2r":null,"q3r":null,"q4r":null,"sizeId":"123","sizeName":"123","modelSize":"312","typeId":null,"userType":null,"waterPrice":null,"serviceArea":null,"servicePopulation":null,"contactNumber":null,"chargable":0,"inOutput":null,"dmaId":null,"firmId":"2705","meterBrandId":"123","steelNo":"123","remoteBrandId":"213","rtuId":"23","rtuCode":null,"rtuAddr":null,"rtuInstallDate":null,"rtuInstallPerson":null,"rtuContact":null,"commCard":null,"remoteModel":null,"remoteMemo":null,"commIsp":null,"pressureRange":null,"pressureMaxLimit":null,"pressureMinLimit":null,"powerType":0,"meterStatus":0,"adminMobile":null,"meterLoc":"213","lastCalib":null,"memo":"123","createBy":null,"createDate":null,"updateBy":null,"updateDate":null},"list":null}"""
            entity = StringEntity(json, ContentType.APPLICATION_JSON)
        }

        HttpClients.createDefault().execute(post).also { resp ->
            assertEquals(HttpResponseCodes.SC_OK, resp.statusLine.statusCode)
            val json = resp.entity.content.reader().readText()
            lgr.info("list meter: {}", json)
        }
    }
}