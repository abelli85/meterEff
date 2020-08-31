package com.abel.bigwater.impl

import com.abel.bigwater.api.BwHolder
import com.abel.bigwater.api.BwResult
import com.abel.bigwater.api.EffParam
import com.abel.bigwater.api.EffService
import com.abel.bigwater.model.eff.EffMeter
import com.abel.bigwater.model.eff.EffTask
import org.springframework.stereotype.Service

@Service("effService")
class EffServiceImpl : EffService {
    /**
     * 列出计量效率分析任务
     */
    override fun listEffTask(holder: BwHolder<EffParam>): BwResult<EffTask> {
        TODO("Not yet implemented")
    }

    /**
     * 获取单个计量效率分析任务的详情
     */
    override fun fetchEffTask(holder: BwHolder<EffParam>): BwResult<EffTask> {
        TODO("Not yet implemented")
    }

    /**
     * 列出水表的分析结果
     */
    override fun listMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter> {
        TODO("Not yet implemented")
    }

    /**
     * 获取单个水表的分析详情
     */
    override fun fetchMeterEff(holder: BwHolder<EffParam>): BwResult<EffMeter> {
        TODO("Not yet implemented")
    }
}