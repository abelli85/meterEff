package com.abel.bigwater.model

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.locationtech.jts.io.WKTReader
import java.util.*

// import org.springframework.format.annotation.DateTimeFormat;

open class BwMeter : BwBase() {
    /**
     * 本系统标示
     * the id to set
     */
    var meterId: String? = null

    /**
     * 用户编号
     * the userCode to set
     */
    var userCode: String? = null

    /**
     * 水表编号
     * the meterCode to set
     */
    var meterCode: String? = null

    /**
     * 水表名称
     * the name to set
     */
    var meterName: String? = null

    /**
     * 用来在combox/listbox中显示为标题。
     */
    var title: String? = ""
        get() = "${meterId} - ${meterName}"

    /**
     * 账册序号。
     * order in meter list.
     */
    var meterOrder: Int = 0

    /**
     * 数据标识码
     * the extId to set
     */
    var extId: String? = null

    /**
     * 安装地址
     * the location to set
     */
    var location: String? = null

    // @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    /**
     * 安装日期
     * the installDate to set
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var installDate: Date? = null

    /**
     * pulse for meter, generally be 1 / 10 / 100 / 1000.
     */
    var meterPulse: Double? = null

    /**
     * 计量点Q1(m³/h).下同
     * the q1 to set
     */
    var q1: Double = 0.0

    /**
     * the q2 to set
     */
    var q2: Double = 0.0

    /**
     * the q3 to set
     */
    var q3: Double = 0.0

    /**
     * the q4 to set
     */
    var q4: Double = 0.0

    /**
     * 计量点Q1的效率(%), 下同
     * the q1r to set
     */
    var q1r: Double = 0.0

    /**
     * the q2r to set
     */
    var q2r: Double = 0.0

    /**
     * the q3r to set
     */
    var q3r: Double = 0.0

    /**
     * the q4r to set
     */
    var q4r: Double = 0.0

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
     * 水表类型
     */
    @JsonIgnore
    var type: BwMeterType? = null
        set(value) {
            field = value
            if (field != null) typeId = field?.name
        }
        get() = BwMeterType.values().find { it.name == typeId }

    /**
     * 水表类型
     */
    var typeId: String? = null

    /**
     * 用水性质-如工业、栋表、消防等。
     */
    var userType: String? = null

    /**
     * 水价（元）, 保留2位小数。
     */
    var waterPrice: Double? = null

    /**
     * 用水面积（平方公里）, 保留2位小数.
     */
    var serviceArea: Double? = null

    /**
     * 用水人数（万人），保留2位小数
     */
    var servicePopulation: Double? = null

    /**
     * 联系电话
     */
    var contactNumber: String? = null

    /**
     * 是否计量表
     * 1 - true, 0 - false.
     */
    var chargable: Char = '0'
        set(value) {
            if (value < 1.toChar()) field = '0'
        }

    /** 0: IN; 1: OUT. Default to 0.  */
    var inOutput: Int = 0

    /**
     * DMA标示
     * the dmaId to set
     */
    var dmaId: String? = null

    /**
     * the name to set
     */
    var dmaName: String? = null

    /**
     * 水司标示
     * the firmId to set
     */
    var firmId: String? = null

    /**
     * 水表品牌标示
     * the meterBrandId to set
     */
    var meterBrandId: String? = null

    /**
     * 水表钢印号 / 表码
     */
    var steelNo: String? = null

    /**
     * 远传品牌标示
     * the remoteBrandId to set
     */
    var remoteBrandId: String? = null

    /**
     * 水表品牌
     * the meterBrand to set
     */
    @JsonIgnore
    var meterBrand: BwMeterBrand? = null
        set(value) {
            field = value
            meterBrandId = value?.brandId
        }

    /**
     * 远传品牌
     * the remoteBrand to set
     */
    @JsonIgnore
    var remoteBrand: BwRemoteBrand? = null
        set(value) {
            field = value
            remoteBrandId = value?.brandId
        }

    /**
     * 远传设备标示。
     * the rtuId to set
     */
    var rtuId: String? = null

    /**
     * 远传设备编号
     */
    var rtuCode: String? = null

    /**
     * 远传设备安装地址
     */
    var rtuAddr: String? = null

    /**
     * 远传设备安装日期。
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var rtuInstallDate: Date? = null

    /**
     * 远传设备安装人
     */
    var rtuInstallPerson: String? = null

    /**
     * 远传设备联系人
     */
    var rtuContact: String? = null

    /**
     * 数据卡号
     */
    var commCard: String? = null

    /**
     * 远传设备型号
     */
    var remoteModel: String? = null

    /**
     * 远传设备描述信息
     */
    var remoteMemo: String? = null

    /**
     * 运营商 - 中国移动、中国联通、中国电信.
     */
    var commIsp: String? = null

    /**
     * 压力范围（米）
     * the pressureRange to set
     */
    var pressureRange: Double = 0.0

    /**
     * 压力高限（米）
     * the pressureMaxLimit to set
     */
    var pressureMaxLimit: Double = 0.0

    /**
     * 压力底限（米）
     * the pressureMinLimit to set
     */
    var pressureMinLimit: Double = 0.0

    /**
     * 供电类型
     * the powerType to set
     */
    var powerType: PowerType? = null

    /**
     * 水表状态
     * the meterStatus to set
     */
    var meterStatus: MeterStatus? = null

    /**
     * 抄表员手机号
     * the adminMobile to set
     */
    var adminMobile: String? = null

    /**
     * 位置的WKT数据，对应矢量坐标Point。
     * 转换矢量坐标对象，请参考Geo（C#）等相关框架。
     * the meterLoc to set
     */
    var meterLoc: String? = null
        set(value) {
            field = if (!meterLoc.isNullOrBlank()) {
                try {
                    val g = WKTReader().read(value!!)
                    if ("Point".equals(g.geometryType, true)) {
                        value
                    } else null
                } catch (t: Throwable) {
                    null
                }
            } else null
        }

    /**
     * 最后校准日期
     * the lastCalib to set
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    var lastCalib: Date? = null

    /**
     * 水表描述.
     */
    var memo: String? = null

    override fun hashCode(): Int {
        return meterId?.hashCode() ?: 0
    }

    override fun equals(other: Any?): Boolean =
            meterId == (other as BwMeter?)?.meterId
}
