package com.abel.bigwater.model

enum class DataErr(
        /**
         * @return the name
         */
        val msg: String) {
    NoError(""),
    NotEnoughData("数据不全"),
    IncompatibleData("行度和水量不一致"),
    NoUsage("未用水"),
    NotSuitable("用水过程不适合分析")
}
