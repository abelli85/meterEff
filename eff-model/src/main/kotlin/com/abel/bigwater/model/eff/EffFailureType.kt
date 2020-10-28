package com.abel.bigwater.model.eff

enum class EffFailureType(val msg: String) {
    DATA("数据不足"),

    POINT("计量点不足3个或Q2/Q3不存在"),

    ABSENT_POINT("计量点不能为空"),

    ABSENT_TIME("采样时间不能为空"),

    ABSENT_LIKE("没有相近水量的远传表"),

    OTHER("其他");
}