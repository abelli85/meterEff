package com.abel.bigwater.impl

import com.abel.bigwater.Helper
import com.abel.bigwater.api.*
import com.abel.bigwater.mapper.ConfigMapper
import com.abel.bigwater.mapper.UserMapper
import com.abel.bigwater.model.*
import com.alibaba.fastjson.JSON
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.apache.dubbo.rpc.RpcContext
import org.locationtech.jts.io.WKTWriter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.DigestUtils
import java.util.*

@Service("loginService")
open class UserServiceImpl : UserService {

    @Autowired
    private var userMapper: UserMapper? = null

    @Autowired
    private var loginManager: LoginManager? = null

    @Autowired
    private var configMapper: ConfigMapper? = null

    /**
     * 用户详情，仅供测试
     */
    override fun userInfo(id: Long): BwResult<BwUser> {
        return BwResult(BwUser().apply {
            this.userId = id.toString()
            userName = "测试${id}"
            firmId = "11"
            firmName = "水司演示"
        })
    }

    /**
     * 登录
     */
    override fun login(lr: LoginRequest): BwResult<BwUserLogin> {
        log.info("TRY to login with ${JSON.toJSONString(lr)}")

        val ctx = RpcContext.getContext()
        log.info("ctx: ${ctx.remoteHost}, ${ctx.remoteHostName}, ${ctx.remoteAddress}, ${ctx.remoteAddressString}, ${ctx.remotePort}, ${ctx.request}")

        val remoteHost = ctx.remoteHostName ?: "junit"
        val remoteAddr = ctx.remoteAddress?.address?.hostAddress ?: ctx.remoteHost ?: "junit"

        // 获取IP对应的地理位置
        val job = GlobalScope.async {
            GaodeWebapi.requestGeom(remoteAddr).also {
                it?.hostName = remoteHost
                it?.ipAddr = remoteAddr
            }
        }

        // session may not exist yet
        if (lr.userId.isNullOrBlank() || lr.devId.isNullOrBlank() || lr.timestamp.isNullOrBlank()
                || lr.clientHash.isNullOrBlank()) {
            return BwResult(2, LoginManager.INVALID_USER_PASS)
        }

        val rightName = UserService.BASE_PATH + "/login"

        val oper = BwUserOper().apply {
            userId = lr.userId
            operTime = Date()
            operCase = rightName
            operRight = rightName
            devId = lr.devId
            clientIp = remoteAddr
            serverIp = ctx.localAddressString
        }

        // compare hash.
        val user = userMapper?.selectUser(lr.userId!!)
        if (user == null) {
            log.warn("用户不存在: " + lr.userId!!)
            return BwResult(3, LoginManager.INVALID_USER_PASS)
        }

        // check right
        val rightList = userMapper?.userRightList(user.userId!!, rightName)
        if (rightList?.isEmpty() == true) {
            log.warn("用户权限不足: ${rightName}")
            return BwResult(5, CANNOT_LOGIN_WEB)
        }

        val _firm = configMapper!!.selectFirm(firmId = user.firmId).firstOrNull()

        // oper.firmId can't be null.
        oper.also {
            it.firmId = user.firmId
        }

        try {
            // check session firstly
            if (lr.sessionId?.isNotBlank() == true) {
                val login = userMapper!!.selectUserLogin(lr)
                return if (login == null) {
                    log.warn("会话失效")
                    oper.apply {
                        operResult = 6
                        operDesc = EXPIRED_SESSION
                    }
                    BwResult(6, EXPIRED_SESSION)
                } else {
                    // 操作轨迹
                    oper.apply {
                        firmId = login.firmId
                        operRole = login.roleList?.firstOrNull()?.name
                        operDesc = login.userId
                    }

                    // 等待地址
                    GlobalScope.async {
                        handleUserOper(login, job.await(), oper)
                    }

                    // verify session-id
                    if (DigestUtils.md5DigestAsHex((lr.sessionId + lr.timestamp + login.shareSalt).toByteArray())
                            != lr.clientHash) {
                        log.warn("会话哈希值校验错误")
                        oper.apply {
                            operResult = 4
                            operDesc = LoginManager.INVALID_SESSION
                        }
                        return BwResult(4, LoginManager.INVALID_SESSION)
                    }

                    // fill role-list & right-list
                    login.roleList = userMapper?.userRoleList(login.userId!!)
                    login.rightList = userMapper?.userRightList(login.userId!!, null)

                    // session-id passed.
                    oper.apply {
                        operResult = 0
                        operDesc = BwUserOper.OK
                    }

                    log.info("Session verified - ${login.userId}/${login.firmId}")
                    BwResult(login.apply { firm = _firm })
                }
            }

            val serverHash = DigestUtils.md5DigestAsHex((user.passHash!! + lr.timestamp!!).toByteArray())
            if (serverHash != lr.clientHash) {
                log.info("哈希值校验错误: client hash#${lr.clientHash} != serverHash#${serverHash}")
                oper.apply {
                    operResult = 4
                    operDesc = LoginManager.INVALID_SESSION
                }
                return BwResult(4, LoginManager.INVALID_USER_PASS)
            }

            // generate session id.
            val login = loginManager?.genSession(lr, remoteHost, remoteAddr)?.apply {
                smallIcon = user.smallIcon
                bigIcon = user.bigIcon
                signPic = user.signPic

                userName = user.userName
                loginHost = remoteHost
                loginIp = remoteAddr
            }

            // fill role-list & right-list
            login!!.roleList = userMapper?.userRoleList(lr.userId!!)
            login.rightList = userMapper?.userRightList(lr.userId!!, null)

            // fill firmId
            login.firmId = user.firmId
            login.firmName = user.firmName

            // 操作轨迹
            oper.apply {
                firmId = login.firmId
                operRole = login.roleList?.firstOrNull()?.name
                operResult = 0
                operDesc = BwUserOper.OK
            }

            // 等待地址
            GlobalScope.async {
                handleUserOper(login, job.await(), oper)
            }

            log.info("LOGIN successfully - ${login.userId}")
            return BwResult(login.apply {
                firm = _firm
            })
        } catch (ex: Exception) {
            log.error(ex.message, ex)
            return BwResult(1, "${ex.message}")
        } finally {
            oper.also {
                it.returnTime = Date()
            }
            userMapper!!.insertUserOper(oper)
        }
    }

    private fun handleUserOper(login: BwUserLogin, ipGeom: IpGeom?, oper: BwUserOper): BwUserOper {
        login.loginGeom = ipGeom

        login.loginIp = login.loginGeom?.ipAddr
        login.loginHost = login.loginGeom?.hostName
        login.loginAddr = "${login.loginGeom?.provinceStr()},${login.loginGeom?.cityStr()}"
        login.loginLoc = if (login.loginGeom?.plgon != null)
            WKTWriter().write(login.loginGeom?.plgon)
        else login.loginGeom?.rectangleStr()

        // 设置地址
        oper.loginIp = login.loginIp
        oper.loginHost = login.loginHost
        oper.loginAddr = login.loginAddr
        oper.loginLoc = login.loginLoc

        return oper
    }

    /**
     * 登出
     */
    override fun logout(ul: BwUserLogin): BwResult<BwUserLogin> {
        val oper = BwUserOper().apply {
            userId = ul.userId
            firmId = ul.firmId
            operTime = Date()
            operRight = UserService.BASE_PATH + "/login"
            operCase = UserService.BASE_PATH + "/login"
            operRole = UserRoles.ROLE_MOB_USER
        }

        return try {
            BwResult(0, if (loginManager!!.invalidSession(ul.sessionId!!) > 0) "会话成功退出" else "会话已退出")
        } catch (ex: Exception) {
            oper.apply {
                operResult = 1
                operDesc = ex.toString().take(200)
            }

            BwResult(1, "退出未清理: ${ex.message}")
        } finally {
            userMapper!!.insertUserOper(oper)
        }
    }

    /**
     * 修改本账号的口令
     */
    override fun changePassword(holder: BwHolder<PassParam>): BwResult<BwUser> {
        log.info("${holder.lr?.userId} try to change its own password: ${holder.single?.userId}")

        val up = holder.single
        if (up == null || up.userId.isNullOrBlank() || up.oldHash.isNullOrBlank() || up.newHash.isNullOrBlank()
                || up.oldHash == up.newHash) {
            return BwResult(3, "无效的用户信息")
        }

        val rightName = UserService.BASE_PATH + "/changePassword"
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single))
            if (login.code != 0) return BwResult(login.code, login.error!!)

            val user = userMapper?.selectUser(login.single?.userId!!)
            if (up.oldHash != user?.passHash) {
                user!!.passHash = up.newHash

                val cnt = userMapper?.updateUser(BwUser().apply {
                    userId = user.userId
                    passHash = up.newHash
                })

                if (cnt ?: -1 != 0) {
                    return BwResult(4, "修改口令失败: ${cnt}")
                } else {
                    loginManager?.invalidSession(holder.lr?.sessionId!!)
                    return BwResult(0, "修改口令成功，请重新登录")
                }
            }

            return BwResult(5, "原口令不正确，请重新尝试")
        } catch (ex: Exception) {
            log.error(ex.message, ex);
            return BwResult(1, ex.message!!);
        }
    }

    /** 获取制定用户的详情 */
    override fun userInfo(holder: BwHolder<BwUser>): BwResult<BwUser> {
        log.info("try to retrieve user info ${holder.single?.userId}")

        val rightName = UserService.BASE_PATH + "/info"
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single))
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            // refine firmId.
            var firmId = login.single!!.firmId
            if (Helper.TOP_FIRM_ID != firmId) {
                firmId = firmId + "%"
            } else firmId = "%"

            val userId = if (holder.single == null || holder.single!!.userId.isNullOrBlank())
                holder.single?.userId else login.single?.userId

            // retrieve user.
            val user = userMapper?.listUser(firmId, userId, null)?.firstOrNull() ?: null
            if (user == null) return BwResult(3, "获取用户详情失败: ${userId}")
            user.roles = userMapper?.userRoleList(user.userId!!)

            user.passHash = "DUMMY"
            user.firm = configMapper!!.selectFirm(firmId = user.firmId).firstOrNull()

            return BwResult<BwUser>(user)
        } catch (ex: Exception) {
            log.error(ex.message, ex);
            return BwResult(1, "获取用户详情失败: ${ex.message}");
        }
    }

    /**
     * 用户列表
     * holder#single represents userId string.
     */
    override fun userList(holder: BwHolder<String>): BwResult<BwUser> {
        log.info("try to list user: ${JSON.toJSONString(holder)}")

        val rightName = UserService.BASE_PATH + "/userList"
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single))
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            // refine firmId.
            var firmId = login.single!!.firmId
            if (Helper.TOP_FIRM_ID != firmId) {
                firmId = firmId + "%"
            } else firmId = "%"

            // retrieve user.
            val userList = userMapper?.listUser(firmId, if (holder.single.isNullOrBlank()) null else holder.single, null)
            userList?.forEach {
                it.passHash = "DUMMY"

                it.roles = userMapper?.userRoleList(it.userId!!)
            }

            return BwResult<BwUser>(userList!!)
        } catch (ex: Exception) {
            log.error(ex.message, ex);
            return BwResult(1, ex.message!!);
        }
    }

    /**
     * 创建用户
     */
    override fun createUser(holder: BwHolder<BwUser>): BwResult<BwUser> {
        log.info("${holder.lr?.userId} try to create user: ${JSON.toJSONString(holder.single)}")

        val user = holder.single
        if (user == null || user.userId.isNullOrBlank() || user.passHash.isNullOrBlank()) {
            return BwResult(3, "无效的用户信息")
        }

        val rightName = UserService.BASE_PATH + "/createUser"
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single))
            if (login.code != 0) return BwResult(login.code, login.error!!)

            // refine firmId of user from client.
            if (user.firmId.isNullOrBlank()
                    || (Helper.TOP_FIRM_ID != login.single?.firmId
                            && !user.firmId.orEmpty().startsWith(login.single!!.firmId!!))) {
                user.firmId = login.single!!.firmId!!
            }

            val cnt = userMapper?.insertUser(user)
            for (r1 in user.roles.orEmpty()) {
                userMapper?.insertUserRole(user.userId!!, r1.name!!)
            }

            return BwResult(user)
        } catch (ex: Exception) {
            log.error(ex.message, ex);
            return BwResult(1, ex.message!!);
        }
    }

    /**
     * 更改用户
     */
    override fun updateUser(holder: BwHolder<BwUser>): BwResult<BwUser> {
        log.info("${holder.lr?.userId} try to create user: ${holder.single?.userName} - ${holder.single?.userId}")

        if (holder.single?.userId.isNullOrBlank()) {
            return BwResult(3, "无效的用户信息")
        }
        val user = holder.single!!

        // 记录日志
        val rightName = UserService.BASE_PATH + "/updateUser"
        val oper = BwUserOper()
        oper.userId = holder.lr?.userId
        oper.operTime = Date()
        oper.operRight = rightName
        oper.operCase = rightName
        oper.operResult = 2

        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single))

            // 延迟提交操作日志
            oper.operDesc = login.single?.userId
            oper.firmId = login.single!!.firmId
            oper.operRole = login.single!!.roleList?.firstOrNull()?.name

            if (login.code != 0) return BwResult(login.code, login.error!!)

            // 先更新用户信息
            if (user.firmId.isNullOrBlank()
                    || (Helper.TOP_FIRM_ID != login.single?.firmId
                            && !user.firmId.orEmpty().startsWith(login.single!!.firmId!!))) {
                user.firmId = login.single!!.firmId!!
            }
            val cnt = userMapper?.updateUser(user)

            // 删除用户角色
            if (!user.roles.isNullOrEmpty()) {
                userMapper?.deleteUserRole(user.userId!!)
                user.roles.orEmpty().forEach {
                    userMapper?.insertUserRole(user.userId!!, it.name!!)
                }
            }

            // 操作轨迹
            oper.operResult = 0

            return BwResult(user)
        } catch (ex: Exception) {
            oper.operResult = 1

            log.error(ex.message, ex);
            return BwResult(1, ex.message!!);
        } finally {
            userMapper?.insertUserOper(oper)
        }
    }

    /**
     * 删除用户
     */
    override fun deleteUser(holder: BwHolder<BwUser>): BwResult<BwUser> {
        log.info("${holder.lr?.userId} try to delete user: ${holder.single?.userName} - ${holder.single?.userId}")

        if (holder.single?.userId.isNullOrBlank() || holder.single?.userId == USER_ADMIN) {
            return BwResult(2, "无法删除该用户: ${holder.single?.userId}.")
        }
        val user = holder.single!!

        // 记录日志
        val rightName = UserService.BASE_PATH + "/deleteUser"
        val oper = BwUserOper()
        oper.userId = holder.lr?.userId
        oper.operTime = Date()
        oper.operRight = rightName
        oper.operCase = rightName
        oper.operResult = 2

        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(user))

            // 延迟提交操作日志
            oper.operDesc = login.single?.userId
            oper.firmId = login.single!!.firmId
            oper.operRole = login.single!!.roleList?.firstOrNull()?.name

            if (login.code != 0) return BwResult(login.code, login.error!!)
            if (login.single?.userId == user.userId) {
                return BwResult(3, "用户不能删除自己: ${user.userId}")
            }

            // 先更新用户信息
            if (Helper.TOP_FIRM_ID != login.single?.firmId
                    && !user.firmId.orEmpty().startsWith(login.single!!.firmId!!)) {
                user.firmId = login.single!!.firmId!!.plus("%")
            }
            // 删除用户角色
            userMapper!!.deleteUserRole(user.userId!!)
            val cnt = userMapper!!.deleteUser(user)


            // 操作轨迹
            oper.operResult = 0

            return BwResult(user).apply {
                error = "已删除($cnt): ${user.userId}"
            }
        } catch (ex: Exception) {
            log.error(ex.message, ex);
            return BwResult(1, ex.message!!);
        } finally {
            userMapper?.insertUserOper(oper)
        }
    }

    /**
     * 权限列表
     */
    override fun rightList(): BwResult<BwRight> {
        val rlist = userMapper?.rightList()
        return BwResult(rlist!!)
    }

    /**
     * 角色列表
     */
    override fun roleList(): BwResult<BwRole> {
        val rlist = userMapper?.roleList()
        for (role in rlist.orEmpty()) {
            role.rights = userMapper?.roleRightList(role.name!!)
        }

        return BwResult(rlist!!)
    }

    /**
     * 角色详情
     */
    override fun selectRole(roleName: String): BwResult<BwRole> {
        val role = userMapper?.selectRole(roleName)
        if (role == null) {
            return BwResult(1, "角色不存在: ${roleName}")
        }

        val rlist = userMapper?.roleRightList(roleName)
        role.rights = rlist
        return BwResult(role)
    }

    /**
     * 创建角色
     */
    override fun createRole(holder: BwHolder<BwRole>): BwResult<BwRole> {
        log.info("${holder.lr?.userId} try to create role: ${JSON.toJSONString(holder.single)}")
        if (holder.lr == null || holder.single?.name.isNullOrBlank()) {
            return BwResult(2, "角色不能为空")
        }

        val rightName = UserService.BASE_PATH + "/createRole"
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            val role = holder.single!!
            val cnt = userMapper?.insertRole(role)
            if (cnt!! == 0) return BwResult(3, "创建角色失败: ${role}")
            for (r1 in role.rights ?: ArrayList<BwRight>()) {
                if (r1.name == null) {
                    return BwResult(2, "权限不能为空")
                }
                userMapper?.insertRoleRight(role.name!!, r1.name)
            }

            return BwResult(role)
        } catch (ex: Exception) {
            log.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message?.take(80)}")
        }
    }

    /**
     * 删除角色
     */
    override fun deleteRole(holder: BwHolder<BwRole>): BwResult<BwRole> {
        log.info("${holder.lr?.userId} try to delete role: ${JSON.toJSONString(holder.single)}")

        // 操作轨迹
        val rightName = UserService.BASE_PATH + "/deleteRole"
        val oper = BwUserOper()
        oper.userId = holder.lr?.userId
        oper.operTime = Date()
        oper.operCase = rightName
        oper.operRight = rightName
        oper.operResult = 2

        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));

            // 异步记录操作轨迹
            oper.operDesc = login.single?.userId
            oper.firmId = login.single?.firmId
            oper.operRole = login.single?.roleList?.firstOrNull()?.name

            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            val role = holder.single
            if (role == null || role.name == null) {
                return BwResult(2, "角色不能为空")
            }

            // verify if there're users
            val ulist = userMapper?.listUserByRole(role.name)
            if (ulist?.isNotEmpty() ?: false) {
                return BwResult(3, "角色被引用暂时无法删除")
            }

            userMapper?.deleteRoleRight(role.name!!)
            val cnt = userMapper?.deleteRole(role.name!!)

            return if (cnt!! > 0) {
                // 操作轨迹
                oper.operResult = 0

                BwResult(role)
            } else BwResult(4, "删除角色失败或不存在: ${cnt}")
        } catch (ex: Exception) {
            log.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        } finally {
            userMapper?.insertUserOper(oper)
        }
    }

    /**
     * 更改角色
     */
    override fun updateRole(holder: BwHolder<BwRole>): BwResult<BwRole> {
        log.info("${holder.lr?.userId} try to update role: ${JSON.toJSONString(holder.single)}")
        if (holder.lr == null || holder.single == null) {
            return BwResult(2, "角色不能为空")
        }

        val rightName = UserService.BASE_PATH + "/updateRole"
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            // update role
            val role = holder.single!!
            val cnt = userMapper?.updateRole(role)
            if (cnt!! == 0) return BwResult(3, "更改角色不存在: ${role.name}")

            // update rights
            userMapper?.deleteRoleRight(role.name!!)

            for (r1 in role.rights!!) {
                userMapper?.insertRoleRight(role.name!!, r1.name)
            }

            return BwResult(role)
        } catch (ex: Exception) {
            log.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 获取水司列表
     */
    override fun firmList(holder: BwHolder<String>): BwResult<BwFirm> {
        log.info("${holder.lr?.userId} try to list firm: ${JSON.toJSONString(holder.single)}")

        val rightName = UserService.BASE_PATH + "/firmList"
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            val firmId = if (Helper.TOP_FIRM_ID == login.single?.firmId) "%" else (login.single?.firmId + "%")
            return BwResult(configMapper?.selectFirm(firmId)!!)
        } catch (ex: Exception) {
            log.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 增加机构信息
     */
    override fun addFirm(holder: BwHolder<BwFirm>): BwResult<BwFirm> {
        if (holder.single?.id.isNullOrBlank()
                || holder.single?.name.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        log.info("${holder.lr?.userId} try to add firm: ${JSON.toJSONString(holder.single)}")

        val rightName = UserService.BASE_PATH + UserService.PATH_ADD_FIRM
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            // 只能更新 所在机构及分公司
            if (!holder.single!!.id!!.startsWith(login.single!!.firmId!!)) {
                return BwResult(3, WARN_NO_RIGHT + holder.single?.id)
            }

            val cnt = configMapper!!.addFirm(holder.single!!)
            return BwResult(holder.single!!).apply {
                error = "增加机构： $cnt"
            }
        } catch (ex: Exception) {
            log.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 更新机构信息
     */
    override fun updateFirm(holder: BwHolder<BwFirm>): BwResult<BwFirm> {
        if (holder.single?.id.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        log.info("${holder.lr?.userId} try to update firm: ${JSON.toJSONString(holder.single)}")

        val rightName = UserService.BASE_PATH + UserService.PATH_UPDATE_FIRM
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            // 只能更新 所在机构及分公司
            if (!holder.single!!.id!!.startsWith(login.single!!.firmId!!)) {
                return BwResult(3, WARN_NO_RIGHT + holder.single?.id)
            }

            val cnt = configMapper!!.updateFirm(holder.single!!)
            return BwResult(holder.single!!).apply {
                error = "更新机构： $cnt"
            }
        } catch (ex: Exception) {
            log.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 删除机构信息
     */
    override fun deleteFirm(holder: BwHolder<BwFirm>): BwResult<BwFirm> {
        if (holder.single?.id.isNullOrBlank()) {
            return BwResult(2, ERR_PARAM)
        }

        log.info("${holder.lr?.userId} try to delete firm: ${JSON.toJSONString(holder.single)}")

        val rightName = UserService.BASE_PATH + UserService.PATH_DELETE_FIRM
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            // 只能更新 所在机构及分公司
            if (!holder.single!!.id!!.startsWith(login.single!!.firmId!!)) {
                return BwResult(3, WARN_NO_RIGHT + holder.single?.id)
            }

            val cnt = configMapper!!.deleteFirm(holder.single!!.id!!)
            return if (cnt == 1) BwResult(holder.single!!).apply {
                error = "删除机构： $cnt"
            } else BwResult(4, "预置机构不允许删除: ".plus(holder.single?.id))
        } catch (ex: Exception) {
            log.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 获取用户操作列表
     */
    override fun operList(holder: BwHolder<UserOperParam>): BwResult<BwUserOper> {
        log.info("${holder.lr?.userId} try to list oper: ${JSON.toJSONString(holder.single)}")
        if (holder.single == null) return BwResult(2, "参数不能为空")

        val rightName = UserService.BASE_PATH + "/operList"
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            return BwResult(userMapper?.listUserOper(holder.single!!)!!)
        } catch (ex: Exception) {
            log.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 获取用户操作统计结果
     */
    override fun operStat(holder: BwHolder<UserOperParam>): BwResult<BwUserOper> {
        log.info("${holder.lr?.userId} try to stat oper: ${JSON.toJSONString(holder.single)}")
        if (holder.single == null) return BwResult(2, "参数不能为空")

        val rightName = UserService.BASE_PATH + "/operStat"
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            return BwResult(userMapper?.statUserOper(holder.single!!)!!)
        } catch (ex: Exception) {
            log.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 获取当前登录会话
     */
    override fun loginList(holder: BwHolder<UserOperParam>): BwResult<BwUserLogin> {
        log.info("list login: ${JSON.toJSONString(holder)}")
        if (holder.single == null || holder.single!!.firmId.isNullOrBlank()) {
            return BwResult(2, "参数不完整")
        }

        val rightName = UserService.BASE_PATH + "/loginList"
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            val firmId = Helper.refineFirmId(holder.single?.firmId, login.single?.firmId!!)
            return BwResult(userMapper!!.listUserLogin(firmId!!, holder.single?.userId,
                    holder.single?.operTime1, holder.single?.operTime2,
                    holder.single?.sessionId))
        } catch (ex: Exception) {
            log.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    /**
     * 踢出某个会话
     */
    override fun kickLogin(holder: BwHolder<UserOperParam>): BwResult<BwUserLogin> {
        log.info("list login: ${JSON.toJSONString(holder)}")
        if (holder.single == null || holder.single!!.firmId.isNullOrBlank() || holder.single!!.userId.isNullOrBlank()) {
            return BwResult(2, "参数不完整")
        }

        val rightName = UserService.BASE_PATH + "/kickLogin"
        try {
            val login = loginManager!!.verifySession(holder.lr!!, rightName, rightName, JSON.toJSONString(holder.single));
            if (login.code != 0) {
                return BwResult(login.code, login.error!!)
            }

            val firmId = Helper.refineFirmId(holder.single?.firmId, login.single?.firmId!!)
            val cnt = userMapper!!.kickUserLogin(firmId!!, holder.single!!.userId!!,
                    holder.single?.operTime1, holder.single?.operTime2,
                    holder.single?.sessionId)
            return BwResult(0, "踢出会话数量:$cnt")
        } catch (ex: Exception) {
            log.error(ex.message, ex);
            return BwResult(1, "内部错误: ${ex.message}")
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(UserServiceImpl::class.java)

        const val ERR_PARAM = "参数不完整"
        const val ERR_INTERNAL = "内部错误:"

        const val EXPIRED_SESSION = "会话已失效，请注销后重新登录"

        const val WARN_NO_RIGHT = "无权执行该操作: "

        const val CANNOT_LOGIN_WEB = "抱歉，您不具有登录网页系统的权限！"

        /**
         * super user, can't be deleted.
         */
        const val USER_ADMIN = "admin"

        const val KEY_VALUE = "value"

        const val KEY_USER_ID = "userId"
    }
}