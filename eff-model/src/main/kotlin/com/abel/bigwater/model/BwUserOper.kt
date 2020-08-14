package com.abel.bigwater.model

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.joda.time.DateTime
import org.joda.time.Duration
import java.io.Serializable
import java.util.*

/**
 * 用户操作轨迹.
 * @author Abel
 */
data class BwUserOper(
        /**
         * 自增量标示
         */
        var operId: Long? = null,

        var userId: String? = null,

        var userName: String? = null,

        var firmId: String? = null,

        /**
         * 操作设备标识.长度不超过36个字符.
         */
        var devId: String? = null,
        /**
         * 功能点.
         */
        var operCase: String? = null,

        var operRole: String? = null,

        var operRight: String? = null,

        /**
         * 登录ip地址.
         */
        var loginIp: String? = null,

        /**
         * 登录主机名
         */
        var loginHost: String? = null,

        /**
         * 登录地区
         */
        var loginAddr: String? = null,

        /**
         * 登录经纬度
         */
        var loginLoc: String? = null,

        /**
         * 客户机网址
         */
        var clientIp: String? = null,

        /**
         * 服务器网址
         */
        var serverIp: String? = null,

        /**
         * 操作地区
         */
        var operCity: String? = null,

        /**
         * 成功- OK/0
         * 失败- FAIL/1/2/etc.
         */
        var operResult: Int? = null,

        var operCount: Int = 1,

        /**
         * 其他描述信息, 最大长度36字符.
         */
        var operDesc: String? = null) : Serializable {

    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var operTime: Date? = null

    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var returnTime: Date? = null

    /**
     * 花费时间, 单位: 秒.
     */
    val costSeconds: Long?
        get() = if (operTime == null || returnTime == null) 0
        else Duration(DateTime(operTime), DateTime(returnTime)).standardSeconds


    companion object {
        val FAIL = "FAIL"
        val OK = "OK"
    }
}