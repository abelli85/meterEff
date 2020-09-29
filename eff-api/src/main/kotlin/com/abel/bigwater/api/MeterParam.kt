package com.abel.bigwater.api

import com.abel.bigwater.model.JsonDateDeserializer
import com.abel.bigwater.model.JsonDateSerializer
import com.abel.bigwater.model.JsonHelper
import com.alibaba.fastjson.annotation.JSONField
import com.alibaba.fastjson.serializer.DateCodec
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

/**
 * 机构ID移到基类中:
 * @see BaseParam.firmId
 */
data class MeterParam(var meterId: String? = null,

                      /**
                       * 包含的水表ID列表
                       */
                      var meterIdList: List<String>? = null,

                      var meterName: String? = null,
                      var meterCode: String? = null,
                      var userCode: String? = null,

                      var extId: String? = null,
                      /**
                       * 包含的数据ID列表
                       */
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
                      var keywords: String? = null) : BaseParam() {
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
     * 口径
     * the size to set
     */
    var sizeId: Int? = null

    /**
     * 口径
     * the size to set
     */
    var sizeName: String? = null

    /**
     * 水表型号
     * the model to set
     */
    var modelSize: String? = null

    /**
     * 委托编号
     */
    var batchId: String? = null

    /**
     * 检定自动编号
     */
    var verifyId: Long? = null

    /**
     * 检定自动编号
     */
    var verifyIdList: List<Long>? = null

    /**
     * 自动标示编号
     */
    var pointId: Long? = null

    /**
     * 自动标示编号
     */
    var pointIdList: List<Long>? = null

    /**
     * 排除的水表ID列表
     */
    var excludeMeterIdList: List<String>? = null

    /**
     * 排除的数据ID列表
     */
    var excludeExtIdList: List<String>? = null

    var index: Int = 0
    var rows: Int = 20000

}