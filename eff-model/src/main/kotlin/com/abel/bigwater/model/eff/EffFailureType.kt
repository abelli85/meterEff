package com.abel.bigwater.model.eff

enum class EffFailureType(val msg: String) {
    DATA("数据不足"),

    POINT("计量点不足3个或Q2/Q3不存在"),

    ABSENTPOINT("计量点不能为空"),

    ABSENTTIME("采样时间不能为空"),

    OTHER("其他");
}