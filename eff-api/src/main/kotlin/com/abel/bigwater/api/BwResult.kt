package com.abel.bigwater.api

import java.io.Serializable

open class BwResult<T : Serializable> : Serializable {
    companion object {
        /** 服务版本，请每次发布时更新 */
        val SERVICE_VERSION = "v20191128.1"
    }

    /**
     * @return the code
     */
    var code: Int = 0

    /**
     * @return the error
     */
    var error: String? = null

    /** 服务版本，每次发布时更新 */
    val version: String = SERVICE_VERSION

    /**
     * @return the object
     */
    var single: T? = null

    /**
     * @return the list
     */
    var list: List<T>? = null

    /**
     *
     */
    constructor() {}

    /**
     * @param code
     * @param error
     */
    constructor(code: Int, error: String) {
        this.code = code
        this.error = error
    }

    /**
     * @param code
     * @param error
     */
    constructor(code: Int, error: String, `object`: T) {
        this.code = code
        this.error = error
        this.single = `object`
    }

    /**
     * @param code
     * @param error
     */
    constructor(code: Int, error: String, list: List<T>) {
        this.code = code
        this.error = error
        this.list = list
    }

    /**
     * @param object
     */
    constructor(`object`: T) {
        this.single = `object`
    }

    /**
     * @param list
     */
    constructor(list: List<T>) {
        this.list = list
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    override fun toString(): String {
        return ("BwResult [code=" + code + ", error=" + error + ", single="
                + single + ", list=" + list + "]")
    }
}
