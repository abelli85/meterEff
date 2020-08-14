package com.abel.bigwater.api

import com.abel.bigwater.model.*
import io.swagger.annotations.ApiOperation
import org.apache.dubbo.rpc.protocol.rest.support.ContentType
import javax.ws.rs.*

/**
 *
 */
@Path("user")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8)
@ApiOperation("loginService")
interface UserService {

    companion object {
        val BASE_PATH = "/user"

        const val PATH_UPDATE_FIRM = "/updateFirm"
        const val PATH_ADD_FIRM = "/addFirm"
        const val PATH_DELETE_FIRM = "/deleteFirm"

        /**
         * 获取计量标准列表
         */
        const val PATH_LIST_STD_AUTH = "/listStdAuth"
        /**
         * 插入计量标准
         */
        const val PATH_ADD_STD_AUTH = "/addStdAuth"
        /**
         * 删除计量标准
         */
        const val PATH_DELETE_STD_AUTH = "/deleteStdAuth"
    }

    /**
     * 用户详情，仅供测试
     */
    @GET
    @ApiOperation("userInfo")
    @Path("{id:\\d+}")
    fun userInfo(@PathParam("id") id: Long): BwResult<BwUser>

    /**
     * 登录
     */
    @POST
    @Path("login")
    @ApiOperation("login")
    fun login(lr: LoginRequest): BwResult<BwUserLogin>

    /**
     * 登出
     */
    @POST
    @Path("logout")
    @ApiOperation("logout")
    fun logout(ul: BwUserLogin): BwResult<BwUserLogin>

    /**
     * 修改本账号的口令
     */
    @POST
    @Path("changePassword")
    fun changePassword(holder: BwHolder<PassParam>): BwResult<BwUser>

    /** 获取制定用户的详情 */
    @POST
    @Path("info")
    @ApiOperation("info")
    fun userInfo(holder: BwHolder<BwUser>): BwResult<BwUser>

    /**
     * 用户列表
     * holder#single represents userId string.
     */
    @POST
    @Path("userList")
    fun userList(holder: BwHolder<String>): BwResult<BwUser>

    /**
     * 创建用户
     */
    @POST
    @Path("createUser")
    fun createUser(holder: BwHolder<BwUser>): BwResult<BwUser>

    /**
     * 更改用户
     */
    @POST
    @Path("updateUser")
    fun updateUser(holder: BwHolder<BwUser>): BwResult<BwUser>

    /**
     * 删除用户
     */
    @POST
    @Path("deleteUser")
    fun deleteUser(holder: BwHolder<BwUser>): BwResult<BwUser>

    /**
     * 权限列表
     */
    @GET
    @Path("rightList")
    fun rightList(): BwResult<BwRight>

    /**
     * 角色列表
     */
    @GET
    @Path("roleList")
    fun roleList(): BwResult<BwRole>

    /**
     * 角色详情
     */
    @GET
    @Path("role/{roleName}")
    fun selectRole(@PathParam("roleName") roleName: String): BwResult<BwRole>

    /**
     * 创建角色
     */
    @POST
    @Path("createRole")
    fun createRole(holder: BwHolder<BwRole>): BwResult<BwRole>

    /**
     * 删除角色
     */
    @POST
    @Path("deleteRole")
    fun deleteRole(holder: BwHolder<BwRole>): BwResult<BwRole>

    /**
     * 更改角色
     */
    @POST
    @Path("updateRole")
    fun updateRole(holder: BwHolder<BwRole>): BwResult<BwRole>

    /**
     * 获取水司列表
     */
    @POST
    @Path("firmList")
    fun firmList(holder: BwHolder<String>): BwResult<BwFirm>

    /**
     * 增加机构信息
     */
    @POST
    @Path(PATH_ADD_FIRM)
    fun addFirm(holder: BwHolder<BwFirm>): BwResult<BwFirm>

    /**
     * 更新机构信息
     */
    @POST
    @Path(PATH_UPDATE_FIRM)
    fun updateFirm(holder: BwHolder<BwFirm>): BwResult<BwFirm>

    /**
     * 删除机构信息
     */
    @POST
    @Path(PATH_DELETE_FIRM)
    fun deleteFirm(holder: BwHolder<BwFirm>): BwResult<BwFirm>

    /**
     * 获取用户操作列表
     */
    @POST
    @Path("operList")
    fun operList(holder: BwHolder<UserOperParam>): BwResult<BwUserOper>

    /**
     * 获取用户操作统计结果
     */
    @POST
    @Path("operStat")
    fun operStat(holder: BwHolder<UserOperParam>): BwResult<BwUserOper>

    /**
     * 获取当前登录会话
     */
    @POST
    @Path("loginList")
    fun loginList(holder: BwHolder<UserOperParam>): BwResult<BwUserLogin>

    /**
     * 踢出某个会话
     */
    @POST
    @Path("kickLogin")
    fun kickLogin(holder: BwHolder<UserOperParam>): BwResult<BwUserLogin>
}