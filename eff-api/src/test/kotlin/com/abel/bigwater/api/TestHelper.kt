package com.abel.bigwater.api

import com.abel.bigwater.model.BwUserLogin
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.springframework.util.DigestUtils

object TestHelper {
    const val URL_BASE = "http://localhost:8899/meff"
    const val URL_LOGIN = "$URL_BASE/user/login"
    class BwUserLoginResult : BwResult<BwUserLogin>()

    fun buildLoginRequest(ul: BwUserLogin): LoginRequest {
        return LoginRequest().apply {
            userId = ul.userId
            devId = ul.devId
            sessionId = ul.sessionId
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            clientHash = DigestUtils.md5DigestAsHex((ul.sessionId + timestamp + ul.shareSalt).toByteArray())
        }
    }
}