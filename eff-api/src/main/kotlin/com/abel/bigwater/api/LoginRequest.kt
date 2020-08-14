package com.abel.bigwater.api

class LoginRequest {
    /**
     * @return the userId
     */
    var userId: String? = null

    /**
     * @return the timestamp
     */
    var timestamp: String? = null

    /**
     * @return the clientHash
     */
    var clientHash: String? = null

    /**
     * @return the devId
     */
    var devId: String? = null

    /**
     * 用户登录后得到的门卡
     * @return the sessionId
     */
    var sessionId: String? = null
}
