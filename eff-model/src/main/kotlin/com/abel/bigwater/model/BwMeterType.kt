/**
 *
 */
package com.abel.bigwater.model

/**
 * @author Abel5
 */
enum class BwMeterType(val typeName: String) {
    /** 对照表 */
    CHECK("对照表"),

    /** 消防表 */
    FIRE("消防表"),

    /** 消费表 */
    CHARGE("消费表"),

    /** 户表 */
    USER("户表"),

    /** 用户对照表 */
    USERCHECK("用户对照表"),

    /**
     * 车载
     * @obsolete
     */
    VEHICLE("车载"),

    ALL("全部");
}
