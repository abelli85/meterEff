/**
 *
 */
package com.abel.bigwater.impl

import com.abel.bigwater.model.BwRight
import com.abel.bigwater.model.BwUserLogin
import com.abel.bigwater.model.BwUserOper
import com.abel.bigwater.model.JsonHelper
import com.abel.bigwater.mapper.UserMapper
import com.abel.bigwater.api.BwResult
import com.abel.bigwater.api.LoginRequest
import org.apache.dubbo.rpc.RpcContext
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.Executors

/**
 * @author Abel5
 */
@Component
open class LoginManager {

    @Autowired
    private var userMapper: UserMapper? = null

    private val poolCity = Executors.newSingleThreadExecutor()

    @Synchronized
    fun genSession(lr: LoginRequest, remoteHost: String, ipAddr: String): BwUserLogin {
        val login = BwUserLogin()
        login.sessionId = UUID.randomUUID().toString()

        login.userId = lr.userId
        login.loginTime = Date()
        login.devId = lr.devId

        login.ipAddr = remoteHost
        login.loginIp = ipAddr
        login.loginLoc = remoteHost

        // simple share-salt.
        login.shareSalt = UUID.randomUUID().toString().takeLast(Random().nextInt(16))

        userMapper!!.insertUserLogin(login)

        return login
    }

    @Synchronized
    fun invalidSession(sid: String): Int {
        userMapper!!.deleteUserLoginObsolete()
        return userMapper!!.deleteUserLogin(sid)
    }

    /**
     * Try to verify the session. UserId will be stored in
     * [BwResult.single] if passed verification.
     *
     * @param lr  id.
     * @param rightName  right name to be verified. can be null which means no required
     * right.
     * @return non-null value, valid only if code == 0, userId is stored in
     * result#error.
     */
    fun verifySession(lr: LoginRequest, rightName: String?): BwResult<BwUserLogin> {
        // no session yet.
        if (lr.sessionId.isNullOrBlank() || lr.userId.isNullOrBlank() || lr.devId.isNullOrBlank()) {
            return BwResult(2, INVALID_SESSION)
        }

        // check if exists.
        val ulogin = userMapper!!.selectUserLogin(lr) ?: return BwResult(3, INVALID_SESSION)

        val uo = BwUserOper().apply {
            userId = ulogin.userId
            firmId = ulogin.firmId
            operTime = Date()
            operResult = 0
            operDesc = BwUserOper.OK
            operRight = rightName
            operCase = rightName
        }

        // verify right.
        return authUserOper(rightName, ulogin, uo)
    }

    /**
     * Try to verify the session. UserId will be stored in
     * [BwResult.single] if passed verification.
     *
     * @param lr  id.
     * @param rightName  right name to be verified. can be null which means no required
     * right.
     * @return non-null value, valid only if code == 0, userId is stored in
     * result#error.
     */
    fun verifySession(
            lr: LoginRequest,
            rightName: String?,
            caseName: String,
            content: String?): BwResult<BwUserLogin> {
        // no session yet.
        if (lr.sessionId.isNullOrBlank() || lr.userId.isNullOrBlank() || lr.devId.isNullOrBlank()) {
            return BwResult(2, INVALID_SESSION)
        }

        // check if exists.
        val ulogin = userMapper!!.selectUserLogin(lr) ?: return BwResult(3, INVALID_SESSION)

        val ctx = RpcContext.getContext()
        val uo = BwUserOper().apply {
            userId = ulogin.userId
            firmId = ulogin.firmId
            operTime = Date()
            operResult = 0
            operDesc = BwUserOper.OK
            clientIp = ctx.remoteAddressString
            serverIp = ctx.localAddressString
            operRight = rightName
            operCase = caseName
            operDesc = content
            operDesc = content?.take(260)
            devId = (ctx.get("User-Agent") as String?)?.take(32)
        }

        // verify right.
        return authUserOper(rightName, ulogin, uo)
    }

    private fun authUserOper(rightName: String?, ulogin: BwUserLogin, uo: BwUserOper): BwResult<BwUserLogin> {
        if (rightName != null) {
            val rightList = userMapper!!.userRightList(ulogin.userId!!, rightName)
            val right = BwRight(rightName)
            if (!rightList.contains(right)) {
                uo.apply {
                    operResult = CODE_INVALID_AUTHORIZE
                    operDesc = INVALID_AUTHORIZE
                }

                userMapper!!.insertUserOper(uo)
                fillOperCity(uo)

                return BwResult(CODE_INVALID_AUTHORIZE, INVALID_AUTHORIZE)
            }
        }

        userMapper!!.insertUserOper(uo)
        fillOperCity(uo)

        // return valid login.
        lgr.info("Login OK: {}", ulogin)
        return BwResult(ulogin)
    }

    /**
     * 异步填充登录城市
     *
     * @param userMapper
     * @param uo
     */
    private fun fillOperCity(uo: BwUserOper) {
        poolCity.execute {
            if (uo.clientIp == null || uo.clientIp!!.trim { it <= ' ' }.isEmpty()) return@execute

            try {
                uo.operCity = JsonHelper.getCity(uo.clientIp)
                userMapper!!.updateUserOper(uo)
            } catch (ex: Exception) {
                val msg = "fail to getIpInfo for ${uo.clientIp}: ${ex.message}"
                lgr.error(msg)
                lgr.debug(msg, ex)
            }
        }
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(LoginManager::class.java)

        const val TOP_FIRM_ID = "1"
        const val KEY_FIRM_ID = "firmId"
        const val KEY_SESSION_ID = "sessionId"

        const val CODE_INVALID_AUTHORIZE = 4

        const val INVALID_USER_PASS = "无效的用户名/口令"

        const val INVALID_SESSION = "会话失效，请重新登录"

        const val INVALID_AUTHORIZE = "无权执行操作"

        const val INTERNAL_ERROR = "内部错误"
    }
}
