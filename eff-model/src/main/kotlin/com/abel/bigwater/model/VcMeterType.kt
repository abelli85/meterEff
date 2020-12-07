package com.abel.bigwater.model

data class VcMeterType(var typeId: String? = null,
                       var typeName: String? = null,
                       var memo: String? = null,

                       /**
                        * 预置标志，1 - 不可删改; 0 - 可删改.
                        */
                       var preInit: Boolean? = false) : BwBase() {
}

data class VcFactoryModel(var factId: String? = null,
                          var typeId: String? = null,

                          /**
                           * 规格型号
                           */
                          var modelSize: String? = null,

                          /**
                           * 备注
                           */
                          var modelList: String? = null,

                          /**
                           * 预置标志，1 - 不可删改; 0 - 可删改.
                           */
                          var preInit: Boolean? = false) : BwBase() {

    /**
     * 名称
     */
    var factName: String? = null

    /**
     * 口径
     * the size to set
     */
    var sizeId: Int? = null

    /**
     * 排序
     */
    var valueOrder: Int? = null
}