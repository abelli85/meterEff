package com.abel.bigwater.model

import com.abel.bigwater.model.JsonDateSerializer
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.io.Serializable
import java.util.*

abstract class VcBase : Serializable {

    open var createBy: String? = null

    /**
     * 记录创建的日期
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    open var createDate: Date? = null

    open var updateBy: String? = null

    /**
     * 记录更新的日期
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    open var updateDate: Date? = null
}