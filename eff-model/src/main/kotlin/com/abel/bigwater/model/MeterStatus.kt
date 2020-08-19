package com.abel.bigwater.model

enum class MeterStatus {
    /** 在用 */
    WORK,

    /** 暂停 */
    PAUSE,

    /** 报废 */
    RETIRE,

    /** 其他 */
    RESERVED
}
