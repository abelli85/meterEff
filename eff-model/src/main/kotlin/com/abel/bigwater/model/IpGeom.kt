package com.abel.bigwater.model

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonIgnore
import org.locationtech.jts.geom.Envelope
import org.locationtech.jts.geom.Polygon
import java.io.Serializable

/**
 * http://restapi.amap.com/v3/ip?key=...&ip=...
 * {"status":"1","info":"OK","infocode":"10000","province":"北京市","city":"北京市","adcode":"110000","rectangle":"116.0119343,39.66127144;116.7829835,40.2164962"}
 *
 * {"status":"1","info":"OK","infocode":"10000","province":[],"city":[],"adcode":[],"rectangle":[]}
 */
abstract class IpGeom : Serializable {

    /**
     * 主机名称
     */
    var hostName: String? = null

    /**
     * IP地址
     */
    var ipAddr: String? = null

    /**
    返回结果状态值
    值为0或1,0表示失败；1表示成功
     */
    var status: String? = null

    /**
    返回状态说明
    返回状态说明，status为0时，info返回错误原因，否则返回“OK”。
     */
    var info: String? = null

    /**
    状态码
    返回状态说明,10000代表正确,详情参阅info状态表
     */
    var infocode: String? = null

    /**
    省份名称
    若为直辖市则显示直辖市名称；
    如果在局域网 IP网段内，则返回“局域网”；
    非法IP以及国外IP则返回空
     */
    abstract fun provinceStr(): String?

    /**
    城市名称
    若为直辖市则显示直辖市名称；
    如果为局域网网段内IP或者非法IP或国外IP，则返回空
     */
    abstract fun cityStr(): String?

    /**
    城市的adcode编码
     */
    abstract fun adcodeStr(): String?

    /**
    所在城市矩形区域范围
    所在城市范围的左下右上对标对
     */
    abstract fun rectangleStr(): String?

    /**
     * 登录区域的外接矩形。
     */
    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    var enlp: Envelope? = null

    /**
     * 登录区域的多边形，用来构造WKT
     */
    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    var plgon: Polygon? = null
}