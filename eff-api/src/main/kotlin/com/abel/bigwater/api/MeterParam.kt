package com.abel.bigwater.api

import com.abel.bigwater.model.JsonDateDeserializer
import com.abel.bigwater.model.JsonDateSerializer
import com.abel.bigwater.model.JsonHelper
import com.alibaba.fastjson.annotation.JSONField
import com.alibaba.fastjson.serializer.DateCodec
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

data class MeterParam(var meterId: String? = null,
                      var meterIdList: List<String>? = null,
                      var meterName: String? = null,
                      var meterCode: String? = null,
                      var userCode: String? = null,
                      var extId: String? = null,
                      var extIdList: List<String>? = null,

                      /**
                       * DMA
                       */
                      var dmaId: String? = null,
                      var dmaName: String? = null,
                      var dmaIdList: List<String>? = null,

                      /**
                       * 片区
                       */
                      var zoneId: String? = null,
                      var zoneName: String? = null,
                      var zoneIdList: List<String>? = null,

                      var typeId: String? = null,
                      var location: String? = null,

                      var rtuId: String? = null,

                      var meterBrandId: String? = null,
                      var remoteBrandId: String? = null,

                      /**
                       * 请试用实体词前后加*，全文检索效率较高，如 *鹏兴* 、 *莲塘* 、 等
                       * 不支持量词、通用词的全文检索，如 *花园* 、 *一期* 、*五期* 等。
                       */
                      var keywords: String? = null,
                      var firmId: String? = null) : BaseParam() {
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(serializeUsing = DateCodec::class, format = JsonHelper.FULL_DATE_FORMAT)
    var installDate1: Date? = null

    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(serializeUsing = DateCodec::class, format = JsonHelper.FULL_DATE_FORMAT)
    var installDate2: Date? = null

    var instrumentNo: String? = null

    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(serializeUsing = DateCodec::class, format = JsonHelper.FULL_DATE_FORMAT)
    var verifyDate: Date? = null

    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(serializeUsing = DateCodec::class, format = JsonHelper.FULL_DATE_FORMAT)
    var verifyDateStart: Date? = null

    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(serializeUsing = DateCodec::class, format = JsonHelper.FULL_DATE_FORMAT)
    var verifyDateEnd: Date? = null

    /**
     * 委托编号
     */
    var batchId: String? = null

    var index: Int = 0
    var rows: Int = 20000

}