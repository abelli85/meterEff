package com.abel.bigwater.api

import java.io.Serializable

/**
 * Request holder.
 */
class BwHolder<T : Serializable> : Serializable {

    var lr: LoginRequest? = null

    /**
     * 其他参数1/2
     */
    var param1: String? = null
    var param2: String? = null

    /**
     * 单个对象
     */
    var single: T? = null

    /**
     * 对象列表
     */
    var list: List<T>? = null

    /**
     * default ctor must exist.
     */
    constructor() {}

    constructor(lr: LoginRequest?, t1: T?) {
        this.lr = lr
        single = t1
    }

    constructor(lr: LoginRequest?, tlist: List<T>?) {
        this.lr = lr
        list = tlist
    }

    constructor(lr: LoginRequest?, p1: String?, p2: String?) {
        this.lr = lr
        param1 = p1
        param2 = p2
    }

    constructor(lr: LoginRequest?, p1: String?, p2: String?, t1: T?) : this(lr, p1, p2) {
        single = t1
    }

    constructor(lr: LoginRequest?, p1: String?, p2: String?, tlist: List<T>?) : this(lr, p1, p2) {
        list = tlist
    }
}