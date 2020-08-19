package com.abel.bigwater.model

/**
 * 水表厂商
 */
class VcFactory : BwBase() {
    /**
     * 厂家标示
     */
    var factId: String? = null

    /**
     * 名称
     */
    var factName: String? = null

    /**
     * 地址
     */
    var addr: String? = null

    /**
     * 联系人
     */
    var contact: String? = null

    /**
     * 电话, 一般是固话
     */
    var phone: String? = null

    /**
     * 电话2
     */
    var phone2: String? = null

    /**
     * 传真
     */
    var fax: String? = null

    /**
     * 邮箱
     */
    var email: String? = null

    /**
     * 备注
     */
    var memo: String? = null

    /**
     * 预置标志，1 - 不可删改; 0 - 可删改.
     */
    var preInit: Boolean? = null
}