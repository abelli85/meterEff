package com.abel.bigwater.model

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.io.Serializable
import java.util.*

abstract class BwBase : Serializable {
    /**
     * @return the createBy
     */
    var createBy: String? = null

    /**
     * @return the createDate
     */
    // @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var createDate: Date? = null

    /**
     * @return the updateBy
     */
    var updateBy: String? = null

    /**
     * @return the updateDate
     */
    // @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    @get:JsonSerialize(using = JsonDateSerializer::class)
    @set:JsonSerialize(using = JsonDateSerializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var updateDate: Date? = null

    companion object {
        const val TOP_FIRM_ID = "1"
    }
}
