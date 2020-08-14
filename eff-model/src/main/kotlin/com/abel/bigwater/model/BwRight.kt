package com.abel.bigwater.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(value = ["updateBy", "createBy", "roleDesc", "updateDate", "createDate", "title"], ignoreUnknown = true)
class BwRight : BwObject {
    /**
     * @return the name
     */
    var name: String? = null
    /**
     * @return the desc
     */
    var rightDesc: String? = null

    /**
     * 1: 预置; 0 - 用户添加
     */
    var preInit: Int? = null

    /**
     * 显示在列表框
     */
    val title: String
        get() = "${name} - ${rightDesc}"


    /**
     * Default ctor.
     */
    constructor() {}

    /**
     * Constructs with name.
     *
     * @param name
     */
    constructor(name: String) {
        this.name = name
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (name == null) 0 else name!!.hashCode()
        return result
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    override fun equals(obj: Any?): Boolean {
        if (this === obj)
            return true
        if (obj == null)
            return false
        if (javaClass != obj.javaClass)
            return false
        val other = obj as BwRight?
        if (name == null) {
            if (other!!.name != null)
                return false
        } else if (name != other!!.name)
            return false
        return true
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    override fun toString(): String {
        return "BwRight [name=$name, rightDesc=$rightDesc]"
    }
}
