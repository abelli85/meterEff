package com.abel.bigwater.impl

import com.abel.bigwater.api.DataParam
import com.abel.bigwater.api.EffParam
import com.abel.bigwater.api.MeterParam
import com.abel.bigwater.mapper.ConfigMapper
import com.abel.bigwater.mapper.DataMapper
import com.abel.bigwater.mapper.EffMapper
import com.abel.bigwater.mapper.MeterMapper
import com.abel.bigwater.model.*
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
import smile.clustering.GMeans
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue

@Component
@EnableScheduling
open class EffTaskBean {

    @Autowired
    private var configMapper: ConfigMapper? = null

    @Autowired
    private var meterMapper: MeterMapper? = null

    @Autowired
    private var dataMapper: DataMapper? = null

    @Autowired
    private var effMapper: EffMapper? = null

    @Autowired
    private var cronConfig: EffTaskConfig? = null

    @Scheduled(cron = "0 15 12,20,2 * * ?")
    fun effAll() {
        lgr.info("定时任务: 分析所有水司的水表计量效率...")
        val now = LocalDateTime.now().atZone(ZoneId.of("Asia/Shanghai"))
        if (now.hour in 5..19) {
            if (cronConfig?.cronDaytime != true) {
                lgr.info("白天不执行分析任务~")
                return
            } else {
                lgr.warn("白天启动计量效率分析任务...")
            }
        }
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
        }).sortedBy { it.powerType }.forEach {
            if (!fillPointList(it)) {
                lgr.warn("计量点不足3个或Q2/Q3不存在: {}/{}", it.meterId, it.meterName)
                return@forEach
            }
            lgr.info("fill-point-list result: {}", JSON.toJSONString(it, true))

            if (it.powerTypeObj != PowerType.MANUAL) {
                val effList = effMeter(it, task, 183)
                lgr.info("analyze {} eff for {}/{} in {}", effList.size,
                        it.meterId, it.meterName, firm.title)
                if (effList.isEmpty()) return@forEach

                val drange = DataRange().apply {
                    meterId = it.meterId
                    minTime = effList.minOf { e1 -> e1.taskStart!! }
                    maxTime = effList.maxOf { e1 -> e1.taskEnd!! }
                }

                // both month & year
                buildMonthEff(drange)
                buildYearEff(drange)
            } else {
                val effList = effMeter(it, task)
                lgr.info("analyze {} eff for {}/{} in {}", effList.size,
                        it.meterId, it.meterName, firm.title)

                if (effList.isEmpty()) return@forEach

                // only year for manual
                val drange = DataRange().apply {
                    meterId = it.meterId
                    minTime = effList.minOf { e1 -> e1.taskStart!! }
                    maxTime = effList.maxOf { e1 -> e1.taskEnd!! }
                }
                buildYearEff(drange)
            }
        }
    }

    /**
     * build monthly / annual eff from daily.
     */
    open fun buildMonthEff(drange: DataRange) {
        if (drange.meterId.isNullOrBlank() || drange.minTime == null) {
            lgr.error("buildMonthEff: meterId SHOULD BE NON-NULL!")
            throw IllegalArgumentException("buildYearEff: meterId SHOULD BE NON-NULL!")
        }
        val pmth = EffParam().apply {
            meterId = drange.meterId
            periodTypeObj = EffPeriodType.Month
            taskStart = drange.minTime
            taskEnd = drange.maxTime
        }

        // truncate to month
        pmth.also {
            it.taskStart = it.jodaTaskStart?.withDayOfMonth(1)?.withTimeAtStartOfDay()?.toDate()
            it.taskEnd = it.jodaTaskEnd?.withDayOfMonth(1)?.withTimeAtStartOfDay()?.plusMonths(1)?.minusSeconds(1)?.toDate()

        }
        effMapper!!.deleteEffMeter(pmth)
        effMapper!!.deleteEffPoint(pmth)
        val cntEff = effMapper!!.buildEffMeterMonth(pmth)
        val cntPt = effMapper!!.buildEffPointMonth(pmth)
        lgr.info("build monthly eff: $cntEff / $cntPt")
    }

    /**
     * build monthly / annual eff from daily.
     */
    open fun buildYearEff(drange: DataRange) {
        if (drange.meterId.isNullOrBlank() || drange.minTime == null) {
            lgr.error("buildYearEff: meterId SHOULD BE NON-NULL!")
            throw IllegalArgumentException("buildYearEff: meterId SHOULD BE NON-NULL!")
        }
        val pmth = EffParam().apply {
            meterId = drange.meterId
            periodTypeObj = EffPeriodType.Year
            taskStart = drange.minDateTime!!.withDayOfYear(1).withTimeAtStartOfDay().toDate()
            taskEnd = drange.maxDateTime!!.withDayOfYear(1).withTimeAtStartOfDay().plusYears(1).minusSeconds(1).toDate()
        }
        effMapper!!.deleteEffMeter(pmth)
        effMapper!!.deleteEffPoint(pmth)
        val cntEffYear = effMapper!!.buildEffMeterYear(pmth)
        val cntPtYear = effMapper!!.buildEffPointYear(pmth)
        lgr.info("build annual eff: $cntEffYear / $cntPtYear")
    }

    /**
     * 填充计量点. 按照顺序:
     * @see ZoneMeter.decayId
     * @see VcMeterVerifyPoint
     * @see ZoneMeter.q1 - 2/3/4
     * Q4/Q3=1.25，Q2/Q1=1.6来确定过载流量Q4和分界流量Q2
     */
    fun fillPointList(meter: ZoneMeter): Boolean {
        var srcList = fillPointListFromVerify(meter)?.dropWhile { it.pointFlow == null }
        if (srcList.isNullOrEmpty()) {
            srcList = fillPointListFromDecay(meter).orEmpty()
            meter.srcErrorObj = SourceErrorType.DecayTemplate
        } else {
            meter.srcErrorObj = SourceErrorType.VerifyResult
        }
        srcList.forEach {
            if (it.pointDev == null) it.pointDev = 0.0
        }

        if (meter.q1 < 1E-3 || meter.q2 <= meter.q1 || meter.q3 <= meter.q2) return false

        // fill model-point-list after q1/2/3/4 are filled.
        fillModelPointList(meter)

        val ptList = ArrayList<VcMeterVerifyPoint>(srcList)

        kotlin.run {
            val p1 = ptList.find { it.pointFlow!!.minus(meter.q1).absoluteValue < 1E-2 }
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

        kotlin.run {
            val p2 = ptList.find { it.pointFlow!!.minus(meter.q2).absoluteValue < 1E-2 }
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

        kotlin.run {
            val p3 = ptList.find { it.pointFlow!!.minus(meter.q3).absoluteValue < 1E-2 }
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

        if (meter.q4 > meter.q3) {
            val p4 = ptList.find { it.pointFlow!!.minus(meter.q4).absoluteValue < 1E-2 }
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

        meter.pointList = ptList.sortedBy { it.pointFlow }.toList()
        return true
    }

    /**
     * fill point-list with verify.
     * Q4/Q3=1.25，Q2/Q1=1.6来确定过载流量Q4和分界流量Q2
     */
    private fun fillPointListFromVerify(meter: ZoneMeter): List<VcMeterVerifyPoint>? {
        meter.pointList = meterMapper!!.listVerifyPointLast(MeterParam().apply {
            meterId = meter.meterId
        })

        backPointToMeter(meter)

        return if (meter.q1 < 1E-3 || meter.q2 <= meter.q1 || meter.q3 <= meter.q2) null
        else meter.pointList
    }

    /**
     * fill point-list with decay-template.
     * Q4/Q3=1.25，Q2/Q1=1.6来确定过载流量Q4和分界流量Q2
     */
    private fun fillPointListFromDecay(meter: ZoneMeter): List<VcMeterVerifyPoint>? {
        effMapper!!.selectEffDecay(EffParam().apply {
            decayId = meter.decayId
            meterBrandId = meter.meterBrandId
            sizeId = meter.sizeId
            sizeName = meter.sizeName
            modelSize = meter.modelSize?.substringBefore('-')
        }).firstOrNull()?.also { decay ->
            meter.decayObj = decay

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

            if (meter.q1 < 1E-3 || meter.q2 <= meter.q1 || meter.q3 <= meter.q2) return null

            val ptList = arrayListOf<VcMeterVerifyPoint>()

            if (decay.qs1 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 11
                    pointName = "QS1"
                    pointFlow = decay.qs1
                    pointDev = decay.qs1r
                })
            }
            if (decay.qs2 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 12
                    pointName = "QS2"
                    pointFlow = decay.qs2
                    pointDev = decay.qs2r
                })
            }
            if (decay.qs3 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 13
                    pointName = "QS3"
                    pointFlow = decay.qs3
                    pointDev = decay.qs3r
                })
            }
            if (decay.qs4 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 14
                    pointName = "QS4"
                    pointFlow = decay.qs4
                    pointDev = decay.qs4r
                })
            }
            if (decay.qs5 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 15
                    pointName = "QS5"
                    pointFlow = decay.qs5
                    pointDev = decay.qs5r
                })
            }
            if (decay.qs6 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 16
                    pointName = "QS6"
                    pointFlow = decay.qs6
                    pointDev = decay.qs6r
                })
            }
            if (decay.qs7 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 17
                    pointName = "QS7"
                    pointFlow = decay.qs7
                    pointDev = decay.qs7r
                })
            }
            if (decay.qs8 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 18
                    pointName = "QS8"
                    pointFlow = decay.qs8
                    pointDev = decay.qs8r
                })
            }
            if (decay.qs9 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 19
                    pointName = "QS9"
                    pointFlow = decay.qs9
                    pointDev = decay.qs9r
                })
            }
            if (decay.qs10 ?: 0.0 > 0.0) {
                ptList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 20
                    pointName = "QS10"
                    pointFlow = decay.qs10
                    pointDev = decay.qs10r
                })
            }

            return ptList
        }

        return null
    }

    fun effMeter(meter: ZoneMeter, task: EffTask, days: Int? = null): List<EffMeter> {
        // find time-range firstly.
        val timeRange = dataMapper!!.realtimeDateRange(DataParam().apply {
            meterId = meter.meterId
        }).firstOrNull()?.also {
            if (it.maxDateTime?.isAfterNow == true) it.maxDateTime = DateTime.now()
            if (it.minDateTime?.isAfterNow == true) return emptyList()
        } ?: return emptyList()

        // if there're no eff-rows, then use time-range [min, min + days];
        // if there're eff-rows, then [eff.max - 1'day', eff.max + days].
        val effRange = if (days ?: 0 > 0)
            effMapper!!.listEffRange(EffParam().apply {
                meterId = meter.meterId
            }).firstOrNull()?.also {
                // change with days
                it.minDateTime = it.maxDateTime!!.minusDays(1)
                it.maxDateTime = it.maxDateTime!!.plusDays(days!!)
            } ?: DataRange().also {
                it.meterId = meter.meterId
                val lastyr = DateTime.now().withTimeAtStartOfDay().withDayOfYear(1).minusYears(1)
                it.minTime = if (timeRange.minDateTime!!.isBefore(lastyr))
                    lastyr.toDate()
                else timeRange.minTime
                it.maxDateTime = timeRange.minDateTime!!.plusDays(days!!)
            }
        else
        // change with task
            DataRange().apply {
                meterId = meter.meterId
                minDateTime = DateTime(task.taskStart!!)
                maxDateTime = DateTime(task.taskEnd!!)
            }

        // truncate to time-range: [min, max].
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

    fun effMeterRange(meter: ZoneMeter, startDay: DateTime, endDay: DateTime, task: EffTask): List<EffMeter> {
        val retList = arrayListOf<EffMeter>()
        var day1 = startDay.withTimeAtStartOfDay()
        while (day1.isBefore(endDay)) {
            val eff = EffMeter().apply {
                meterId = meter.meterId
                meterName = meter.meterName
                powerType = meter.powerType

                taskId = task.taskId
                taskName = task.taskName

                meterBrandId = meter.meterBrandId ?: "0"
                sizeId = meter.sizeId ?: 0
                sizeName = meter.sizeName ?: "0"
                modelSize = meter.modelSize
                srcError = meter.srcError

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

            if (meter.powerTypeObj == PowerType.MANUAL) {
                // truncate to month
                day1 = day1.withDayOfMonth(1)

                eff.apply {
                    periodTypeObj = EffPeriodType.Month
                    powerTypeObj = meter.powerTypeObj
                    taskStart = day1.toDate()
                    taskEnd = day1.plusMonths(1).toDate()
                }

                val dlist = dataMapper!!.selectMeterRealtime(DataParam().apply {
                    meterId = meter.meterId
                    sampleTime1 = day1.toDate()
                    sampleTime2 = day1.plusMonths(3).toDate()
                    rows = 20000
                })
                if (dlist.isEmpty()) eff.taskResult = EffFailureType.DATA.name

                if (dlist.isNotEmpty() && effMeterManual(meter, day1, dlist, eff)) {
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
                } else {
                    // failure
                    val param = EffParam().apply {
                        meterId = meter.meterId
                        periodType = "%"
                        taskStart = day1.toDate()
                    }
                    lgr.info("remove eff failure: {}/{}",
                            effMapper!!.deleteEffPoint(param),
                            effMapper!!.deleteEffFailure(param)
                    )

                    val cntEff = effMapper!!.insertEffFailureSingle(eff)
                    lgr.info("insert eff failure: {}@ {} / {}", cntEff, eff.meterId, LocalDate(eff.taskStart))

                    retList.add(eff)
                }

                day1 = day1.plusMonths(1)
                continue
            }

            eff.apply {
                periodTypeObj = EffPeriodType.Day
                taskStart = day1.toDate()
                taskEnd = day1.plusDays(1).toDate()
            }

            val dlist = dataMapper!!.selectMeterRealtime(DataParam().apply {
                meterId = meter.meterId
                sampleTime1 = day1.toDate()
                sampleTime2 = day1.plusMonths(1).plusHours(1).toDate()
                rows = 20000
            })
            if (dlist.isEmpty()) eff.taskResult = EffFailureType.DATA.name

            if (dlist.isNotEmpty() && effSingleMeterDay(meter, day1, dlist, eff)) {
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
            } else {
                // failure
                val param = EffParam().apply {
                    meterId = meter.meterId
                    periodType = "%"
                    taskStart = day1.toDate()
                }
                lgr.info("remove eff failure: {}/{}",
                        effMapper!!.deleteEffPoint(param),
                        effMapper!!.deleteEffFailure(param)
                )

                val cntEff = effMapper!!.insertEffFailureSingle(eff)
                lgr.info("insert eff failure: {}@ {} / {}", cntEff, eff.meterId, LocalDate(eff.taskStart))

                retList.add(eff)
            }

            day1 = day1.plusDays(1)
        }

        return retList
    }

    fun effMeterManual(meter: ZoneMeter, day: DateTime, dataList: List<BwData>, eff: EffMeter): Boolean {
        if (meter.meterId.isNullOrBlank() || meter.pointList?.size ?: 0 < 3
                || meter.modelPointList?.size ?: 0 < 3) {
            lgr.error("计量点不足3个或Q2/Q3不存在: ${meter.meterId}")
            eff.taskResult = EffFailureType.ABSENT_POINT.name
            return false
        }
        if (eff.periodTypeObj != EffPeriodType.Month || day.withTimeAtStartOfDay().withDayOfMonth(1) != day) {
            lgr.error("必须为当月1日: ${day.toString(ISODateTimeFormat.basicDateTime())}")
            eff.taskResult = EffFailureType.ABSENT_TIME.name
            return false
        }

        // decayed eff
        eff.decayObj = meter.decayObj

        eff.apply {
            runTime = Date()
        }

        if (!convertMeterEffPoint(eff, meter)) return false

        val dstart = dataList.firstOrNull { it.jodaSample?.monthOfYear == day.monthOfYear }
        val dend = dataList.firstOrNull { it.jodaSample?.monthOfYear ?: 0 != day.monthOfYear }
        eff.also {
            it.startTime = dstart?.sampleTime
            it.endTime = dend?.sampleTime
            it.startFwd = dstart?.forwardReading
            it.endFwd = dend?.forwardReading
        }

        if (dstart == null || dend == null) {
            lgr.warn("not enough data for ${meter.meterId} in ${day.toString(ISODateTimeFormat.basicDateTime())}")
            eff.taskResult = EffFailureType.DATA_LESS.name
            return false
        }
        if (dstart.sampleTime == null || dend.sampleTime == null) {
            lgr.error("采样时间不能为空: ${meter.meterId}")
            eff.taskResult = EffFailureType.ABSENT_TIME.name
            return false
        }

        eff.stdDays = Duration(DateTime(eff.startTime), DateTime(eff.endTime)).standardSeconds.toDouble().div(24 * 3600)
//        eff.dataRows = dataList.indexOf(dend) - dataList.indexOf(dstart)
        eff.meterWater = (eff.endFwd ?: 0.0) - (eff.startFwd ?: 0.0)

        val monthParam = EffParam().apply {
            firmId = meter.firmId?.plus('%')
            pointTypeObj = EffPointType.EFF
            lowDayConsume = eff.meterWater!!.div(eff.stdDays!!).times(0.9)
            highDayConsume = eff.meterWater!!.div(eff.stdDays!!).times(1.1)
        }
        val pteList = effMapper!!.statEffPointManual(monthParam)
        val modelList = effMapper!!.statEffPointManual(monthParam.also {
            it.pointTypeObj = EffPointType.MODEL
        })

        if (pteList.isNullOrEmpty() || modelList.isNullOrEmpty()) {
            eff.taskResult = EffFailureType.ABSENT_LIKE.name
            lgr.error("${EffFailureType.ABSENT_LIKE}: ${meter.meterId}")
            return false
        }

        // 先使用相近远传表
        eff.dataRows = pteList[0].dataRows ?: 0

        val ratioEff = eff.meterWater!! / pteList.sumByDouble { it.pointWater ?: 0.0 }
        eff.pointEffList!!.forEach {
            val lk = pteList.find { p1 -> p1.pointNo == it.pointNo }
            if (lk == null) {
                lgr.warn("LIKE absent pointNo: ${it.pointNo} / ${it.pointName}")
                eff.taskResult = EffFailureType.ABSENT_LIKE.name
                return false
            }

            it.periodTypeObj = EffPeriodType.Month

            it.pointWater = (lk.pointWater ?: 0.0).times(ratioEff)
            it.realWater = it.pointWater!! - it.pointWater!!.times(it.pointDev ?: 0.0).div(100.0)
        }
        eff.pointEffList!!.last().also {
            if ((it.pointFlow ?: 0.0) > 1.0E12) {
                val q3pt = eff.pointEffList!!.takeLast(2).firstOrNull()
                it.pointFlow = q3pt?.pointFlow?.times(2.0)
            }
        }

        // 先使用相近远传表
        val ratioModel = eff.meterWater!! / modelList.sumByDouble { it.pointWater ?: 0.0 }
        eff.modelPointList!!.forEach {
            val lk = modelList.find { p1 -> p1.pointNo == it.pointNo }
            if (lk == null) {
                lgr.warn("LIKE absent pointNo: ${it.pointNo} / ${it.pointName}")
                eff.taskResult = EffFailureType.ABSENT_LIKE.name
                return false
            }

            it.periodTypeObj = EffPeriodType.Month
            it.pointWater = (lk.pointWater ?: 0.0).times(ratioModel)
        }
        eff.modelPointList!!.last().also {
            if ((it.pointFlow ?: 0.0) > 1.0E12) {
                val q3pt = eff.pointEffList!!.takeLast(2).firstOrNull()
                it.pointFlow = q3pt?.pointFlow?.times(2.0)
            }
        }

        eff.apply {
            runDuration = Duration(DateTime(runTime!!), DateTime.now()).millis.toInt()
            realWater = pointEffList!!.sumByDouble { it.realWater!! }

            // to avoid dividen-by-0 when matching match.
            if (meterWater ?: 0.0 < 1.0E-3) meterWater = 1.0E-3
            if (realWater ?: 0.0 < 1.0E-3) realWater = 1.0E-3

            meterEff = if (realWater!! > 1.0E-3) {
                meterWater!!.div(realWater!!).times(100.0)
            } else 100.0

            // decayed eff
            decayEff = if (eff.decayObj?.decayEff == null)
                String.format("%.3f", meterEff)
            else {
                var tfwd = decayObj!!.totalFwd ?: 1.0E6
                if (tfwd < 1.0E5) tfwd = 1.0E6
                val decay = endFwd?.div(tfwd)?.times(decayObj!!.decayEff?.div(100.0) ?: 0.0) ?: 0.0
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

    /**
     * learn week use-model.
     */
    fun learnMeter(meter: ZoneMeter, task: EffTask, weeks: Int? = null): List<EffMeter> {
        val timeRange = dataMapper!!.realtimeDateRange(DataParam().apply {
            meterId = meter.meterId
        }).firstOrNull()?.also {
            if (it.maxDateTime?.isAfterNow == true) it.maxDateTime = DateTime.now()
            if (it.minDateTime?.isAfterNow == true) return emptyList()
        } ?: return emptyList()

        // if there're no eff-rows, then use time-range [min, min + days];
        // if there're eff-rows, then [eff.max - 1'day', eff.max + days].
        val effRange = if (task.taskStart == null || task.taskEnd == null)
            effMapper!!.listEffRange(EffParam().apply {
                meterId = meter.meterId
                periodTypeObj = EffPeriodType.Week
                pointTypeObj = EffPointType.LEARN
            }).firstOrNull()?.also {
                // change with days
                it.minDateTime = it.maxDateTime!!.withTimeAtStartOfDay().withDayOfWeek(1)
                it.maxDateTime = it.maxDateTime!!.withTimeAtStartOfDay().withDayOfWeek(1).plusWeeks(weeks ?: 4)
            } ?: DataRange().also {
                it.meterId = meter.meterId
                it.minDateTime = timeRange.minDateTime!!.withTimeAtStartOfDay().withDayOfWeek(1)
                it.maxDateTime = timeRange.minDateTime!!.withTimeAtStartOfDay().withDayOfWeek(1).plusWeeks(weeks ?: 4)
            }
        else
        // change with task
            DataRange().apply {
                meterId = meter.meterId
                minDateTime = DateTime(task.taskStart!!)
                maxDateTime = DateTime(task.taskEnd!!)
            }

        // truncate to time-range: [min, max].
        effRange.also {
            if (it.minDateTime!!.isBefore(timeRange.minDateTime!!)) {
                it.minDateTime = timeRange.minDateTime
            }
            if (it.maxDateTime!!.isAfter(timeRange.maxDateTime!!)) {
                it.maxDateTime = timeRange.maxDateTime
            }
        }

        val retList = ArrayList<EffMeter>()
        var day1 = effRange.minDateTime!!.withTimeAtStartOfDay().withDayOfWeek(1)
        while (day1.isBefore(effRange.maxDateTime)) {
            // truncate to week
//            day1 = day1.withDayOfWeek(1)

            val eff = EffMeter().apply {
                meterId = meter.meterId
                meterName = meter.meterName
                taskId = task.taskId
                taskName = task.taskName

                meterBrandId = meter.meterBrandId ?: "0"
                sizeId = meter.sizeId ?: 0
                sizeName = meter.sizeName ?: "0"
                modelSize = meter.modelSize
                srcErrorObj = meter.srcErrorObj

                periodTypeObj = EffPeriodType.Week
                powerTypeObj = meter.powerTypeObj
                taskStart = day1.toDate()
                taskEnd = day1.plusWeeks(1).toDate()

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

            val dlistReal = dataMapper!!.selectMeterRealtime(DataParam().apply {
                meterId = meter.meterId
                sampleTime1 = eff.taskStart
                sampleTime2 = eff.taskEnd
                rows = 20000
            })
            for (idx in 1.until(dlistReal.size)) {
                val d1 = dlistReal[idx - 1]
                val d2 = dlistReal[idx]

                d1.forwardSum = d2.forwardReading?.minus(d1.forwardReading ?: 0.0) ?: 0.0
                // divided by 0?
                d1.avgFlow = d1.forwardSum?.div(
                        Duration(DateTime(d1.sampleTime), DateTime(d2.sampleTime)).standardSeconds.div(3600.0))
            }
            val dlist = dlistReal.dropWhile { it.avgFlow ?: 0.0 < 1.0E-3 }

            if (dlistReal.isEmpty()) {
                eff.taskResult = EffFailureType.DEVICE_OFFLINE.name
            } else if (dlist.size < 20) {
                eff.taskResult = EffFailureType.DATA_LESS.name
            } else {
                eff.also {
                    it.startFwd = dlist.firstOrNull()?.forwardReading
                    it.startTime = dlist.firstOrNull()?.sampleTime
                    it.endFwd = dlist.lastOrNull()?.forwardReading
                    it.endTime = dlist.lastOrNull()?.sampleTime
                    it.stdDays = Duration(DateTime(it.startTime), DateTime(it.endTime)).standardSeconds.toDouble().div(24 * 3600)
                    it.meterWater = it.endFwd?.minus(it.startFwd ?: 0.0)

                    // to avoid dividen-by-0 when matching match.
                    if (it.meterWater ?: 0.0 < 1.0E-3) {
                        it.taskResult = EffFailureType.DATA.name;

                        it.meterWater = 1.0E-3
                    }

                    it.runTime = Date()
                }

                if (eff.taskResult.isNullOrBlank()) {

                    try {
                        if (!gmeansModel(dlist, eff)) {
                            eff.taskResult = EffFailureType.DATA.name
                        }
                    } catch (ex: Exception) {
                        eff.taskResult = EffFailureType.OTHER.name
                        eff.decayEff = ex.message?.take(18)
                    }
                }

                eff.apply {
                    runDuration = Duration(DateTime(runTime!!), DateTime.now()).millis.toInt()
                }
            }

            // save to database.
            val param = EffParam().apply {
                meterId = meter.meterId
                periodTypeObj = eff.periodTypeObj
                pointTypeObj = EffPointType.LEARN
                taskStart = day1.toDate()
            }
            lgr.info("remove eff point/meter/failure: {}/{}/{}",
                    effMapper!!.deleteEffPoint(param),
                    effMapper!!.deleteEffMeter(param),
                    effMapper!!.deleteEffFailure(param)
            )

            if (eff.taskResult.isNullOrBlank()) {
                val cntEff = effMapper!!.insertEffMeterSingle(eff)

                val pp = EffParam().apply {
                    pointEffList = eff.modelPointList
                    pointEffList?.forEach { it.effId = eff.effId }
                }
                val cntPt = effMapper!!.insertEffPoint(pp)
                lgr.info("insert eff meter: {}/{} @ {} / {}", cntEff, cntPt, eff.meterId, LocalDate(eff.taskStart))
            } else {
                effMapper!!.insertEffFailureSingle(eff)
                lgr.error("fail to learn weekly modeal caused by {} / {} @ {}",
                        eff.taskResult, eff.meterId, LocalDate(eff.taskStart))
            }

            retList.add(eff)

            day1 = day1.plusWeeks(1)
        }

        return retList
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(EffTaskBean::class.java)

        val DUMMY_START = LocalDate(2010, 1, 1)
        val DUMMY_END = LocalDate(2010, 1, 2)

        /**
         * back point-list to meter.q1/2/3/4.
         */
        fun backPointToMeter(meter: ZoneMeter) {
            if (!meter.pointList.isNullOrEmpty()) {
                meter.pointList!!.firstOrNull { it.pointName.equals("Q1", true) }?.also {
                    meter.q1 = it.pointFlow ?: 0.0
                    meter.q1r = it.pointDev ?: 0.0
                }
                meter.pointList!!.firstOrNull { it.pointName.equals("Q2", true) }?.also {
                    meter.q2 = it.pointFlow ?: 0.0
                    meter.q2r = it.pointDev ?: 0.0
                }
                meter.pointList!!.firstOrNull { it.pointName.equals("Q3", true) }?.also {
                    meter.q3 = it.pointFlow ?: 0.0
                    meter.q3r = it.pointDev ?: 0.0
                }
                meter.pointList!!.firstOrNull { it.pointName.equals("Q4", true) }?.also {
                    meter.q4 = it.pointFlow ?: 0.0
                    meter.q4r = it.pointDev ?: 0.0
                }
            }
        }

        /**
         * fill model-point-list, not for efficiency.
         * Q4/Q3=1.25，Q2/Q1=1.6来确定过载流量Q4和分界流量Q2
         */
        fun fillModelPointList(meter: ZoneMeter) {
            if (meter.q1 < 1E-3 || meter.q2 <= meter.q1 || meter.q3 <= meter.q2) {
                throw IllegalArgumentException("invalid Q1/Q2/Q3/Q4")
            }

            val mpList = arrayListOf<VcMeterVerifyPoint>()
            kotlin.run {
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

            kotlin.run {
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

            kotlin.run {
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

            kotlin.run {
                // model point.
                mpList.add(VcMeterVerifyPoint().apply {
                    meterId = meter.meterId
                    verifyDate = LocalDate(2010, 1, 1).toDate()
                    pointNo = 10
                    pointName = "Q4"
                    pointFlow = if (meter.q4 < 1E-3) meter.q3.times(1.25) else meter.q4
                    pointDev = meter.q4r
                })
            }

            meter.modelPointList = mpList.toList()
        }

        /**
         * analyze meter-eff for single day.
         * @param meter
         * @param day
         * @param eff
         */
        fun effSingleMeterDay(meter: ZoneMeter, day: DateTime, dataList: List<BwData>, eff: EffMeter): Boolean {
            if (meter.meterId.isNullOrBlank() || meter.pointList?.size ?: 0 < 3
                    || meter.modelPointList?.size ?: 0 < 3) {
                lgr.error("计量点不足3个或Q2/Q3不存在: ${meter.meterId}")
                eff.taskResult = EffFailureType.ABSENT_POINT.name
                return false
            }
            if (day.withTimeAtStartOfDay() != day) {
                lgr.error("不能包含时间部分: ${day.toString(ISODateTimeFormat.basicDateTime())}")
                eff.taskResult = EffFailureType.ABSENT_TIME.name
                return false
            }

            if (dataList.size < 1) {
                eff.taskResult = EffFailureType.DEVICE_OFFLINE.name
                return false
            } else if (dataList.size < 2) {
                eff.taskResult = EffFailureType.DATA_LESS.name
                return false
            }

            // decayed eff
            eff.decayObj = meter.decayObj

            eff.apply {
                runTime = Date()
            }

            if (!convertMeterEffPoint(eff, meter)) return false

            val idxStart = dataList.indexOfFirst { it.jodaSample?.withTimeAtStartOfDay() == day }
            val idxEnd = dataList.indexOfFirst { it.jodaSample?.withTimeAtStartOfDay()?.isAfter(day) == true }
            val dlist = if (idxStart > -1) dataList.subList(idxStart, (idxEnd
                    ?: dataList.size - 1) + 1) else emptyList()
            if (dlist.size < 2) {
                lgr.warn("not enough data for ${meter.meterId} in ${day.toString(ISODateTimeFormat.basicDateTime())}")
                eff.taskResult = EffFailureType.DATA_LESS.name
                return false
            }
            eff.also {
                it.startFwd = dlist.firstOrNull()?.forwardReading
                it.startTime = dlist.firstOrNull()?.sampleTime
                it.endFwd = dlist.lastOrNull()?.forwardReading
                it.endTime = dlist.lastOrNull()?.sampleTime
                it.meterWater = (it.endFwd ?: 0.0) - (it.startFwd ?: 0.0)
                if (it.meterWater ?: 0.0 < 1.0E-3) {
                    it.meterWater = 1.0E-3
                    it.taskResult = EffFailureType.DATA.name
                    return false
                }
                it.stdDays = Duration(DateTime(it.startTime), DateTime(it.endTime)).standardSeconds.toDouble().div(24 * 3600)
            }

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
                realWater = pointEffList!!.sumByDouble { it.realWater!! }

                // to avoid dividen-by-0 when matching match.
                if (realWater ?: 0.0 < 1.0E-3) realWater = 1.0E-3

                meterEff = if (realWater!! > 1.0E-3) {
                    meterWater!!.div(realWater!!).times(100.0)
                } else 100.0

                // decayed eff
                decayEff = if (eff.decayObj?.decayEff == null)
                    String.format("%.3f", meterEff)
                else {
                    var tfwd = decayObj!!.totalFwd ?: 1.0E6
                    if (tfwd < 1.0E5) tfwd = 1.0E6
                    val decay = endFwd?.div(tfwd)?.times(decayObj!!.decayEff?.div(100.0) ?: 0.0) ?: 0.0
                    String.format("%.3f", meterEff?.minus(decay) ?: 0.0)
                }

                q0v = pointEffList?.find { it.pointName?.equals("Q1", true) == true }?.pointWater ?: 0.0
                q1v = pointEffList?.find { it.pointName?.equals("Q2", true) == true }?.pointWater ?: 0.0
                q2v = pointEffList?.find { it.pointName?.equals("Q3", true) == true }?.pointWater ?: 0.0
                q3v = pointEffList?.find { it.pointName?.equals("Q4", true) == true }?.pointWater ?: 0.0
                q4v = pointEffList?.getOrNull(4)?.pointWater
                qtv = pointEffList?.drop(5)?.sumByDouble { it.pointWater!! }

                taskResult = ""
            }

            // ignore daily gmeans.
/*
            gmeansModel(dlist.filter {
                DateTime(it.sampleTime!!).withTimeAtStartOfDay().isEqual(day.withTimeAtStartOfDay())
            }, eff)
*/

            return true
        }

        fun gmeansModel(dlist: List<BwData>, eff: EffMeter): Boolean {
            // do not study anymore.
            if (dlist.size < 20) {
                return false;
            }
            eff.meterWater = dlist.lastOrNull()?.forwardReading?.minus(
                    dlist.firstOrNull()?.forwardReading ?: 0.0) ?: 0.0
            if (eff.meterWater!! < 1.0) {
                return false
            }

            val mg = GMeans.fit(dlist.dropLast(1).map { doubleArrayOf(it.avgFlow ?: 0.0) }.toTypedArray(), 12)
            val mapModel = TreeMap<Int, ArrayList<BwData>>()
            dlist.forEach {
                val k = mg.predict(doubleArrayOf(it.avgFlow ?: 0.0))
                if (!mapModel.containsKey(k)) {
                    mapModel[k] = ArrayList()
                }
                mapModel[k]!!.add(it)
            }

            val mlist = TreeMap<Int, EffMeterPoint>()
            mapModel.forEach {
                if (!mlist.containsKey(it.key)) {
                    mlist[it.key] = EffMeterPoint().apply {
                        taskId = eff.taskId
                        effId = eff.effId
                        taskStart = eff.taskStart
                        taskEnd = eff.taskEnd
                        meterId = eff.meterId
                        pointTypeObj = EffPointType.LEARN
                        periodTypeObj = eff.periodTypeObj

                        pointNo = it.key
                        pointName = it.key.toString()
                        pointFlow = it.value.minOf { d -> d.avgFlow ?: 0.0 }
                        highLimit = it.value.maxOf { d -> d.avgFlow ?: 0.0 }

                        pointWater = 0.0
                    }
                }

                mlist[it.key]!!.also { p ->
                    p.pointWater = it.value.sumByDouble { d -> d.forwardSum ?: 0.0 }
                }
            }

            val plist = mlist.values.sortedBy { it.pointFlow }
            plist.forEachIndexed { idx, pt ->
                pt.pointNo = idx + 1
                pt.pointName = "M${pt.pointNo}"
            }

            eff.modelPointList = eff.modelPointList.orEmpty().plus(plist)

            return true
        }

        private fun convertMeterEffPoint(eff: EffMeter, meter: ZoneMeter): Boolean {
            eff.pointList = meter.pointList!!.sortedBy { it.pointFlow }
            eff.pointList!!.forEach {
                if (it.pointFlow == null) {
                    lgr.error("计量点不能为空")
                    eff.taskResult = EffFailureType.ABSENT_POINT.name
                    return false
                }
            }
            eff.pointEffList = eff.pointList!!.map {
                EffMeterPoint().apply {
                    taskId = eff.taskId
                    effId = eff.effId
                    taskStart = eff.taskStart
                    taskEnd = eff.taskEnd
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
                taskStart = eff.taskStart
                taskEnd = eff.taskEnd
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
                    taskStart = eff.taskStart
                    taskEnd = eff.taskEnd
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
                taskStart = eff.taskStart
                taskEnd = eff.taskEnd
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

            return true
        }
    }
}