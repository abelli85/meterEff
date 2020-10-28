package com.abel.bigwater.model

enum class PowerType {
    /** 电池 */
    BATTERY,

    /** 市电 */
    POWER,

    /** 太阳能 */
    SOLARPOWER,

    /**
     * 人工
     */
    MANUAL,

    /** 数据库 */
    DATABASE,

    /** 其他 */
    RESERVED
}
