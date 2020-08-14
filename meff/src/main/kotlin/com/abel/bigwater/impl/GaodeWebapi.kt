package com.abel.bigwater.impl

import com.abel.bigwater.model.IpGeom
import com.abel.bigwater.model.IpGeomArray
import com.abel.bigwater.model.IpGeomSingle
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.protocol.HTTP
import org.jboss.resteasy.util.HttpResponseCodes
import org.locationtech.jts.geom.Coordinate
import org.slf4j.LoggerFactory
import sun.net.util.IPAddressUtil


/**
 * 通过IP地址获取地理位置
 * http://restapi.amap.com/v3/ip?key=dfc67a835b104c39a28b7e0e8db772b0
 */
object GaodeWebapi {
    const val GAODE_WEBAPI_KEY = "dfc67a835b104c39a28b7e0e8db772b0"
    val lgr = LoggerFactory.getLogger(GaodeWebapi::class.java)

    fun requestGeom(ip: String?): IpGeom? {
        val get = HttpGet(
                if (ip.isNullOrBlank()
                        || ip.equals("127.0.0.1")
                        || ip.equals("localhost")
                        || ip.equals("0:0:0:0:0:0:0:1")
                        || internalIp(ip!!))
                    "http://restapi.amap.com/v3/ip?key=$GAODE_WEBAPI_KEY"
                else "http://restapi.amap.com/v3/ip?key=$GAODE_WEBAPI_KEY&ip=${ip}").apply {
            addHeader("Accept-Charset", "utf-8");
            setHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON_UTF_8);
            addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.160 Safari/537.22");
        }

        lgr.info("get: ${get}")

        val hc = HttpClients.createDefault()
        try {
            val resp = hc.execute(get)
            if (resp.statusLine.statusCode == HttpResponseCodes.SC_OK) {
                val json = resp.entity.content.reader().readText()
                lgr.info("geom for ${ip} ~ ${json}")
                var ipGeom: IpGeom? = null
                try {
                    ipGeom = ObjectMapper().readValue(json, IpGeomSingle::class.java)
                } catch (e1: Exception) {
                    try {
                        ipGeom = ObjectMapper().readValue(json, IpGeomArray::class.java)
                    } catch (e2: Exception) {
                    }
                }

                return ipGeom
            }
        } catch (ex: Exception) {
            lgr.error("Fail to retrieve addr from ip caused by ${ex.message}")
            return null
        }

        return null
    }

    /**
     * http://restapi.amap.com/v3/geocode/geo?address=北京市朝阳区阜通东大街6号&output=XML&key=<用户的key>
     *
     */
    fun requestGeomByAddr(address: String): GaodeGeo? {
        val get = HttpGet("http://restapi.amap.com/v3/geocode/geo?key=$GAODE_WEBAPI_KEY&address=${java.net.URLEncoder.encode(address, Charsets.UTF_8.name())}").apply {
            addHeader("Accept-Charset", "utf-8");
            setHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON_UTF_8);
            addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.160 Safari/537.22");
        }

        lgr.info("get: ${get}")
        val hc = HttpClients.createDefault()
        try {
            val resp = hc.execute(get)
            if (resp.statusLine.statusCode == HttpResponseCodes.SC_OK) {
                val json = resp.entity.content.reader().readText()
                lgr.info("geom for ${address} ~ ${json}")

                return ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                        .readValue(json, GaodeGeo::class.java)
            }
        } catch (ex: Exception) {
            lgr.error("Fail to retrieve addr from ip caused by ${ex.message}")
            return null
        }

        return null
    }

    data class GaodeGeo(var status: String? = null,
                        var count: String? = null,
                        var info: String? = null,
                        var infocode: String? = null,
                        var geocodes: List<GeoCode>? = null) {
    }

    @JsonIgnoreProperties("township", "neighborhood", "building", "street", "number")
    data class GeoCode(var formatted_address: String? = null,
                       var country: String? = null,
                       var province: String? = null,
                       var citycode: String? = null,
                       var city: String? = null,
                       var district: String? = null,
                       var adcode: String? = null,
                       /** "location" :
                       "116.480724,39.989584",*/
                       var location: String? = null,
                       var level: String? = null) {
        @JsonIgnore
        @JSONField(serialize = false)
        var coordinate: Coordinate? = null
            get() {
                val locList = location?.split(",", " ")?.filter { it.isNotBlank() }
                return if (locList?.size == 2)
                    Coordinate(locList[0].toDouble(), locList[1].toDouble())
                else null
            }

    }

    /**
     * tcp/ip协议中，专门保留了三个IP地址区域作为私有地址，其地址范围如下：

    10.0.0.0/8：10.0.0.0～10.255.255.255
    　　172.16.0.0/12：172.16.0.0～172.31.255.255
    　　192.168.0.0/16：192.168.0.0～192.168.255.255
     */
    fun internalIp(ip: String): Boolean {
        val addr = IPAddressUtil.textToNumericFormatV4(ip)
        return if (addr != null) internalIp(addr) else true
    }

    fun internalIp(addr: ByteArray): Boolean {
        val b0 = addr[0]
        val b1 = addr[1]
        //10.x.x.x/8
        val SECTION_1: Byte = 0x0A
        //172.16.x.x/12
        val SECTION_2 = 0xAC.toByte()
        val SECTION_3 = 0x10.toByte()
        val SECTION_4 = 0x1F.toByte()
        //192.168.x.x/16
        val SECTION_5 = 0xC0.toByte()
        val SECTION_6 = 0xA8.toByte()
        when (b0) {
            SECTION_1 -> return true
            SECTION_2 -> {
                if (b1 >= SECTION_3 && b1 <= SECTION_4) {
                    return true
                }
                when (b1) {
                    SECTION_6 -> return true
                }
                return false
            }
            SECTION_5 -> {
                when (b1) {
                    SECTION_6 -> return true
                }
                return false
            }
            else -> return false
        }
    }
}