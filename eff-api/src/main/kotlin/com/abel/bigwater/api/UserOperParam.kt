package com.abel.bigwater.api

import com.abel.bigwater.api.BaseParam
import com.abel.bigwater.model.JsonDateDeserializer
import com.abel.bigwater.model.JsonDateSerializer
import com.abel.bigwater.model.JsonHelper
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

data class UserOperParam(
        /** 用户ID通配符 */
        var userId: String? = null,

        /** 水司ID通配符 */
        var firmId: String? = null,

        /** 设备ID */
        var devId: String? = null,

        /**
         * 计量标准自增量
         */
        var aid: Long? = null,

        /**
         * 用户登录后得到的门卡
         * @return the sessionId
         */
        var sessionId: String? = null,

        /**
         * 功能点.
         */
        var operCase: String? = null,

        /** 操作者身份 */
        var operRole: String? = null,

        /** 操作权限 */
        var operRight: String? = null) : BaseParam() {

    /** 时段开始 */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var operTime1: Date? = null

    /** 时段结束 */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var operTime2: Date? = null

    /**
     * 计量授权证书有效期至	2022年3月25日
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var stdValidDate: Date? = null

    /**
     * 计量授权证书有效期至	2022年3月25日
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var authValidDate: Date? = null
}