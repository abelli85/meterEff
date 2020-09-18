package com.abel.bigwater.model.stat

data class MeterFirmStat(
        /**
         * @return the id
         */
        var firmId: String? = null,

        /**
         * @return the name
         */
        var firmName: String? = null,

        /**
         * 水表数量
         */
        var meterCount: Int? = null) {

        /**
         * 口径
         * the size to set
         */
        var sizeId: Int? = null

        /**
         * 口径
         * the size to set
         */
        var sizeName: String? = null

        /**
         * 水表型号
         * the model to set
         */
        var modelSize: String? = null
}