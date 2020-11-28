package com.abel.bigwater.model

data class VcCode(var codeId: String? = null,

                  var codeName: String? = null,

                  var memo: String? = null,

                  var preInit: Boolean? = null) : BwBase() {

    /**
     * 代码值的数量
     */
    var valueCount: Int? = null
}