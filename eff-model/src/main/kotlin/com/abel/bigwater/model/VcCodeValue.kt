package com.abel.bigwater.model

class VcCodeValue : BwBase() {

    var codeId: String? = null

    var valueId: String? = null

    var valueName: String? = null

    var valueOrder: Int? = null

    /**
     * 类型.
     * @see VcCodeValueType
     */
    var valueType: String? = null

    var preInit: Boolean? = null

    /**
     * 禁用, 0 - 默认不禁用; 1 - 禁用
     */
    var disabled: Boolean = false

    companion object {
        const val TYPE_INT = "INT"
        const val TYPE_VARCHAR = "VARCHAR"
        const val TYPE_DATE = "DATE"
        const val TYPE_FLOAT = "FLOAT"
    }
}

enum class VcCodeValueType(val fmt: String) {
    INT("整数"),

    VARCHAR("字符串"),

    DATE("日期"),

    TIME("时间"),

    DECIMAL("小数")
}