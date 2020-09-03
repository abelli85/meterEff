package com.abel.bigwater.model.stat

data class MeterSizeStat(
        /**
         * 口径
         * the size to set
         */
        var sizeId: Int? = null,

        /**
         * 口径
         * the size to set
         */
        var sizeName: String? = null,

        /**
         * 水表数量
         */
        var meterCount: Int? = null) {
}