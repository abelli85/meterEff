package com.abel.bigwater.mapper

import com.abel.bigwater.model.*
import com.abel.bigwater.api.LoginRequest
import com.abel.bigwater.api.UserOperParam
import org.apache.ibatis.annotations.*
import java.util.*

@Mapper
interface UserMapper {
    /**
     * This method is called by user-manager use-case.
     */
    fun listUser(@Param("firmId") firmId: String,
                 @Param("idStr") idStr: String?,
                 @Param("nameStr") nameStr: String?): List<BwUser>

    fun listUserByRole(@Param("roleName") roleName: String?): List<BwUser>

    /**
     * This method is called by user-login use-case.
     */
    fun selectUser(@Param("userId") userId: String): BwUser

    /**
     * insertUser commits to the database.
     */
    fun insertUser(user: BwUser): Int

    fun deleteUser(user: BwUser): Int

    fun updateUser(user: BwUser): Int

    /**
     * User#id, emailValid, emailToken, userToken may be set.
     *
     * @param user
     * -
     */
    fun updateUserToken(user: BwUser): Int

    /**
     * for user-login use-case.
     */
    fun insertUserLogin(userLogin: BwUserLogin): Int

    fun deleteUserLogin(@Param("sid") sid: String): Int

    fun deleteUserLoginObsolete(): Int

    fun selectUserLogin(lr: LoginRequest): BwUserLogin?

    fun listUserLogin(@Param("firmId") firmId: String,
                      @Param("userId") userId: String? = null,
                      @Param("startTime") startTime: Date? = null,
                      @Param("endTime") endTime: Date? = null,
                      @Param("sessionId") sessionId: String? = null): List<BwUserLogin>

    fun kickUserLogin(@Param("firmId") firmId: String,
                      @Param("userId") userId: String,
                      @Param("startTime") startTime: Date? = null,
                      @Param("endTime") endTime: Date? = null,
                      @Param("sessionId") sessionId: String? = null): Int

    fun rightList(): List<BwRight>

    fun roleList(): List<BwRole>

    fun userRoleList(@Param("userId") userId: String): List<BwRole>

    fun userRightList(@Param("userId") userId: String,
                      @Param("rightName") rightName: String?): List<BwRight>

    fun selectRole(name: String): BwRole

    fun roleRightList(@Param("roleName") roleName: String): List<BwRight>

    fun insertRole(role: BwRole): Int

    /**
     * <pre>
     * <parameterMap type="map" id="roleRight">
     * <parameter property="roleName"></parameter>
     * <parameter property="rightName"></parameter>
    </parameterMap> *
    </pre> *
     *
     * @param map
     * - roleName, rightName
     */
    fun insertRoleRight(@Param("roleName") roleName: String,
                        @Param("rightName") rightName: String?): Int

    /**
     * <pre>
     * <parameterMap type="map" id="userRole">
     * <parameter property="userId"></parameter>
     * <parameter property="roleName"></parameter>
    </parameterMap> *
    </pre> *
     *
     * @param map
     * - userId, roleName
     */
    fun insertUserRole(@Param("userId") userId: String,
                       @Param("roleName") roleName: String): Int

    fun updateRole(role: BwRole): Int

    fun deleteRole(@Param("roleName") roleName: String): Int

    fun deleteRoleRight(@Param("roleName") roleName: String): Int

    fun deleteUserRole(@Param("userId") userId: String): Int

    /**
     * 异步更新登录地区
     */
    fun updateLoginCity(login: BwUserLogin): Int

    /**
     * 插入操作记录
     */
    fun insertUserOper(uo: BwUserOper): Int

    /**
     * 填充日志字段，如登录地区
     */
    fun updateUserOper(uo: BwUserOper): Int

    fun listUserOper(param: UserOperParam): List<BwUserOper>

    fun statUserOper(param: UserOperParam): List<BwUserOper>
}