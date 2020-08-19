package com.abel.bigwater.api

data class CodeParam(

        /**
         * 自增量, 唯一编号
         */
        var platId: Long? = null,

        /**
         * 编号
        JB01
         */
        var userNo: String? = null,

        /**
         * 出厂编号
         */
        var manuNo: String? = null,

        /**
         * 公司ID
         */
        var firmId: String? = null,

        /**
         * 是否自动同步, 默认0 - 不自动同步.
         */
        var autoSync: Int? = null
) : BaseParam() {

    /**
     * 仅作为查询条件
     * 编号
    JB01
     */
    var userNoList: List<String>? = null

    /**
     * 仅作为查询条件
     * 出厂编号
     */
    var manuNoList: List<String>? = null
}