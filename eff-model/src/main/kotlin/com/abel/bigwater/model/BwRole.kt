package com.abel.bigwater.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(value = ["updateBy", "createBy", "updateDate", "createDate", "title"], ignoreUnknown = true)
class BwRole : BwObject() {
    /**
     * @return the name
     */
    var name: String? = null
    /**
     * @return the desc
     */
    var roleDesc: String? = null
    /**
     * @return the rights
     */
    var rights: List<BwRight>? = null

    /**
     * 1: 预置; 0 - 用户添加
     */
    var preInit: Int? = null

    /**
     * 显示在列表框
     */
    val title: String
        get() = "${name} - ${roleDesc}"

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    override fun toString(): String {
        return ("BwRole [name=" + name + ", desc=" + roleDesc + ", rights="
                + rights + "]")
    }

}
