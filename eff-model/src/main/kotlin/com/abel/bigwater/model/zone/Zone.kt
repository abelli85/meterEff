package com.abel.bigwater.model.zone

import com.abel.bigwater.model.BwBase

class Zone : BwBase() {
    public val LEAF_DMA = "DMA"
    public val LEAF_STAT = "STAT"
    public val LEAF_MIXED = "MIXED"

    /**
     * 片区标示
     */
    var zoneId: String = ""

    /**
     * 片区名称
     */
    var zoneName: String = ""

    /**
     * 用来在combox/listbox中显示为标题。
     */
    var title: String? = ""
        get() = "${zoneId} - ${zoneName}"

    /**
     * 片区层级。
     */
    var level: Int = 0

    var zoneType: String? = null

    /**
     * 片区类型
     */
    var type: ZoneType = ZoneType.FAMILY
        get() = ZoneType.values().first { it.name == zoneType } ?: ZoneType.FAMILY
        set(value) {
            field = value
            zoneType = field.name
        }

    var reff: Double = 7.0

    var leafType: String = "DMA"

    /**
     * 备注
     */
    var memo: String? = null

    var defaultRef: RefMethod = RefMethod.WEEK_HOUR

    /** 大表数量 */
    var bigMeterCount: Int = 0

    /** 居民表数量 */
    var resiMeterCount: Int = 0

    /** 在线大表数量 */
    var onlineBigMeterCount: Int = 0

    /** 在线小表数量 */
    var onlineResiMeterCount: Int = 0

    /**
     * 位置的WKT数据，对应矢量坐标Point。
     * 转换矢量坐标对象，请参考Geo（C#）等相关框架。
     */
    var zoneLoc: String? = null

    /**
     * 水司边界的WKT数据，对应矢量边界Polygon。
     * 转换矢量坐标对象，请参考Geo（C#）等相关框架。
     */
    var zoneRegion: String? = null

    /**
     * 水司标示
     */
    var firmId: String = ""

    /**
     * 包含的水表清单, 按进出水可划分为 进水、出水; 按供需类型可分为 总表、分表.
     * @see ZoneMeter.flowOut
     * @see ZoneMeter.childType
     */
    var meterList: List<ZoneMeter>? = null

    /**
     * 是否叶子节点，即不再包含下级分区
     * 0 - 非叶子节点
     */
    @Volatile
    var leafable: Int? = 0

    override fun toString(): String {
        return "{id:$zoneId,name:$zoneName}"
    }

    override fun hashCode(): Int {
        return zoneId.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if (other is Zone) (other as Zone).zoneId == zoneId else false
    }
}