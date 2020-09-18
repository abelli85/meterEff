package com.abel.bigwater.api

import com.abel.bigwater.model.*
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

open class BaseParam : BwBase() {

    /** 水司标示 */
    var firmId: String? = null

    /**
     * 记录创建的日期
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var createDateStart: Date? = null

    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var createDateEnd: Date? = null

    /**
     * 记录更新的日期
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var updateDateStart: Date? = null

    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var updateDateEnd: Date? = null

    /**
     * 超级用户, firmId不变;
     * 否则使用login.firmId或者其子公司的firmId.
     * e.g., ('23', '1') = '23';
     * ('2301', '23') = '2301';
     * ('2401', '23') = '23';
     * @param login firm-id needs to be refined
     * @param withWildcard firm-id of login user.
     */
    fun <T : BaseParam> refineFirmId(login: BwUserLogin, withWildcard: Boolean = true): T {
        // 超级用户
        if (login.firmId == TOP_FIRM_ID) {
            if (withWildcard && !firmId.orEmpty().endsWith('%')) {
                firmId += '%'
            }
        } else if (firmId?.startsWith(login.firmId.orEmpty()) == false) {
            // 必须为本司或子公司
            firmId = if (withWildcard) login.firmId?.plus('%') else login.firmId
        }

        return this as T
    }

    fun refineFirmId(login: BwUserLogin, withWildcard: Boolean = true) {
        // 超级用户
        if (login.firmId == TOP_FIRM_ID) {
            if (withWildcard && !firmId.orEmpty().endsWith('%')) {
                firmId += '%'
            }
        } else if (firmId?.startsWith(login.firmId.orEmpty()) == false) {
            // 必须为本司或子公司
            firmId = if (withWildcard) login.firmId?.plus('%') else login.firmId
        }
    }

    companion object {
        const val TOP_FIRM_ID = "1"
    }
}