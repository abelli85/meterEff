package com.abel.bigwater.api

import com.abel.bigwater.model.VcCodeValue

/**
 * 机构ID移到基类中:
 * @see BaseParam.firmId
 */
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

    /**
     * 预置代码值只能修改
     * @see VcCodeValue.valueOrder
     */
    var valueList: List<VcCodeValue>? = null

    /** 用户ID */
    var userId: String? = null
}