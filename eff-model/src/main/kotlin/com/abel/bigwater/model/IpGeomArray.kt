package com.abel.bigwater.model

import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Envelope
import org.locationtech.jts.geom.GeometryFactory


/**
 * http://restapi.amap.com/v3/ip?key=...&ip=...
 * {"status":"1","info":"OK","infocode":"10000","province":"北京市","city":"北京市","adcode":"110000","rectangle":"116.0119343,39.66127144;116.7829835,40.2164962"}
 *
 * {"status":"1","info":"OK","infocode":"10000","province":[],"city":[],"adcode":[],"rectangle":[]}
 */
open class IpGeomArray : IpGeom() {

    /** 国家名称 */
    var country: String? = null

    /**
    省份名称
    若为直辖市则显示直辖市名称；
    如果在局域网 IP网段内，则返回“局域网”；
    非法IP以及国外IP则返回空
     */
    var province: Array<String>? = null

    /**
    城市名称
    若为直辖市则显示直辖市名称；
    如果为局域网网段内IP或者非法IP或国外IP，则返回空
     */
    var city: Array<String>? = null

    /**
    城市的adcode编码
     */
    var adcode: Array<String>? = null

    /**
    所在城市矩形区域范围
    所在城市范围的左下右上对标对
     */
    var rectangle: Array<String>? = null
        set(value) {
            field = value

            if (value == null || value.size == 0) {
//                plgon = null
                enlp = null
            } else {
                val vlist = value[0].split(" ", ",", ";")
                if (vlist.size == 4) {
                    try {
                        val lng1 = vlist[0].toDouble()
                        val lat1 = vlist[1].toDouble()
                        val lng2 = vlist[2].toDouble()
                        val lat2 = vlist[3].toDouble()

                        enlp = Envelope(lng1, lng2, lat1, lat2)

                        val gf = GeometryFactory()
                        plgon = gf.createPolygon(gf.createLinearRing(arrayOf(
                                Coordinate(lng1, lat1),
                                Coordinate(lng1, lat2),
                                Coordinate(lng2, lat2),
                                Coordinate(lng2, lat1),
                                Coordinate(lng1, lat1))))
                    } catch (ex: Exception) {
                        // invalid
                        println("Fail to create plgon & enlp caused by ${ex.message}")
                    }
                }
            }
        }

    /**
    省份名称
    若为直辖市则显示直辖市名称；
    如果在局域网 IP网段内，则返回“局域网”；
    非法IP以及国外IP则返回空
     */
    override fun provinceStr(): String? {
        return province?.joinToString { "," }
    }

    /**
    城市名称
    若为直辖市则显示直辖市名称；
    如果为局域网网段内IP或者非法IP或国外IP，则返回空
     */
    override fun cityStr(): String? {
        return city?.joinToString { "," }
    }

    /**
    城市的adcode编码
     */
    override fun adcodeStr(): String? {
        return adcode?.joinToString { "," }
    }

    /**
    所在城市矩形区域范围
    所在城市范围的左下右上对标对
     */
    override fun rectangleStr(): String? {
        return rectangle?.joinToString { "," }
    }
}