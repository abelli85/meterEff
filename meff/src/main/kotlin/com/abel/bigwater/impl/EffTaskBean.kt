package com.abel.bigwater.impl

import com.abel.bigwater.api.DataParam
import com.abel.bigwater.api.EffParam
import com.abel.bigwater.api.MeterParam
import com.abel.bigwater.mapper.ConfigMapper
import com.abel.bigwater.mapper.DataMapper
import com.abel.bigwater.mapper.EffMapper
import com.abel.bigwater.mapper.MeterMapper
import com.abel.bigwater.model.BwData
import com.abel.bigwater.model.BwFirm
import com.abel.bigwater.model.BwMeter
import com.abel.bigwater.model.DataRange
import com.abel.bigwater.model.eff.*
import com.abel.bigwater.model.zone.ZoneMeter
import com.alibaba.fastjson.JSON
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.LocalDate
import org.joda.time.format.ISODateTimeFormat
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue

@Component
@EnableScheduling
class EffTaskBean {

    @Autowired
    private var configMapper: ConfigMapper? = null

    @Autowired
    private var meterMapper: MeterMapper? = null

    @Autowired
    private var dataMapper: DataMapper? = null

    @Autowired
    private var effMapper: EffMapper? = null

    @Scheduled(cron = "0 15 0/6 * * ?")
    fun effAll() {
        lgr.info("定时任务: 分析所有水司的水表计量效率...")
        val firmList = configMapper!!.selectFirm("%")
        firmList.forEach {
            try {
                effFirm(it)
            } catch (ex: Exception) {
                lgr.error("fail to build eff for ${it.firmId}/${it.firmName} caused by ${ex.message}", ex)
            }
        }

        lgr.info("完成任务: 分析 ${firmList.size} 家水司的水表计量效率.")
    }

    fun effFirm(firm: BwFirm) {
        val task = EffTask().apply {
            taskName = "auto"
            firmId = firm.firmId
            firmName = firm.firmName
            periodTypeObj = EffPeriodType.Day
            taskStart = DUMMY_START.toDate()
            taskEnd = DUMMY_END.toDate()

            effMapper!!.createEffTask(this)
        }

        meterMapper!!.selectMeterDma(MeterParam().apply {
            firmId = firm.firmId
        }).forEach {
            if (!fillPointList(it)) {
                lgr.warn("计量点不足3个或Q2/Q3不存在: {}/{}", it.meterId, it.meterName)
                return@forEach
            }
            lgr.info("fill-point-list result: {}", JSON.toJSONString(it, true))

            val effList = effMeter(it, task, 31)
            lgr.info("analyze {} eff for {}/{} in {}", effList.size,
                    it.meterId, it.meterName, firm.title)
            if (effList.isEmpty()) return@forEach

            buildMonthEff(effList)
        }
    }

    fun buildMonthEff(effList: List<EffMeter>) {
        val pmth = EffParam().apply {
            meterId = effList.firstOrNull()?.meterId
            periodTypeObj = EffPeriodType.Month
            taskStart = effList.minOf { e1 -> e1.taskStart!! }
            taskEnd = effList.maxOf { e1 -> e1.taskStart!! }
        }

        // truncate to month
        pmth.also {
            it.taskStart = it.jodaTaskStart?.withDayOfMonth(1)?.toDate()
            it.taskEnd = it.jodaTaskEnd?.plusMonths(1)?.withDayOfMonth(1)?.toDate()

        }
        effMapper!!.deleteEffMeter(pmth)
        val cntEff = effMapper!!.buildEffMeterMonth(pmth)
        val cntPt = effMapper!!.buildEffPointMonth(pmth)
        lgr.info("build month eff: $cntEff / $cntPt")
    }

    /**
     * fill model-point-list, not for efficiency.
     */
    private fun fillModelPointList(meter: ZoneMeter) {
        val mpList = arrayListOf<VcMeterVerifyPoint>()
        if (meter.q1 > 1E-3) {
            q1@ kotlin.run {
                // model point.
                mpList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 1
                    pointName = "Q1/2"
                    pointFlow = meter.q1 / 2
                    pointDev = meter.q1r / 2
                })
                mpList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 2
                    pointName = "Q1"
                    pointFlow = meter.q1
                    pointDev = meter.q1r
                })
            }

            q2@ if (meter.q2 > meter.q1) {
                // model point.
                mpList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 3
                    pointName = "(Q1+Q2)/2"
                    pointFlow = meter.q1.plus(meter.q2).div(2)
                    pointDev = meter.q1r.plus(meter.q2r).div(2)
                })
                mpList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 4
                    pointName = "Q2"
                    pointFlow = meter.q2
                    pointDev = meter.q2r
                })
            }

            q3@ if (meter.q3 > meter.q2) {
                // model point.
                mpList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 5
                    pointName = "Q2+(Q3-Q2)/5"
                    pointFlow = meter.q2 + (meter.q3 - meter.q2) / 5
                    pointDev = meter.q2r + (meter.q3r - meter.q2r) / 5
                })
                mpList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 6
                    pointName = "Q2+(Q3-Q2)*2/5"
                    pointFlow = meter.q2 + (meter.q3 - meter.q2) * 2 / 5
                    pointDev = meter.q2r + (meter.q3r - meter.q2r) * 2 / 5
                })
                mpList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 7
                    pointName = "Q2+(Q3-Q2)*3/5"
                    pointFlow = meter.q2 + (meter.q3 - meter.q2) * 3 / 5
                    pointDev = meter.q2r + (meter.q3r - meter.q2r) * 3 / 5
                })
                mpList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 8
                    pointName = "Q2+(Q3-Q2)*4/5"
                    pointFlow = meter.q2 + (meter.q3 - meter.q2) * 4 / 5
                    pointDev = meter.q2r + (meter.q3r - meter.q2r) * 4 / 5
                })
                mpList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 9
                    pointName = "Q3"
                    pointFlow = meter.q3
                    pointDev = meter.q3r
                })
            }

            q4@ if (meter.q4 > meter.q3) {
                // model point.
                mpList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 10
                    pointName = "Q4"
                    pointFlow = meter.q4
                    pointDev = meter.q4r
                })
            }
        }

        if (mpList.size < 2 + 2 + 5 + 1) {
            lgr.error("标准流量点不完整: ${meter.meterId} (${meter.meterName})")
        }

        meter.modelPointList = mpList.toList()
    }

    /**
     * 填充计量点. 按照顺序:
     * @see ZoneMeter.decayId
     * @see VcMeterVerifyPoint
     * @see ZoneMeter.q1 - 2/3/4
     */
    fun fillPointList(meter: ZoneMeter): Boolean {
        val ptList = if (meter.decayId ?: 0 > 0) {
            fillPointListFromDecay(meter)
        } else {
            ArrayList<VcMeterVerifyPoint>(meterMapper!!.listVerifyPointLast(MeterParam().apply {
                meterId = meter.meterId
            }))
        }

        if (meter.q1 < 1E-3 || meter.q2 <= meter.q1 || meter.q3 <= meter.q2) return false

        // fill model-point-list after q1/2/3/4 are filled.
        fillModelPointList(meter)

        ptList.forEach {
            if (it.pointFlow == null) it.pointFlow = 0.0
            if (it.pointDev == null) it.pointDev = 0.0
        }

        if (meter.q1 > 1E-3) {
            q1@ kotlin.run {
                val p1 = ptList.find { it.pointFlow!!.minus(meter.q1).absoluteValue < 1E-3 }
                if (p1 != null) {
                    p1.pointNo = 1
                    p1.pointName = "Q1"
                } else
                    ptList.add(VcMeterVerifyPoint().apply {
                        meterId = meter.meterId
                        verifyDate = LocalDate(2010, 1, 1).toDate()
                        pointNo = 1
                        pointName = "Q1"
                        pointFlow = meter.q1
                        pointDev = meter.q1r
                    })
            }

            q2@ if (meter.q2 > meter.q1) {
                val p2 = ptList.find { it.pointFlow!!.minus(meter.q2).absoluteValue < 1E-3 }
                if (p2 != null) {
                    p2.pointNo = 2
                    p2.pointName = "Q2"
                } else
                    ptList.add(VcMeterVerifyPoint().apply {
                        meterId = meter.meterId
                        verifyDate = LocalDate(2010, 1, 1).toDate()
                        pointNo = 2
                        pointName = "Q2"
                        pointFlow = meter.q2
                        pointDev = meter.q2r
                    })
            }

            q3@ if (meter.q3 > meter.q2) {
                val p3 = ptList.find { it.pointFlow!!.minus(meter.q3).absoluteValue < 1E-3 }
                if (p3 != null) {
                    p3.pointNo = 3
                    p3.pointName = "Q3"
                } else
                    ptList.add(VcMeterVerifyPoint().apply {
                        meterId = meter.meterId
                        verifyDate = LocalDate(2010, 1, 1).toDate()
                        pointNo = 3
                        pointName = "Q3"
                        pointFlow = meter.q3
                        pointDev = meter.q3r
                    })
            }

            q4@ if (meter.q4 > meter.q1) {
                val p4 = ptList.find { it.pointFlow!!.minus(meter.q4).absoluteValue < 1E-3 }
                if (p4 != null) {
                    p4.pointNo = 4
                    p4.pointName = "Q4"
                } else
                    ptList.add(VcMeterVerifyPoint().apply {
                        meterId = meter.meterId
                        verifyDate = LocalDate(2010, 1, 1).toDate()
                        pointNo = 4
                        pointName = "Q4"
                        pointFlow = meter.q4
                        pointDev = meter.q4r
                    })
            }
        }

        // use q1/2/3 from meter-info if no verify-result
        if (ptList.size < 3
                || ptList.find { it.pointName?.equals("Q2", true) == true } == null
                || ptList.find { it.pointName?.equals("Q3", true) == true } == null
                || meter.modelPointList?.size ?: 0 < 3
                || meter.modelPointList?.find { it.pointName?.equals("Q2", true) == true } == null
                || meter.modelPointList?.find { it.pointName?.equals("Q3", true) == true } == null) {
            lgr.error("标准流量点不足3个或Q2/Q3不存在: ${meter.meterId} (${meter.meterName})")
            return false
        }

        meter.pointList = ptList.sortedBy { it.pointFlow }.toList()
        return true
    }

    /**
     * fill point-list with decay-template.
     */
    private fun fillPointListFromDecay(meter: ZoneMeter): ArrayList<VcMeterVerifyPoint> {
        val ptList = arrayListOf<VcMeterVerifyPoint>()

        effMapper!!.selectEffDecay(EffParam().apply {
            decayId = meter.decayId
        }).firstOrNull()?.also { decay ->
            if (decay.q1 ?: 0.0 > 1E-3) {
                meter.q1 = decay.q1!!
                meter.q1r = decay.q1r ?: 0.0
            }
            if (decay.q2 ?: 0.0 > meter.q1) {
                meter.q2 = decay.q2!!
                meter.q2r = decay.q2r ?: 0.0
            }
            if (decay.q3 ?: 0.0 > meter.q2) {
                meter.q3 = decay.q3!!
                meter.q3r = decay.q3r ?: 0.0
            }
            if (decay.q4 ?: 0.0 > meter.q3) {
                meter.q4 = decay.q4!!
                meter.q4r = decay.q4r ?: 0.0
            }

            qs1@ if (decay.qs1 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 11
                    pointName = "QS1"
                    pointFlow = decay.qs1
                    pointDev = decay.qs1r
                })
            }
            qs2@ if (decay.qs2 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 12
                    pointName = "QS2"
                    pointFlow = decay.qs2
                    pointDev = decay.qs2r
                })
            }
            qs3@ if (decay.qs3 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 13
                    pointName = "QS3"
                    pointFlow = decay.qs3
                    pointDev = decay.qs3r
                })
            }
            qs4@ if (decay.qs4 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 14
                    pointName = "QS4"
                    pointFlow = decay.qs4
                    pointDev = decay.qs4r
                })
            }
            qs5@ if (decay.qs5 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 15
                    pointName = "QS5"
                    pointFlow = decay.qs5
                    pointDev = decay.qs5r
                })
            }
            qs6@ if (decay.qs6 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 16
                    pointName = "QS6"
                    pointFlow = decay.qs6
                    pointDev = decay.qs6r
                })
            }
            qs7@ if (decay.qs7 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 17
                    pointName = "QS7"
                    pointFlow = decay.qs7
                    pointDev = decay.qs7r
                })
            }
            qs8@ if (decay.qs8 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 18
                    pointName = "QS8"
                    pointFlow = decay.qs8
                    pointDev = decay.qs8r
                })
            }
            qs9@ if (decay.qs9 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 19
                    pointName = "QS9"
                    pointFlow = decay.qs9
                    pointDev = decay.qs9r
                })
            }
            qs10@ if (decay.qs10 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 20
                    pointName = "QS10"
                    pointFlow = decay.qs10
                    pointDev = decay.qs10r
                })
            }
        }

        return ptList
    }

    /**
     * fill eff-meter for decay
     * ['0', 0, '0'] default for all meters.
     */
    fun fillDecay(meter: BwMeter) {
        // if there's decay
        effMapper!!.selectEffDecay(EffParam().apply {
            meterBrandId = meter.meterBrandId
            sizeId = meter.sizeId
            modelSize = meter.modelSize
        }).firstOrNull()?.also {
            meter.effDecay = it
        }

        if (meter.effDecay == null) {
            effMapper!!.selectEffDecay(EffParam().apply {
                meterBrandId = "0"
                sizeId = meter.sizeId
                modelSize = "0"
            }).firstOrNull()?.also {
                meter.effDecay = it
            }
        }

        if (meter.effDecay == null) {
            effMapper!!.selectEffDecay(EffParam().apply {
                meterBrandId = "0"
                sizeId = 0
                modelSize = "0"
            }).firstOrNull()?.also {
                meter.effDecay = it
            }
        }
    }

    fun effMeter(meter: BwMeter, task: EffTask, days: Int? = null): List<EffMeter> {
        val timeRange = dataMapper!!.realtimeDateRange(DataParam().apply {
            meterId = meter.meterId
        }).firstOrNull()?.also {
            if (it.maxDateTime?.isAfterNow == true) it.maxDateTime = DateTime.now()
            if (it.minDateTime?.isAfterNow == true) it.minDateTime = DateTime.now()
        } ?: return emptyList()

        val effRange = if (days ?: 0 > 0)
            effMapper!!.listEffRange(EffParam().apply {
                meterId = meter.meterId
            }).firstOrNull()?.also {
                // change with days
                it.minDateTime = it.maxDateTime!!.minusDays(1)
                it.maxDateTime = it.maxDateTime!!.plusDays(days!!)
            } ?: DataRange().also {
                it.meterId = meter.meterId
                it.minTime = timeRange.minTime
                it.maxDateTime = timeRange.minDateTime!!.plusDays(days!!)
            }
        else
        // change with task
            DataRange().apply {
                meterId = meter.meterId
                minDateTime = DateTime(task.taskStart!!)
                maxDateTime = DateTime(task.taskEnd!!)
            }

        effRange.also {
            if (it.minDateTime!!.isBefore(timeRange.minDateTime!!)) {
                it.minDateTime = timeRange.minDateTime
            }
            if (it.maxDateTime!!.isAfter(timeRange.maxDateTime!!)) {
                it.maxDateTime = timeRange.maxDateTime
            }
        }

        return effMeterRange(meter, effRange.minDateTime!!, effRange.maxDateTime!!, task)
    }

    fun effMeterRange(meter: BwMeter, startDay: DateTime, endDay: DateTime, task: EffTask): List<EffMeter> {
        val dlist = dataMapper!!.selectMeterRealtime(DataParam().apply {
            meterId = meter.meterId
            sampleTime1 = startDay.toDate()
            sampleTime2 = endDay.toDate()
            rows = 20000
        })

        if (dlist.isEmpty()) {
            lgr.info("empty realtime for {}", meter.meterId)
        }

        val retList = arrayListOf<EffMeter>()
        var day1 = startDay.withTimeAtStartOfDay()
        while (day1.isBefore(endDay)) {
            val eff = EffMeter().apply {
                meterId = meter.meterId
                meterName = meter.meterName
                taskId = task.taskId
                taskName = task.taskName
                periodTypeObj = EffPeriodType.Day
                taskStart = day1.toDate()
                taskEnd = day1.plusDays(1).toDate()

                meterBrandId = meter.meterBrandId ?: "0"
                sizeId = meter.sizeId ?: 0
                sizeName = meter.sizeName ?: "0"
                modelSize = meter.modelSize

                qr1 = meter.q1r
                qr2 = meter.q2r
                qr3 = meter.q3r
                qr4 = meter.q4r

                q3 = meter.q3
                q4 = meter.q4
                if (meter.q1 > 1E-3 && meter.q2 > meter.q1) {
                    q3toq1 = meter.q3 / meter.q1
                    q2toq1 = meter.q2 / meter.q1
                }
                if (meter.q3 > meter.q2) {
                    q4toq3 = meter.q4 / meter.q3
                }
            }

            if (effSingleMeterDay(meter, day1, dlist, eff)) {
                val param = EffParam().apply {
                    meterId = meter.meterId
                    periodType = "%"
                    taskStart = day1.toDate()
                }
                lgr.info("remove eff meter: {}/{}",
                        effMapper!!.deleteEffPoint(param),
                        effMapper!!.deleteEffMeter(param)
                )

                val cntEff = effMapper!!.insertEffMeterSingle(eff)
                val pp = EffParam().apply {
                    pointEffList = eff.pointEffList?.plus(eff.modelPointList.orEmpty())
                    pointEffList?.forEach { it.effId = eff.effId }
                }
                val cntPt = effMapper!!.insertEffPoint(pp)
                lgr.info("insert eff meter: {}/{} @ {} / {}", cntEff, cntPt, eff.meterId, LocalDate(eff.taskStart))

                retList.add(eff)
            }

            day1 = day1.plusDays(1)
        }

        return retList
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(EffTaskBean::class.java)

        val DUMMY_START = LocalDate(2010, 1, 1)
        val DUMMY_END = LocalDate(2010, 1, 2)

        /**
         * analyze meter-eff for single day.
         * @param meter
         * @param day
         * @param eff
         */
        fun effSingleMeterDay(meter: BwMeter, day: DateTime, dataList: List<BwData>, eff: EffMeter): Boolean {
            if (meter.meterId.isNullOrBlank() || meter.pointList?.size ?: 0 < 3
                    || meter.modelPointList?.size ?: 0 < 3) {
                throw IllegalArgumentException("计量点不足3个或Q2/Q3不存在: ${meter.meterId}")
            }
            if (day.withTimeAtStartOfDay() != day) {
                throw IllegalArgumentException("不能包含时间部分: ${day.toString(ISODateTimeFormat.basicDateTime())}")
            }
            eff.pointList = meter.pointList!!.sortedBy { it.pointFlow }
            eff.pointList!!.forEach {
                if (it.pointFlow == null) {
                    throw IllegalArgumentException("计量点不能为空")
                }
            }

            // decayed eff
            eff.effDecay = meter.effDecay

            val dlist = dataList.dropWhile { it.jodaSample?.withTimeAtStartOfDay() != day }
            if (dlist.size < 2) {
                lgr.warn("not enough data for ${meter.meterId} in ${day.toString(ISODateTimeFormat.basicDateTime())}")
                return false
            }
            dlist.forEach {
                if (it.sampleTime == null) {
                    throw IllegalArgumentException("采样时间不能为空: ${meter.meterId}")
                }
            }

            eff.apply {
                runTime = Date()
            }

            eff.pointEffList = eff.pointList!!.map {
                EffMeterPoint().apply {
                    taskId = eff.taskId
                    effId = eff.effId
                    taskStart = day.toDate()
                    taskEnd = day.plusDays(1).toDate()
                    meterId = meter.meterId
                    pointTypeObj = EffPointType.EFF
                    periodTypeObj = EffPeriodType.Day

                    pointNo = it.pointNo
                    pointName = it.pointName
                    pointFlow = it.pointFlow
                    pointDev = it.pointDev
                    highLimit = it.highLimit
                    lowLimit = it.lowLimit

                    pointWater = 0.0
                    realWater = 0.0
                }
            }.plus(EffMeterPoint().apply {
                taskId = eff.taskId
                effId = eff.effId
                taskStart = day.toDate()
                taskEnd = day.plusDays(1).toDate()
                meterId = meter.meterId
                pointTypeObj = EffPointType.EFF
                periodTypeObj = EffPeriodType.Day

                pointNo = 21
                pointName = "Q21"
                pointFlow = Double.MAX_VALUE
                pointDev = 1.0
                highLimit = 0.0
                lowLimit = 0.0

                pointWater = 0.0
                realWater = 0.0
            })

            eff.modelPointList = meter.modelPointList!!.map {
                EffMeterPoint().apply {
                    taskId = eff.taskId
                    effId = eff.effId
                    taskStart = day.toDate()
                    taskEnd = day.plusDays(1).toDate()
                    meterId = meter.meterId
                    pointTypeObj = EffPointType.MODEL
                    periodTypeObj = EffPeriodType.Day

                    pointNo = it.pointNo
                    pointName = it.pointName
                    pointFlow = it.pointFlow
                    pointDev = it.pointDev
                    highLimit = it.highLimit
                    lowLimit = it.lowLimit

                    pointWater = 0.0
                    realWater = 0.0
                }
            }.plus(EffMeterPoint().apply {
                taskId = eff.taskId
                effId = eff.effId
                taskStart = day.toDate()
                taskEnd = day.plusDays(1).toDate()
                meterId = meter.meterId
                pointTypeObj = EffPointType.MODEL
                periodTypeObj = EffPeriodType.Day

                pointNo = 21
                pointName = "Q4+"
                pointFlow = Double.MAX_VALUE
                pointDev = 1.0
                highLimit = 0.0
                lowLimit = 0.0

                pointWater = 0.0
                realWater = 0.0
            })

            eff.startFwd = dlist.first().forwardReading
            for (idx in 1.until(dlist.size)) {
                val d1 = dlist[idx - 1]
                val d2 = dlist[idx]

                d1.forwardSum = d2.forwardReading?.minus(d1.forwardReading ?: 0.0) ?: 0.0
                // divided by 0?
                d1.avgFlow = d1.forwardSum?.div(
                        Duration(DateTime(d1.sampleTime), DateTime(d2.sampleTime)).standardSeconds.div(3600.0))

                eff.pointEffList!!.firstOrNull { d1.avgFlow!!.absoluteValue <= it.pointFlow!! }?.also { ep ->
                    ep.pointWater = ep.pointWater!!.plus(d1.forwardSum!!)
                }
                eff.modelPointList!!.firstOrNull { d1.avgFlow!!.absoluteValue <= it.pointFlow!! }?.also { mp ->
                    mp.pointWater = mp.pointWater!!.plus(d1.forwardSum!!)
                }

                // break if not same day
                if (DateTime(d2.sampleTime!!).dayOfMonth != DateTime(d1.sampleTime!!).dayOfMonth) {
                    eff.endFwd = d2.forwardReading
                    eff.dataRows = idx
                    break
                }
            }
            if (eff.endFwd == null) {
                eff.endFwd = dlist.last().forwardReading
            }

            eff.pointEffList!!.forEach {
                it.realWater = it.pointWater!! - it.pointWater!!.times(it.pointDev!!).div(100.0)
            }

            // to avoid numeric overflow
            eff.pointEffList!!.also {
                it.last().pointFlow = it[it.size - 2].pointFlow?.times(2.0)
            }
            eff.modelPointList!!.also {
                it.last().pointFlow = it[it.size - 2].pointFlow?.times(2.0)
            }

            eff.apply {
                runDuration = Duration(DateTime(runTime!!), DateTime.now()).millis.toInt()
                meterWater = pointEffList!!.sumByDouble { it.pointWater!! }
                realWater = pointEffList!!.sumByDouble { it.realWater!! }

                // to avoid dividen-by-0 when matching match.
                if (meterWater ?: 0.0 < 1.0E-3) meterWater = 1.0E-3
                if (realWater ?: 0.0 < 1.0E-3) realWater = 1.0E-3

                meterEff = if (realWater!! > 1.0E-3) {
                    meterWater!!.div(realWater!!).times(100.0)
                } else 100.0

                // decayed eff
                decayEff = if (eff.effDecay?.decayEff == null)
                    String.format("%.3f", meterEff)
                else {
                    var tfwd = effDecay!!.totalFwd ?: 1.0E6
                    if (tfwd < 1.0E5) tfwd = 1.0E6
                    val decay = endFwd?.div(tfwd)?.times(effDecay!!.decayEff?.div(100.0) ?: 0.0) ?: 0.0
                    String.format("%.3f", meterEff?.minus(decay) ?: 0.0)
                }

                q0v = pointEffList?.getOrNull(0)?.pointWater
                q1v = pointEffList?.find { it.pointName?.equals("Q1", true) == true }?.pointWater ?: 0.0
                q2v = pointEffList?.find { it.pointName?.equals("Q2", true) == true }?.pointWater ?: 0.0
                q3v = pointEffList?.find { it.pointName?.equals("Q3", true) == true }?.pointWater ?: 0.0
                q4v = pointEffList?.getOrNull(4)?.pointWater
                qtv = pointEffList?.drop(5)?.sumByDouble { it.pointWater!! }

                taskResult = ""
            }

            return true
        }
    }
}