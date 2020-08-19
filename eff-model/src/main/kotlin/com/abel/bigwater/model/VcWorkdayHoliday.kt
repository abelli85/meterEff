package com.abel.bigwater.model

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

data class VcWorkdayHoliday(
        /**
         * 序号
         */
        var wid: Long? = null,

        /**
         * 年份
         */
        var yr: Int? = null,

        /**
         * 是否节日, 1 - 是; 0 - 不是. 下同
         */
        var holiday: Boolean = false,

        /**
         * 是否假日
         */
        var weekend: Boolean = false,

        /**
         * 是否工作日
         */
        var workday: Boolean = true,

        /**
         * 法定类型
         */
        var ruleType: String? = null) : BwBase() {

        /**
         * 开始日期, 默认一天的零点
         */
        @JsonSerialize(using = JsonDateSerializer::class)
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
        open var startDate: Date? = null

        /**
         * 结束日期, 默认{=startDate}一天的零点
         */
        @JsonSerialize(using = JsonDateSerializer::class)
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
        open var endDate: Date? = null
}