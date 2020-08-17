package com.abel.bigwater.impl

import com.abel.bigwater.TestHelper
import com.abel.bigwater.TestHelper.buildLoginRequest
import com.abel.bigwater.api.BwHolder
import com.abel.bigwater.api.LoginRequest
import com.abel.bigwater.api.UserOperParam
import com.abel.bigwater.api.UserService
import com.abel.bigwater.mapper.ConfigMapper
import com.abel.bigwater.mapper.UserMapper
import com.abel.bigwater.model.BwFirm
import com.abel.bigwater.model.BwRole
import com.abel.bigwater.model.BwUser
import com.abel.bigwater.model.BwUserLogin
import com.alibaba.fastjson.JSON
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.dubbo.rpc.RpcContext
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import org.junit.Assert.*
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.mybatis.spring.annotation.MapperScan
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.util.DigestUtils

/**
 * Launch zookeeper before the tests.
 */
@ContextConfiguration(locations = ["classpath:/spring/rest-provider.xml", "classpath:/spring-mybatis.xml"])
@RunWith(SpringJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.DEFAULT)
@MapperScan(basePackages = ["com.abel.bigwater.mapper"])
class UserServiceImplTest {

    @Autowired
    private var bean: UserService? = null

    @Autowired
    private var lm: LoginManager? = null

    @Autowired
    private var userMapper: UserMapper? = null

    @Autowired
    private var configMapper: ConfigMapper? = null

    /**
     * 登录并返回结果
     */
    private fun login(_userId: String = "abel", _pass: String = "test", blog: Boolean = false): BwUserLogin {
        val passHash = DigestUtils.md5DigestAsHex(_pass.toByteArray())
        val ul = bean?.login(LoginRequest().apply {
            userId = _userId
            devId = "junit"
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            clientHash = DigestUtils.md5DigestAsHex((passHash + timestamp).toByteArray())
        })

        lgr.info("login first: {}", if (blog) JSON.toJSONString(ul, true) else ul?.error)
        assertTrue(ul?.code == 0)
        return ul!!.single!!
    }

    @Test
    fun userInfo() {
        lgr.info("demo test for user-info")
        val user = bean?.userInfo(2)
        lgr.info(JSON.toJSONString(user, true))
    }

    @Test
    fun loginFirst() {
        val passHash = DigestUtils.md5DigestAsHex("test".toByteArray())
        val ul = bean?.login(LoginRequest().apply {
            userId = "abel"
            devId = "junit"
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            clientHash = DigestUtils.md5DigestAsHex((passHash + timestamp).toByteArray())
        })

        lgr.info("login first: {}", JSON.toJSONString(ul, true))
        assertTrue(ul?.code == 0)
    }

    @Test
    fun loginSecond() {
        val ctx = RpcContext.getContext()
        val sess = lm!!.genSession(LoginRequest().apply {
            userId = "abel"
            devId = "junit"
        }, ctx.remoteAddressString, ctx.localAddressString)

        val ul = bean?.login(LoginRequest().apply {
            userId = "abel"
            devId = "junit"
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            sessionId = sess.sessionId
            clientHash = DigestUtils.md5DigestAsHex((sess.sessionId + timestamp + sess.shareSalt).toByteArray())
        })

        lgr.info("login second: {}", JSON.toJSONString(ul, true))
        assertTrue(ul?.code == 0)
    }

    @Test
    fun loginInvalid() {
        val ul = bean?.login(LoginRequest())
        lgr.info("login with invalid: {}", JSON.toJSONString(ul, true))
        assertTrue(ul?.code == 2)
    }

    @Test
    fun loginNoUser() {
        val ul = bean?.login(LoginRequest().apply {
            userId = "no-user"
            devId = "junit"
            timestamp = "dummy-timestamp"
            clientHash = "dummy-hash"
        })

        lgr.info("login with invalid: {}", JSON.toJSONString(ul, true))
        assertTrue(ul?.code == 3)
    }

    @Test
    fun loginNoRight() {
        val ul = bean?.login(LoginRequest().apply {
            userId = "scott"
            devId = "junit"
            timestamp = "dummy-timestamp"
            clientHash = "dummy-hash"
            sessionId = "dummy-session"
        })

        lgr.info("login without right: {}", JSON.toJSONString(ul, true))
        assertTrue(ul?.code == 5)
    }

    @Test
    fun loginExpired() {
        val ul = bean?.login(LoginRequest().apply {
            userId = "abel"
            devId = "junit"
            timestamp = "dummy-timestamp"
            clientHash = "dummy-hash"
            sessionId = "dummy-session"
        })

        lgr.info("login with expired: {}", JSON.toJSONString(ul, true))
        assertTrue(ul?.code == 6)
    }

    @Test
    fun loginInvalidSession() {
        val ctx = RpcContext.getContext()
        val ul = lm!!.genSession(LoginRequest().apply {
            userId = "abel"
            devId = "junit"
        }, ctx.remoteAddressString, ctx.localAddressString)

        val ret = bean?.login(LoginRequest().apply {
            userId = "abel"
            devId = "junit"
            timestamp = "dummy-timestamp"
            clientHash = "dummy-hash"
            sessionId = ul.sessionId
        })

        lgr.info("login with invalid session: {}", JSON.toJSONString(ret, true))
        assertTrue(ret?.code == 4)
    }

    @Test
    fun logout() {
        val passHash = DigestUtils.md5DigestAsHex("test".toByteArray())
        val ul = bean?.login(LoginRequest().apply {
            userId = "abel"
            devId = "junit"
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            clientHash = DigestUtils.md5DigestAsHex((passHash + timestamp).toByteArray())
        })

        assertTrue(ul?.code == 0)

        val rl2 = bean?.logout(BwUserLogin().apply {
            sessionId = ul?.single?.sessionId
            userId = ul?.single?.userId
            firmId = ul?.single?.firmId
        })

        lgr.info("logout: {}", JSON.toJSONString(rl2))
        assertEquals(0, rl2?.code)
    }

    @Test
    fun changePassword() {
    }

    @Test
    fun testUserInfo() {
        val passHash = DigestUtils.md5DigestAsHex("test".toByteArray())
        val ul = bean?.login(LoginRequest().apply {
            userId = "abel"
            devId = "junit"
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            clientHash = DigestUtils.md5DigestAsHex((passHash + timestamp).toByteArray())
        })

        assertTrue(ul?.code == 0)

        val user = bean?.userInfo(BwHolder(buildLoginRequest(ul?.single!!), BwUser().apply { userId = "scott" }))
        lgr.info("user info: {}", ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(user))
        assertTrue(user?.single?.firmId != null)
    }

    @Test
    fun userList() {
        val passHash = DigestUtils.md5DigestAsHex("test".toByteArray())
        val ul = bean?.login(LoginRequest().apply {
            userId = "abel"
            devId = "junit"
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            clientHash = DigestUtils.md5DigestAsHex((passHash + timestamp).toByteArray())
        })

        assertTrue(ul?.code == 0)

        val user = bean?.userList(BwHolder(buildLoginRequest(ul?.single!!), ""))
        lgr.info("user list: {}", ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(user))
        assertTrue(user?.list?.size ?: 0 > 0)
    }

    @Test
    fun createUser() {
        val ul = login()
        val usr = BwUser().apply {
            userId = "test-1"
            userName = "测试-1"
            mobile = "13312345678"
            email = "133@123.cn"
            firmId = "2799"
            verifyStuff = "99"
            passHash = DigestUtils.md5DigestAsHex("hello".toByteArray())
        }

        try {
            val r1 = bean!!.createUser(BwHolder(TestHelper.buildLoginRequest(ul), usr))
            lgr.info("create user result: {}", JSON.toJSONString(r1, true))
            assertEquals(0, r1.code)

            kotlin.run {
                val r2 = bean!!.userList(BwHolder(TestHelper.buildLoginRequest(ul), usr.userId))
                lgr.info("user info: {}", JSON.toJSONString(r2, true))
                assertEquals(0, r2.code)
            }

            // update stuff-id & firm-id
            kotlin.run {
                val r2 = bean!!.updateUser(BwHolder(TestHelper.buildLoginRequest(ul), BwUser().apply {
                    userId = usr.userId
                    firmId = "00"
                    verifyStuff = "98"
                }))
                lgr.info("update user: {}", JSON.toJSONString(r2, true))
                assertEquals(0, r2.code)
            }

            // verify if changed
            kotlin.run {
                val r2 = bean!!.userList(BwHolder(TestHelper.buildLoginRequest(ul), usr.userId))
                lgr.info("changed user info: {}", JSON.toJSONString(r2, true))
                assertEquals(0, r2.code)
                assertEquals("98", r2.list?.firstOrNull()?.verifyStuff)
                assertEquals("27", r2.list?.firstOrNull()?.firmId)
            }
        } finally {
            kotlin.run {
                val r2 = bean!!.deleteUser(BwHolder(TestHelper.buildLoginRequest(ul), BwUser().apply {
                    userId = usr.userId
                }))
                lgr.info("delete user: {}", JSON.toJSONString(r2, true))
                assertEquals(0, r2.code)
            }
        }
    }

    @Test
    fun updateUser() {
        val passHash = DigestUtils.md5DigestAsHex("test".toByteArray())
        val ul = bean?.login(LoginRequest().apply {
            userId = "abel"
            devId = "junit"
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            clientHash = DigestUtils.md5DigestAsHex((passHash + timestamp).toByteArray())
        })

        assertTrue(ul?.code == 0)

        val user = bean?.updateUser(BwHolder(buildLoginRequest(ul?.single!!), BwUser().apply {
            this.userId = "abel"
            this.signPic = "http://localhost:8080/docs/images/tomcat.png"
            this.smallIcon = "http://localhost:8080/examples/jsp/jsp2/jspx/textRotate.jpg"
            this.bigIcon = "http://localhost:8080/docs/images/asf-logo.svg"
        }))
        lgr.info("update user: {}", ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(user))
        assertTrue(user?.single != null)
    }

    @Test
    fun deleteUser() {
        val ul = login()
        val r1 = bean?.deleteUser(BwHolder(TestHelper.buildLoginRequest(ul), BwUser().apply {
            userId = "abel"
        }))

        lgr.info(JSON.toJSONString(r1, true))
        assertNotEquals(0, r1?.code)
    }

    @Test
    fun rightList() {
        val ret = bean?.rightList()
        lgr.info("right list: {}", ObjectMapper().writeValueAsString(ret))
        assertTrue(ret?.list?.size ?: 0 > 1)
    }

    @Test
    fun roleList() {
        val ret = bean?.roleList()
        lgr.info("role list: {}", ObjectMapper().writeValueAsString(ret))
        assertTrue(ret?.list?.size ?: 0 > 1)
    }

    @Test
    fun selectRole() {
    }

    @Test
    fun createRoleDuplicate() {
        val passHash = DigestUtils.md5DigestAsHex("world".toByteArray())
        val ul = bean?.login(LoginRequest().apply {
            userId = "admin"
            devId = "junit"
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            clientHash = DigestUtils.md5DigestAsHex((passHash + timestamp).toByteArray())
        })

        assertTrue(ul?.code == 0)

        val user = bean?.createRole(BwHolder(buildLoginRequest(ul?.single!!), BwRole().apply {
            name = "BACK_USER"
        }))
        lgr.info("try to create role: {}", ObjectMapper().writeValueAsString(user))
        assertTrue(user?.code == 1)
    }

    @Test
    fun createRole() {
        try {
            val passHash = DigestUtils.md5DigestAsHex("world".toByteArray())
            val ul = bean?.login(LoginRequest().apply {
                userId = "admin"
                devId = "junit"
                timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
                clientHash = DigestUtils.md5DigestAsHex((passHash + timestamp).toByteArray())
            })

            assertTrue(ul?.code == 0)

            val user = bean?.createRole(BwHolder(buildLoginRequest(ul?.single!!), BwRole().apply {
                name = "BACK_USER2"
            }))
            lgr.info("try to create role: {}", ObjectMapper().writeValueAsString(user))
            assertTrue(user?.code == 0)
        } finally {
            userMapper!!.deleteRole("BACK_USER2")
        }
    }

    @Test
    fun deleteRoleRef() {
        val passHash = DigestUtils.md5DigestAsHex("world".toByteArray())
        val ul = bean?.login(LoginRequest().apply {
            userId = "admin"
            devId = "junit"
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            clientHash = DigestUtils.md5DigestAsHex((passHash + timestamp).toByteArray())
        })

        assertTrue(ul?.code == 0)

        val user = bean?.deleteRole(BwHolder(buildLoginRequest(ul?.single!!), BwRole().apply {
            name = "BACK_USER"
        }))
        lgr.info("try to delete role: {}", ObjectMapper().writeValueAsString(user))
        assertTrue(user?.code == 3)
    }

    @Test
    fun deleteRole() {
        val passHash = DigestUtils.md5DigestAsHex("world".toByteArray())
        val ul = bean?.login(LoginRequest().apply {
            userId = "admin"
            devId = "junit"
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            clientHash = DigestUtils.md5DigestAsHex((passHash + timestamp).toByteArray())
        })

        assertTrue(ul?.code == 0)

        val user = bean?.deleteRole(BwHolder(buildLoginRequest(ul?.single!!), BwRole().apply {
            name = "DMA_USER"
        }))
        lgr.info("try to delete role: {}", ObjectMapper().writeValueAsString(user))
        assertTrue(user?.code == 4)
    }

    @Test
    fun updateRoleAuthFailed() {
        val passHash = DigestUtils.md5DigestAsHex("test".toByteArray())
        val ul = bean?.login(LoginRequest().apply {
            userId = "abel"
            devId = "junit"
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            clientHash = DigestUtils.md5DigestAsHex((passHash + timestamp).toByteArray())
        })

        assertTrue(ul?.code == 0)

        val user = bean?.updateRole(BwHolder(buildLoginRequest(ul?.single!!), BwRole().apply {
            name = "BACK_USER"
            roleDesc = "test"
            preInit = false
        }))
        lgr.info("try to update role: {}", ObjectMapper().writeValueAsString(user))
        // 预置角色不能修改
        assertTrue(user?.code == 3)
    }

    @Test
    fun updateRole() {
        val passHash = DigestUtils.md5DigestAsHex("world".toByteArray())
        val ul = bean?.login(LoginRequest().apply {
            userId = "admin"
            devId = "junit"
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            clientHash = DigestUtils.md5DigestAsHex((passHash + timestamp).toByteArray())
        })

        assertTrue(ul?.code == 0)

        val user = bean?.updateRole(BwHolder(buildLoginRequest(ul?.single!!), BwRole().apply {
            name = "BACK_USER"
            roleDesc = "test"
            preInit = false
        }))
        lgr.info("try to update role: {}", ObjectMapper().writeValueAsString(user))
        assertTrue(user?.code == 3)
    }

    @Test
    fun firmList() {
        val passHash = DigestUtils.md5DigestAsHex("test".toByteArray())
        val ul = bean?.login(LoginRequest().apply {
            userId = "abel"
            devId = "junit"
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            clientHash = DigestUtils.md5DigestAsHex((passHash + timestamp).toByteArray())
        })

        assertTrue(ul?.code == 0)

        val user = bean?.firmList(BwHolder(buildLoginRequest(ul?.single!!), ""))
        lgr.info("firm list: {}", ObjectMapper().writeValueAsString(user))
        assertTrue(user?.list?.size ?: 0 > 1)
    }

    /**
     * 更新机构，不包括经纬度
     */
    @Test
    fun testUpdateFirm() {
        val ftest = BwFirm().apply {
            firmId = "2799"
            firmName = "测试机构"
        }

        val ul = login()

        try {
            configMapper!!.addFirm(ftest)

            val r1 = bean!!.firmList(BwHolder(TestHelper.buildLoginRequest(ul), ftest.firmId))
            lgr.info("firm list: {}", JSON.toJSONString(r1))

            val r2 = bean!!.updateFirm(BwHolder(TestHelper.buildLoginRequest(ul),
                    ftest.apply {
                        phone = "0875-12345678"
                        addr = "上布水务大楼"
                        email = "abelli5@126.com"
                        fax = "0875-87654321"
                        contact = "fu"
                    }))
            assertEquals(0, r2.code)

            val r3 = bean!!.firmList(BwHolder(TestHelper.buildLoginRequest(ul), ftest.firmId))
            lgr.info("firm list: {}", JSON.toJSONString(r3))
            assertEquals(true, r3.list?.find { it.firmId == ftest.firmId } != null)
        } finally {
            configMapper!!.deleteFirm(ftest.firmId!!)
        }
    }

    /**
     * 更新机构，包括经纬度
     */
    @Test
    fun testUpdateFirmGeometry() {
        val ftest = BwFirm().apply {
            firmId = "2799"
            firmName = "测试机构"
            firmLoc = GeometryFactory().createPoint(Coordinate(117.435974, 33.609617)).toText()
        }

        val ul = login()

        try {
            configMapper!!.addFirm(ftest)

            val r1 = bean!!.firmList(BwHolder(TestHelper.buildLoginRequest(ul), ftest.firmId))
            lgr.info("firm list: {}", JSON.toJSONString(r1), true)

            val r2 = bean!!.updateFirm(BwHolder(TestHelper.buildLoginRequest(ul),
                    ftest.apply {
                        phone = "0875-12345678"
                        addr = "上布水务大楼"
                        email = "abelli5@126.com"
                        fax = "0875-87654321"
                        contact = "fu"
                        firmRegion = GeometryFactory().createPolygon(arrayOf(
                                Coordinate(117.435974, 33.609617),
                                Coordinate(117.535974, 33.509617),
                                Coordinate(117.335974, 33.309617),
                                Coordinate(117.235974, 33.709617),
                                Coordinate(117.435974, 33.609617)
                        )).toText()
                    }))
            assertEquals(0, r2.code)

            val r3 = bean!!.firmList(BwHolder(TestHelper.buildLoginRequest(ul), ftest.firmId))
            lgr.info("firm list: {}", JSON.toJSONString(r3, true))
            assertEquals(true, r3.list?.find { it.firmId == ftest.firmId } != null)
        } finally {
            configMapper!!.deleteFirm(ftest.firmId!!)
        }
    }

    @Test
    fun testAddFirm() {
        val ftest = BwFirm().apply {
            firmId = "2799"
            firmName = "测试机构"
            firmLoc = GeometryFactory().createPoint(Coordinate(117.435974, 33.609617)).toText()
            phone = "0875-12345678"
            addr = "上布水务大楼"
            postcode = "123456"
            email = "abelli5@126.com"
            fax = "0875-87654321"
            contact = "fu"
            firmRegion = GeometryFactory().createPolygon(arrayOf(
                    Coordinate(117.435974, 33.609617),
                    Coordinate(117.535974, 33.509617),
                    Coordinate(117.335974, 33.309617),
                    Coordinate(117.235974, 33.709617),
                    Coordinate(117.435974, 33.609617)
            )).toText()
        }

        val ul = login()

        try {
            val r5 = bean!!.addFirm(BwHolder(TestHelper.buildLoginRequest(ul), ftest))
            lgr.info("add firm: {}", JSON.toJSONString(r5, true))
            assertEquals(0, r5.code)

            val r1 = bean!!.firmList(BwHolder(TestHelper.buildLoginRequest(ul), ftest.firmId))
            lgr.info("firm list: {}", JSON.toJSONString(r1.list?.find { it.firmId == ftest.firmId }, true))

            val r2 = bean!!.deleteFirm(BwHolder(TestHelper.buildLoginRequest(ul), ftest))
            lgr.info("delete firm: {}", JSON.toJSONString(r2, true))
            assertEquals(0, r2.code)

            val r3 = bean!!.firmList(BwHolder(TestHelper.buildLoginRequest(ul), ftest.firmId))
            lgr.info("firm list: {}", JSON.toJSONString(r3.list?.find { it.firmId == ftest.firmId }, true))
            assertEquals(0, r3.code)
        } finally {
            configMapper!!.deleteFirm(ftest.firmId!!)
        }
    }

    @Test
    fun testDeleteFirm() {
        val ftest = BwFirm().apply {
            firmId = "2799"
            firmName = "test-2799"
        }
        val fmain = BwFirm().apply {
            firmId = "27"
        }
        val ul = login()

        try {
            val r1 = bean!!.addFirm(BwHolder(TestHelper.buildLoginRequest(ul), ftest))
            assertEquals(0, r1.code)

            val r5 = bean!!.deleteFirm(BwHolder(TestHelper.buildLoginRequest(ul), ftest))
            lgr.info("delete firm: {}", JSON.toJSONString(r5, true))
            assertEquals(0, r5.code)

            val r2 = bean!!.deleteFirm(BwHolder(TestHelper.buildLoginRequest(ul), fmain))
            lgr.info("delete firm: {}", JSON.toJSONString(r2, true))
            assertNotEquals(0, r2.code)

            val r3 = bean!!.firmList(BwHolder(TestHelper.buildLoginRequest(ul), fmain.firmId))
            lgr.info("firm list: {}", JSON.toJSONString(r3.list?.find { it.firmId == fmain.firmId }, true))
            assertEquals(0, r3.code)
        } finally {
            configMapper!!.deleteFirm(ftest.firmId!!)
        }
    }

    @Test
    fun operList() {
        val ul = login()
        val r1 = bean!!.operList(BwHolder(TestHelper.buildLoginRequest(ul), UserOperParam()))
        lgr.info("oper list: {}...", JSON.toJSONString(r1, true).take(1000))
    }

    @Test
    fun operStat() {
        val ul = login()
        val r1 = bean!!.operStat(BwHolder(TestHelper.buildLoginRequest(ul), UserOperParam()))
        lgr.info("oper stat: {}...", JSON.toJSONString(r1, true).take(1000))
    }

    @Test
    fun testKickLogin() {
        val passHash = DigestUtils.md5DigestAsHex("test".toByteArray())
        val ul = bean?.login(LoginRequest().apply {
            userId = "abel"
            devId = "junit"
            timestamp = DateTime.now().toString(ISODateTimeFormat.basicDateTime())
            clientHash = DigestUtils.md5DigestAsHex((passHash + timestamp).toByteArray())
        })

        assertTrue(ul?.code == 0)

        val ret = bean?.kickLogin(BwHolder(buildLoginRequest(ul?.single!!), UserOperParam().apply {
            firmId = "11"
            userId = "abel"
        }))
        lgr.info("kick login: {}", ObjectMapper().writeValueAsString(ret))
        assertTrue(ret?.code == 0)
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(UserServiceImplTest::class.java)
    }
}