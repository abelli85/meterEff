package com.abel.bigwater.model.eff

import com.abel.bigwater.model.BwBase
import com.abel.bigwater.model.JsonDateDeserializer
import com.abel.bigwater.model.JsonDateSerializer
import com.abel.bigwater.model.JsonHelper
import com.alibaba.fastjson.annotation.JSONField
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.util.*

open class VcMeterVerify : BwBase() {

    /**
     * 检定自动编号
     */
    var verifyId: Long? = null

    /**
     * 水表标示
     */
    var meterId: String? = null

    /**
     * 委托编号
     */
    var batchId: String? = null

    /**
     * 厂家ID
     */
    var factId: String? = null

    /**
     * 类型ID (1-普通, 2-宽量程)
     */
    var typeId: String? = null

    /**
     * 厂家名称
     */
    var factName: String? = null

    /**
     * 类型名称
     */
    var typeName: String? = null

    /**
     * 修正：作业类型（首次检定、后续检定、水表测试）
     * 检定类型, 参考代码:
     * @see VerifyType
     */
    var verifyTypeId: String? = null

    /**
     * 修正：作业类型（首次检定、后续检定、水表测试）
     * 检定类型名称
     * @see VerifyType
     */
    var verifyTypeName: String? = null

    /**
     * 作业原因（安装前首检、大表后续检定、争议检定、使用中抽检、水表招标，实验室比对，期间核查，重复性试验，稳定性考核，计量研究）
     * @see VerifyReasonType
     */
    var verifyReason: String? = null

    /**
     * 报告类型（检定报告和测试报告，一般作业类型为首次检定和后续检定的，只出检定报告；
     * 作业类型为水表测试的，可能只出测试报告，也可能同时出检定报告和测试报告。
     * 检定报告分为检定证书和检定结果通知书，检定结果合格出检定证书，不合格出检定结果通知书）
     * @see ReportType
     */
    var rptType: String? = null

    /**
     * 水表类型（普通机械水表、高精度机械水表、无源光电直读水表、NB-IOT无磁传感水表，超声波水表、电磁水表）
     * @see MeterClassType
     */
    var meterClass: String? = null

    /**
     * 品牌ID，
     */
    var brandId: String? = null

    /**
     * 品牌名称
     */
    var brandName: String? = null

    /**
     * 准确度等级：    级（默认2级，可修改为1级）、
     */
    // var accurateGrade: String? = null

    /**
     * MAP=1.0 Mpa（默认为1.0MAP，可选择1.0或1.6，也可改为别的）、
     */
    var meterMap: Double = 1.0

    /**
     * 温度等级：□T30 □T50（默认是T30）
     */
    var tempGrade: String? = null

    /**
     * 强检序号
     */
    var forceVerifyNo: String? = null

    /**
     * 模块编号
     */
    var moduleNo: String? = null

    /**
     * 送检单位
     */
    var clientName: String? = null

    /**
     * 生产厂家
     */
    var factoryName: String? = null

    /**
     * 仪表名称
     */
    var meterName: String? = null

    /**
     * 表类型
     * @see VcCode
     * @see VcCodeValue
     */
    var meterType: String? = null

    /**
     * 口径
     * @see VcCode
     * @see VcCodeValue
     */
    var sizeId: Int? = null

    /**
     * 口径名称
     * @see VcCode
     * @see VcCodeValue
     */
    var sizeName: String? = null

    /**
     * 型号规格
     */
    var modelSize: String? = null

    /**
     * 接口类型
     */
    var portType: String? = null

    /**
     * 检定依据
     */
    var verifyRule: String? = null

    /**
     * 标准装置
     */
    var standardInstrument: String? = null

    /**
     * 装置编号
     */
    var instrumentNo: String? = null

    /**
     * 检定机构代码
     * 91440300MA5FGUD013
     */
    var stdFirm: String? = null

    /**
     * 装置型号规格
    XBT15-50BZ
     */
    var platModel: String? = null

    /**
     * 检定台的自增量标识
     */
    var itemId: Long? = null

    /**
     * 检定参数
     */
    var standardParam: String? = null

    /**
     * 室温
     */
    var indoorTemp: Double? = 0.0

    /**
     * 相对湿度
     */
    var moisture: Double? = 0.0

    /**
     * 转换试验
     */
    var convertResult: String? = null

    /**
     * 耐压试验
     */
    var pressResult: String? = null

    /**
     * 模板标示
     */
    var tempId: String? = null

    /**
     * 检定员
     */
    var stuffName: String? = null

    /**
     * 检定结果.
     * @see VerifyResultType
     */
    var verifyResult: String? = null

    /**
     * 来自检定台的结论. 一般采用中文.
     * @see VerifyResultType.PASS.result
     */
    var boardResult: String? = null

    /**
     * 准确度等级
     */
    var accurateGrade: String? = null

    /**
     * 管道温度
     */
    var pipeTemp: Double? = 0.0

    /**
     * 管道压力
     */
    var pipePressure: Double? = 0.0

    /**
     * 脉冲当量
     */
    var pulse: Double? = 0.0

    /**
     * 密度
     */
    var density: Double? = 0.0

    /**
     * 示值误差
     */
    var displayDiff: Double? = 0.0

    /**
     * 机电转换误差
     */
    var convertDiff: Double? = 0.0

    /**
     * Q4
     */
    var q4: Double? = 0.0

    /**
     * Q3
     */
    var q3: Double? = 0.0

    /**
     * Q3/Q1
     */
    var q3toq1: Double? = 0.0

    /**
     * Q4/Q3
     */
    var q4toq3: Double? = 0.0

    /**
     * Q2/Q1
     */
    var q2toq1: Double? = 0.0

    /**
     * 最大流量
     */
    var maxFlow: Double? = 0.0

    /**
     * 最小流量
     */
    var minFlow: Double? = 0.0

    /**
     * 常用流量
     */
    var commonFlow: Double? = 0.0

    /**
     * 机电转换误差限
     */
    var convertLimit: Double? = 0.0

    /**
     * 复检
     */
    var verifyAgain: String? = null

    /**
     * 外观检查
     */
    var outerCheck: String? = null

    /**
     * q1~9的精度数据
     */
    var qr1: Double? = null
    var qr2: Double? = null
    var qr3: Double? = null
    var qr4: Double? = null
    var qr5: Double? = 0.0
    var qr6: Double? = 0.0
    var qr7: Double? = 0.0
    var qr8: Double? = 0.0
    var qr9: Double? = 0.0

    /**
     * 机构ID
     */
    var firmId: String? = null

    /**
     * 机构名称
     */
    var firmName: String? = null

    /**
     * 水表状态
     * ref to
     * @see MeterStatusType
     */
    var status: String? = null

    /**
     * 异常原因, 如果 status 为 ABNORMAL, 则该字段有值.
     */
    var abnormalReason: String? = "丹纳导入"

    /**
     * 该条检定结果包含的检定点.
     */
    var pointList: List<VcMeterVerifyPoint>? = null

    /**
     * 检定日期
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var verifyDate: Date? = null

    /**
     * 有效期
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var validDate: Date? = null

    /**
     * 生产日期
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var makeDate: Date? = null

    /**
     * 采购日期
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var buyDate: Date? = null

    /**
     * 运抵日期
     */
    @JsonSerialize(using = JsonDateSerializer::class)
    @JsonDeserialize(using = JsonDateDeserializer::class)
    @JSONField(format = JsonHelper.FULL_DATE_FORMAT)
    var deliverDate: Date? = null

    /**
     * 不合格原因，包括外观、机电转换、压力、示值 4类
     */
    var summaryResult: String? = null
        get() {
            val bld = StringBuilder()
            if (!outerCheck.isNullOrBlank() && outerCheck != "合格") bld.append("外观").append(',')
            if (!convertResult.isNullOrBlank() && convertResult != "合格") bld.append("机电转换").append(',')
            if (!pressResult.isNullOrBlank() && pressResult != "合格") bld.append("压力").append(',')
            if (!boardResult.isNullOrBlank() && boardResult != "合格") bld.append("示值").append(',')

            return when {
                boardResult.isNullOrBlank() -> null
                bld.isNullOrBlank() -> "合格"
                else -> bld.dropLast(1).toString() + "不合格"
            }
        }
}

