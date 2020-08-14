package com.abel.bigwater

import com.abel.bigwater.model.BwUserLogin
import com.abel.bigwater.api.LoginRequest
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.springframework.util.DigestUtils

object TestHelper {
    
    fun buildLoginRequest(sess: BwUserLogin): LoginRequest {
        return LoginRequest().apply {
            userId = sess.userId
            devId = sess.devId
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            sessionId = sess.id
            clientHash = DigestUtils.md5DigestAsHex((sess.id + timestamp + sess.shareSalt).toByteArray())
        }
    }
}