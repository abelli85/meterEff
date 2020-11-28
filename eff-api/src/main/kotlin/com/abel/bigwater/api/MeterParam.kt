package com.abel.bigwater.api

import com.abel.bigwater.model.JsonDateDeserializer
import com.abel.bigwater.model.JsonDateSerializer
import com.abel.bigwater.model.JsonHelper
import com.abel.bigwater.model.zone.ZoneMeter
import com.alibaba.fastjson.annotation.JSONField
import com.alibaba.fastjson.serializer.DateCodec
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

/**
 * 机构ID移到基类中:
 * @see BaseParam.firmId
 */
data class MeterParam(
        /**
         * 本系统标示
         * the id to set
         */
        var meterId: String? = null,

        /**
         * 包含的水表ID列表
         */
        var meterIdList: List<String>? = null,

        /**
         * 水表名称
         * the name to set
         */
        var meterName: String? = null,

        /**
         * 水表编号
         * the meterCode to set
         */
        var meterCode: String? = null,

        /**
         * 水表钢印号 / 表码
         */
        var steelNo: String? = null,

        /**
         * 用户编号
         * the userCode to set
         */
        var userCode: String? = null,

        /**
         * 数据标识码
         * the extId to set
         */
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


        /**
         * 水表品牌标示
         * the meterBrandId to set
         */
        var meterBrandId: String? = null,

        /**
         * 水表品牌名称
         * the meterBrandId to set
         */
        var meterBrandName: String? = null,

        var remoteBrandId: String? = null,

        /**
         * 使用 & | 符号拼接自己需要的向量: 福州|福州, 福州|中国|花园, 福州&公司, etc.
         * @deprecated
         * 请试用实体词前后加*，全文检索效率较高，如 *鹏兴* 、 *莲塘* 、 等
         * 不支持量词、通用词的全文检索，如 *花园* 、 *一期* 、*五期* 等。
         */
        var keywords: String? = null) : BaseParam() {
    /**
     * 采用拼音查询, 可配合汉字关键词:
     * @see keywords
     */
    var pinyin: String? = null

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

    /**
     * 老化模板ID
     */
    var decayId: Long? = null

    var index: Int = 0
    var rows: Int = 20000

    /**
     * 仅供后端批量更新使用.
     */
    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    var meterList: List<ZoneMeter>? = null

    /**
     * 仅供后端批量更新使用.
     * 下级机构ID, 一般采用 {@see firmId}%, 供后台统计查询.
     */
    @JsonIgnore
    @JSONField(serialize = false, deserialize = false)
    var subFirmId: String? = null
}