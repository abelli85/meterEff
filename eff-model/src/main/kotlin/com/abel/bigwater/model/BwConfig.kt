package com.abel.bigwater.model

class BwConfig : BwBase, Cloneable {
    /**
     * @return the groupId
     */
    var groupId: String? = null
    /**
     * @return the configId
     */
    var configId: String? = null
    /**
     * @return the value
     */
    var value: String? = null

    /**
     * @return the configName
     */
    var configName: String? = null

    /**
     * @return the configDesc
     */
    var configDesc: String? = null

    /**
     * @return the configType
     */
    var configType: String? = null

    /**
     * @return the firmId
     */
    var firmId: String? = null

    /**
     * @return the firmName
     */
    var firmName: String? = null

    /**
     * @param configId
     */
    constructor() {}

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    override fun toString(): String {
        return String.format(
                "{groupId:%s, configId:%s, value:%s, configName:%s, configDesc:%s, configType:%s, firmId:%s, firmName:%s}",
                groupId, configId, value, configName, configDesc, configType, firmId, firmName)
    }

    override fun hashCode(): Int {
        return configId.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return (other as BwConfig?)?.configId == this.configId
    }

    /**
     * @param configId
     */
    constructor(configId: String) {
        this.configId = configId
    }

    /**
     * @param configId
     * @param firmId
     */
    constructor(configId: String, firmId: String) {
        this.configId = configId
        this.firmId = firmId
    }

    /**
     * Try to parse {[.value] as integer. Returns defalutValue if fail to parse.
     *
     * @param defaultValue
     * @return
     */
    fun intValue(defaultValue: Int): Int {
        try {
            return Integer.parseInt(this.value!!)
        } catch (ex: Exception) {
            return defaultValue
        }

    }

    public override fun clone(): BwConfig {
        return super.clone() as BwConfig
    }
}
