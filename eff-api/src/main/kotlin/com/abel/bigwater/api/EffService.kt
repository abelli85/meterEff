package com.abel.bigwater.api

import com.abel.bigwater.model.eff.EffMeter
import com.abel.bigwater.model.eff.EffMeterPoint
import com.abel.bigwater.model.eff.EffTask
import com.abel.bigwater.model.eff.VcEffDecay
import org.apache.dubbo.rpc.protocol.rest.support.ContentType
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("/eff")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8)
interface EffService {

    companion object {
        const val BASE_PATH = "/eff"

        const val PATH_CREATE_TASK = "/createEffTask"
        const val PATH_LIST_TASK = "/listEffTask"
        const val PATH_FETCH_TASK = "/fetchEffTask"

        const val PATH_LIST_METER_EFF = "/listMeterEff"
        const val PATH_ADD_METER_EFF = "/addMeterEff"
        const val PATH_DELETE_METER_EFF = "/deleteMeterEff"
        const val PATH_UPDATE_METER_EFF = "/updateMeterEff"

        const val PATH_LIST_FAILURE_EFF = "/listEffFailure"
        const val PATH_DELETE_EFF_FAILURE = "/deleteEffFailure"

        const val PATH_FETCH_METER_EFF = "/fetchMeterEff"
        const val PATH_REPLACE_METER_EFF = "/replaceMeterEff"
        const val PATH_MATCH_METER = "/matchMeter"
        const val PATH_BUILD_METER_EFF = "/buildMeterEff"
        const val PATH_PUSH_EFF_TASK = "/pushEffTask"

        const val PATH_LIST_EFF_POINT = "/listEffPoint"

        const val PATH_LIST_EFF_DECAY = "/listEffDecay"
        const val PATH_INSERT_EFF_DECAY = "/insertEffDecay"
        const val PATH_DELETE_EFF_DECAY = "/deleteEffDecay"
        const val PATH_CONFIG_METER_DECAY = "/configMeterDecay"
    }

    /**
     * 创建计量效率分析任务
     */
    @POST
    @Path(PATH_CREATE_TASK)
    fun createEffTask(holder: BwHolder<EffTask>): BwResult<EffTask>

    /**
     * 列出计量效率分析任务
     */
    @POST
    @Path(PATH_LIST_TASK)
    fun listEffTask(holder: BwHolder<EffParam>): BwResult<EffTask>

    /**
     * 获取单个计量效率分析任务的详情
     * @see EffParam.taskId
     */
    @POST
    @Path(PATH_FETCH_TASK)
    fun fetchEffTask(holder: BwHolder<EffParam>): BwResult<EffTask>

    /**
     * 列出水表的分析结果, 填充参数:
     * @see EffParam.meterId
     * @see EffParam.meterIdList
     * @see EffParam.taskStart
     * @see EffParam.taskEnd
     */
    @POST
    @Path(PATH_LIST_METER_EFF)
    fun listMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter>

    /**
     * 列出水表的无效分析结果, 填充参数:
     * @see EffParam.taskResult
     * @see EffParam.meterId
     * @see EffParam.meterIdList
     * @see EffParam.taskStart
     * @see EffParam.taskEnd
     */
    @POST
    @Path(PATH_LIST_FAILURE_EFF)
    fun listEffFailure(holder: BwHolder<EffParam>): BwResult<EffMeter>

    /**
     * 添加水表的分析结果
     * @see EffParam.meterList
     */
    @POST
    @Path(PATH_ADD_METER_EFF)
    fun addMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter>

    /**
     * 删除水表的分析结果
     * @see EffParam.taskId
     * @see EffParam.meterId 或
     * @see EffParam.meterIdList
     */
    @POST
    @Path(PATH_DELETE_METER_EFF)
    fun deleteMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter>

    /**
     * 删除水表的分析失败记录
     * @see EffParam.taskId - 可选
     * @see EffParam.meterId 或
     * @see EffParam.meterIdList
     * @see EffParam.taskStart - 日期范围
     * @see EffParam.taskEnd - 日期范围
     */
    @POST
    @Path(PATH_DELETE_EFF_FAILURE)
    fun deleteEffFailure(holder: BwHolder<EffParam>): BwResult<EffMeter>

    /**
     * 修改水表的分析结果
     * @see EffParam.taskId
     * @see EffParam.meterList
     */
    @POST
    @Path(PATH_UPDATE_METER_EFF)
    fun updateMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter>

    /**
     * 获取单个水表的分析详情
     * @see EffParam.taskId
     * @see EffParam.meterId
     */
    @POST
    @Path(PATH_FETCH_METER_EFF)
    fun fetchMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter>

    /**
     * 获取计量点列表
     * @see EffParam.meterId
     * @see EffParam.taskStart
     * @see EffParam.taskEnd
     * @see EffParam.periodType - 默认返回 Day (天); Month 返回月;  如需同时返回, 设为 %.
     * @see EffParam.pointType - 默认返回 MODEL (消费模式的计量点); EFF 返回 计量效率的计量点; 如需同时返回, 设为 %.
     */
    @POST
    @Path(PATH_LIST_EFF_POINT)
    fun listEffPoint(holder: BwHolder<EffParam>): BwResult<EffMeterPoint>

    /**
     * 修改单个水表的分析详情
     * @see EffParam.taskId
     * @see EffParam.meterId
     * @see EffParam.pointEffList
     */
    @POST
    @Path(PATH_REPLACE_METER_EFF)
    fun replaceMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter>

    /**
     * 仅针对远传水表进行口径匹配分析。
     * Step1:暂定Q2～Q3 流量段用水量占比≥80%，匹配。否则，不匹配。
     * 可对Q2～Q3 流量段用水量占比进行调整赋值:
     * @see EffParam.matchQ2v - 默认 80.0
     * @see EffParam.sizeId - 可空
     * @see EffParam.meterBrandId - 可空
     * @see EffParam.modelSize - 可空
     * @see EffParam.taskStart - 可空
     * @see EffParam.taskEnd - 可空
     * @see EffParam.firmId - 可空
     * @see EffParam.meterId - 可空
     * @see EffParam.meterIdList - 可空
     * @see EffParam.periodType - 默认 Day
     */
    @POST
    @Path(PATH_MATCH_METER)
    fun matchMeter(holder: BwHolder<EffParam>): BwResult<EffMeter>

    /**
     * 分析水表的计量效率, 分析一只或多只水表, 指定时段的计量效率.
     * @see EffParam.meterId
     * @see EffParam.meterIdList
     * @see EffParam.taskStart - can be null
     * @see EffParam.taskEnd - can be null
     */
    @POST
    @Path(PATH_BUILD_METER_EFF)
    fun buildMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter>

    /**
     * 列出水表老化规则（每百万方水计量效率衰减）, 如下参数均可选:
     * @see EffParam.meterBrandId
     * @see EffParam.sizeId
     * @see EffParam.sizeName
     * @see EffParam.modelSize
     */
    @POST
    @Path(PATH_LIST_EFF_DECAY)
    fun selectEffDecay(holder: BwHolder<EffParam>): BwResult<VcEffDecay>

    /**
     * 插入水表老化规则（每百万方水计量效率衰减）, 必填:
     * @see EffParam.decayList
     */
    @POST
    @Path(PATH_INSERT_EFF_DECAY)
    fun insertEffDecay(holder: BwHolder<EffParam>): BwResult<VcEffDecay>

    /**
     * 删除水表老化规则（每百万方水计量效率衰减）, 必填:
     * @see EffParam.decayList
     */
    @POST
    @Path(PATH_DELETE_EFF_DECAY)
    fun deleteEffDecay(holder: BwHolder<EffParam>): BwResult<VcEffDecay>

    /**
     * 单个或批量设置水表的老化模板.
     * 可指定水表ID或列表, 以老化模板设定:
     * @see EffParam.meterId
     * @see EffParam.meterIdList
     *
     * 老化模板可单独指定, 则设置匹配品牌/口径/型号的水表老化.
     * @see EffParam.decayId - 如为空, 则清除水表的老化模板
     *
     * 或者指定模板的类型:
     * @see EffParam.meterBrandId
     * @see EffParam.sizeId
     * @see EffParam.modelSize
     */
    @POST
    @Path(PATH_CONFIG_METER_DECAY)
    fun configMeterDecay(holder: BwHolder<EffParam>): BwResult<VcEffDecay>

    /**
     * 分析水表的计量效率, 可以是整个水司, 一只或多只水表, 不指定时段 或 指定时段的计量效率.
     * @see EffParam.taskStart
     * @see EffParam.taskEnd
     */
    @POST
    @Path(PATH_PUSH_EFF_TASK)
    fun pushEffTask(holder: BwHolder<EffParam>): BwResult<EffTask>
}