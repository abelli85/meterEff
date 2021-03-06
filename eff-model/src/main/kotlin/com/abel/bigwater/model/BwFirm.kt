package com.abel.bigwater.model

import com.abel.bigwater.model.stat.MeterFirmStat
import org.locationtech.jts.io.WKTReader

class BwFirm : BwBase() {
    /**
     * @return the id
     */
    var firmId: String? = null

    /**
     * @return the name
     */
    var firmName: String? = null

    /**
     * 单位层级, 根节点为1.
     * 任何层级用户登录, 其看到的机构层级不变.
     */
    var firmLevel: Int? = null

    /**
     * 位置的WKT数据，对应矢量坐标Point。
     * 转换矢量坐标对象，请参考Geo（C#）等相关框架。
     */
    var firmLoc: String? = null
        set(value) {
            field = if (!value.isNullOrBlank()) {
                try {
                    val g = WKTReader().read(value)
                    if ("Point".equals(g.geometryType, true)) {
                        value
                    } else null
                } catch (t: Throwable) {
                    null
                }
            } else null
        }

    /**
     * 水司边界的WKT数据，对应矢量边界Polygon。
     * 转换矢量坐标对象，请参考Geo（C#）等相关框架。
     */
    var firmRegion: String? = null
        set(value) {
            field = if (!value.isNullOrBlank()) {
                try {
                    val g = WKTReader().read(value)
                    if ("Polygon".equals(g.geometryType, true)) {
                        value
                    } else null
                } catch (t: Throwable) {
                    null
                }
            } else null
        }

    /**
     * 小logo
     */
    var smallIcon: String? = null

    /**
     * 大logo
     */
    var largeIcon: String? = null

    /**
     * 技术水平等级
     */
    var grade: Int? = null

    /**
     * 地址
     */
    var addr: String? = null

    /**
     * 邮政编码
     */
    var postcode: String? = null

    /**
     * 联系人
     */
    var contact: String? = null

    /**
     * 电话, 一般是固话
     */
    var phone: String? = null

    /**
     * 电话2
     */
    var phone2: String? = null

    /**
     * 传真
     */
    var fax: String? = null

    /**
     * 邮箱
     */
    var email: String? = null

    /**
     * 备注
     */
    var memo: String? = null

    /**
     * 预置标志，1 - 不可删改; 0 - 可删改.
     */
    var preinit: Boolean? = false

    /**
     * count of meters currently.
     */
    var meterCount: Int? = null

    /**
     * 口径列表
     */
    var sizeList: List<MeterFirmStat>? = null

    /**
     * 用来在combox/listbox中显示为标题。
     */
    var title: String? = ""
        get() = "${firmId} - ${firmName}"

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    override fun toString(): String {
        return "BwFirm [id=$firmId, name=$firmName]"
    }

    override fun hashCode(): Int {
        return firmId?.hashCode() ?: 0
    }

    override fun equals(other: Any?): Boolean {
        if (other is BwFirm) {
            return firmId == (other as BwFirm).firmId
        }
        return false
    }
}
