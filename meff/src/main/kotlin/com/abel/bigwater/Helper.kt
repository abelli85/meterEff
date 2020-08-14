package com.abel.bigwater

object Helper {
    const val TOP_FIRM_ID = "1"

    /**
     * 超级用户, firmId不变;
     * 否则使用login.firmId或者其子公司的firmId.
     * e.g., ('23', '1') = '23';
     * ('2301', '23') = '2301';
     * ('2401', '23') = '23';
     * @param pfirmId firm-id needs to be refined
     * @param loginFirmId firm-id of login user.
     */
    inline fun refineFirmId(pfirmId: String?, loginFirmId: String, withWildcard: Boolean = true): String? {
        // 超级用户
        if (loginFirmId == TOP_FIRM_ID) return pfirmId

        // 必须为本司或子公司
        return if (pfirmId?.isNotBlank() == true && pfirmId.startsWith(loginFirmId)) pfirmId
        else if (withWildcard) "${loginFirmId}%" else loginFirmId
    }

    /***
     * 超级用户, 允许更新;
     * 否则只允许更新其本司及其子公司的firmId.
     * e.g., ('23', '1') = true;
     * ('2301', '23') = true;
     * ('', '23') = true;
     * ('24', '23') = false;
     * ('2401', '23') = false;
     */
    fun canChange(firmId: String?, userFirmId: String): Boolean {
        // 超级用户
        if (userFirmId == TOP_FIRM_ID) return true

        // 必须为本司或子公司
        return firmId.isNullOrBlank() || firmId.startsWith(userFirmId)
    }
}