package com.abel.bigwater.model.zone

enum class SumMethod {
    /** 来自模板 */
    FROM_TEMP,

    /** 来自参考历史 */
    FROM_REF,

    /** 实时 */
    REALTIME,

    /** 混合模式 */
    MIXED,

    /** 缺失*/
    ABSENT
}