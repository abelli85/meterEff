package com.abel.bigwater

import com.abel.bigwater.api.BwResult
import com.abel.bigwater.api.LoginRequest
import com.abel.bigwater.api.UserService
import com.abel.bigwater.model.BwUserLogin
import com.alibaba.fastjson.JSON
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.junit.Assert
import org.slf4j.LoggerFactory
import org.springframework.util.DigestUtils

object TestHelper {
    private val lgr = LoggerFactory.getLogger(TestHelper::class.java)

    fun buildLoginRequest(sess: BwUserLogin): LoginRequest {
        return LoginRequest().apply {
            userId = sess.userId
            devId = sess.devId
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            sessionId = sess.sessionId
            clientHash = DigestUtils.md5DigestAsHex((sess.sessionId + timestamp + sess.shareSalt).toByteArray())
        }
    }

    /**
     * 登录并返回结果
     */
    fun login(bean: UserService?, _userId: String = "abel", _pass: String = "test", blog: Boolean = false): BwResult<BwUserLogin> {
        val passHash = DigestUtils.md5DigestAsHex(_pass.toByteArray())
        val ul = bean!!.login(LoginRequest().apply {
            userId = _userId
            devId = "junit"
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            clientHash = DigestUtils.md5DigestAsHex((passHash + timestamp).toByteArray())
        })

        lgr.info("login first: {}", if (blog) JSON.toJSONString(ul, true) else ul.error)
        Assert.assertTrue(ul.code == 0)
        return ul
    }
}