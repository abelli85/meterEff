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
}