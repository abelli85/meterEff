/**
 *
 */
package com.abel.bigwater.model

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

/**
 * @author Abel5
 */
@JsonIgnoreProperties(value = ["updateBy", "createBy", "updateDate", "createDate", "loginGeom"], ignoreUnknown = true)
class BwUserLogin : BwObject() {
    /**
     * @return the id
     */
    var id: String? = null

    /**
     * @return the userId
     */
    var userId: String? = null

    /**
     * @return the loginTime
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var loginTime: Date? = null

    /**
     * 登录ip地址.
     */
    var loginIp: String? = null

    /**
     * 登录主机名
     */
    var loginHost: String? = null

    /**
     * 登录地区
     */
    var loginAddr: String? = null

    /**
     * 登录经纬度
     */
    var loginLoc: String? = null

    /**
     * 登出时间.
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var logoutTime: Date? = null

    /**
     * 登出IP地址
     */
    var logoutIp: String? = null

    /**
     * 登出地区.
     */
    var logoutLoc: String? = null

    /**
     * @return the ipAddr
     */
    var ipAddr: String? = null

    /**
     * @return the devId
     */
    var devId: String? = null

    /**
     * 对称秘钥, 登录后服务器下发, 保存在客户机,
     * 每次请求时混淆sessionId及时间戳, 会话期间(包括离线)有效.
     */
    var shareSalt: String? = null

    /**
     * @return the userName
     */
    var userName: String? = null

    /**
     * @return the firmId
     */
    var firmId: String? = null

    /**
     * @return the firmName
     */
    var firmName: String? = null

    /**
     * 小头像，建议大小: 64×64像素
     */
    var smallIcon: String? = null

    /**
     * 大头像，建议大小：512×512像素
     */
    var bigIcon: String? = null

    /**
     * 签名图，建议大小: 64×192 像素，放大或缩小应保持长宽比不变
     */
    var signPic: String? = null

    /**
     * @return the firm
     */
    var firm: BwFirm? = null

    /**
     * role list of this user.
     */
    var roleList: List<BwRole>? = null

    /**
     * right list of this user
     */
    var rightList: List<BwRight>? = null

    /**
     * 登录的地址
     */
    var loginGeom: IpGeom? = null

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    override fun toString(): String {
        return String.format(
                "BwUserLogin [id=%s, userId=%s, loginTime=%s, ipAddr=%s, devId=%s, userName=%s, firmId=%s, firmName=%s]",
                id, userId, loginTime, ipAddr, devId, userName, firmId, firmName)
    }

}
