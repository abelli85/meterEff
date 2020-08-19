package com.abel.bigwater.model

data class VcCode(var codeId: String? = null,

                  var codeName: String? = null,

                  var memo: String? = null,

                  var preInit: Boolean? = null) : BwBase() {

}