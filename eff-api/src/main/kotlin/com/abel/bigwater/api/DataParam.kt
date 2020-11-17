package com.abel.bigwater.api

import com.abel.bigwater.model.BwData
import com.abel.bigwater.model.JsonDateDeserializer
import com.abel.bigwater.model.JsonDateSerializer
import com.abel.bigwater.model.JsonHelper
import com.alibaba.fastjson.annotation.JSONField
import com.alibaba.fastjson.serializer.DateCodec
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.text.SimpleDateFormat
import java.util.*

/**
 * 机构ID移到基类中:
 * @see BaseParam.firmId
 */
data class DataParam(
        /** 水表标示 */
        var meterId: String? = null,

        /** 远传标示 */
        var extId: String? = null,

        /**
         * 水表编号
         * the meterCode to set
         */
        var meterCode: String? = null,

        /** 分区标示 */
        var dmaId: String? = null,

        /** 片区标示 */
        var zoneId: String? = null,

        /** RTU标示 */
        var rtuId: String? = null,

        /** 水表标示列表，使用IN(...) */
        var meterIdList: List<String>? = null,
        var extIdList: List<String>? = null,
        var dmaIdList: List<String>? = null,

        /** 是否计算，保留. */
        var calculating: Boolean? = false,

        /**
         * the table name to hold flow points.
         */
        var flowTable: String? = null,

        /**
         * the table name to hold pressure points.
         */
        var pressTable: String? = null,

        /**
         * @return the logId
         */
        var logId: Long = 0,

        var index: Int = 0,
        var rows: Int = 2000
) : BaseParam() {
    /** 数据间隔, 请注意{@link #durationInt}会被设置并使用。
     * 时间间隔，0/15/30返回的都是实时数据，请在客户端进行合并计算。
     * 60 - 在服务器端计算，返回的是小时数据.
     */
    @JsonIgnore
    var duration: DataDuration? = null
        get() = DataDuration.findByPeriod(durationInt)
        set(value) {
            field = value
            durationInt = field?.period
        }

    /** 时间间隔，默认为原始数据 */
    var durationInt: Int? = null

    /**
     * 用来选择单条数据.
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(serializeUsing = DateCodec::class, format = JsonHelper.FULL_DATE_FORMAT)
    var sampleTime: Date? = null

    /**
     * 用来选择一段时间内的数据, {@link sampleTime1} 为开始时间, {@link sampleTime2} 为结束时间.
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(serializeUsing = DateCodec::class, format = JsonHelper.FULL_DATE_FORMAT)
    var sampleTime1: Date? = null

    /**
     * used for non-prepared statement
     */
    var sampleTime1Str: String? = null
        get() = if (sampleTime1 == null) null
        else SimpleDateFormat(JsonHelper.LOCAL_DATE_SECOND_FORMAT_AREA).format(sampleTime1)

    /**
     * 用来选择一段时间内的数据, {@link sampleTime1} 为开始时间, {@link sampleTime2} 为结束时间.
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(serializeUsing = DateCodec::class, format = JsonHelper.FULL_DATE_FORMAT)
    var sampleTime2: Date? = null

    /**
     * used for non-prepared statement
     */
    var sampleTime2Str: String? = null
        get() = if (sampleTime2 == null) null
        else SimpleDateFormat(JsonHelper.LOCAL_DATE_SECOND_FORMAT_AREA).format(sampleTime2)

    /** 查询RTU日志的起始时间 */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(serializeUsing = DateCodec::class, format = JsonHelper.FULL_DATE_FORMAT)
    var logTime1: Date? = null

    /** 查询RTU日志的结束时间 */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(serializeUsing = DateCodec::class, format = JsonHelper.FULL_DATE_FORMAT)
    var logTime2: Date? = null

    /**
     * 仅供后台构造参数使用.
     */
    @JsonIgnore
    var dataList: List<BwData>? = null

    /**
     * 默认返回的最后数据天数
     */
    var lastDays: Int? = null
}

/**
 * 时间间隔，0/15/30返回的都是实时数据，请在客户端进行合并计算。
 * 60 - 在服务器端计算，返回的是小时数据.
 */
enum class DataDuration(var period: Int = 60) {
    /**
     * 当选择时长超过180天, 前端可使用月来查询
     */
    DURATION_MONTH(1440 * 30),

    /**
     * 当选择时长处于[60, 180]天, 前端可使用日来查询
     */
    DURATION_DAY(1440),

    /**
     * 默认查询小时数据
     */
    DURATION_60_MIN(60),

    /**
     * 30分钟间隔
     */
    DURATION_30_MIN(30),

    /**
     * 15分钟间隔
     */
    DURATION_15_MIN(15),

    /**
     * 原始数据
     */
    DURATION_0(0);

    override fun toString(): String {
        return period.toString()
    }

    companion object {
        /**
         * 根据间隔查找枚举对象
         */
        fun findByPeriod(_period: Int?) = values().find { it.period == _period }
    }
}