package com.abel.bigwater.model

import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

enum class UserStatus {
    /** 新建 */
    NEWBIE,
    /** 活跃 */
    ACTIVE,
    /** 在线 */
    ONLINE,
    /** 过期 */
    EXPIRED,
    /** 黑名单 */
    BLACK
}

class BwUser : BwObject() {
    /**
     * @return the id
     */
    var id: String? = null

    /**
     * @return the name
     */
    var name: String? = null

    /**
     * 检定员ID
     */
    var verifyStuff: String? = null

    /** 最后登录IP  */
    var lastLoginIp: String? = null

    /** 最后登录地区. */
    var lastLoginLoc: String? = null

    /** 登录总次数  */
    var loginCount = 0

    /** 最后登录时间  */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var lastLoginTime: Date? = null

    /** 最后一次操作时间 */
    var lastOperTime: Date? = null

    /** 最后操作IP */
    var lastOperIp: String? = null

    /** 最后操作地区 */
    var lastOperLoc: String? = null

    /** 创建时间  */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var createTime: Date? = null

    /**
     * @return the mobile
     */
    var mobile: String? = null

    /**
     * @return the email
     */
    var email: String? = null

    /**
     * @return the passHash
     */
    var passHash: String? = null

    /**
     * 是否启用, 参考 {@link UserStatus}.
     * @return the status
     */
    var status: String? = UserStatus.EXPIRED.name
        set(value) {
            field = if (value != null) UserStatus.valueOf(value)?.name else UserStatus.EXPIRED.name
            _statusObj = UserStatus.valueOf(field!!)
        }

    private var _statusObj: UserStatus? = null
    var statusObj: UserStatus?
        set(value) {
            _statusObj = value ?: UserStatus.EXPIRED
            status = _statusObj!!.name
        }
        get() = _statusObj

    /**
     * 水司ID
     * @return the firmId
     */
    var firmId: String? = null

    /**
     * 水司名称
     */
    var firmName: String? = null

    /**
     * @return the emailValid
     */
    var emailValid: Boolean? = null

    /**
     * @return the emailToken
     */
    var emailToken: String? = null

    /**
     * This token is used as pushed token.
     */
    var userToken: String? = null

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
     * @return the dmaId
     */
    var dmaId: String? = null

    /**
     * @return the dmaIds
     */
    var dmaIdList: List<String>? = null

    /**
     * @return the firm
     */
    var firm: BwFirm? = null

    /**
     * @return the roles
     */
    var roles: List<BwRole>? = null

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    override fun toString(): String {
        return ("BwUser [id=" + id + ", name=" + name + ", mobile=" + mobile
                + ", email=" + email + ", passHash=" + passHash + ", status="
                + status + ", firmId=" + firmId + ", emailValid=" + emailValid
                + ", emailToken=" + emailToken + ", userToken=" + userToken
                + ", dmaId=" + dmaId + ", firm=" + firm + ", roles=" + roles
                + "]")
    }

}
