package com.abel.bigwater.model.eff

enum class EffPointType {
    /**
     * 模式(只分离用水模式, 不分析计量效率)
     */
    MODEL,

    /**
     * 效率(包含用水模式及计量效率)
     */
    EFF;
}