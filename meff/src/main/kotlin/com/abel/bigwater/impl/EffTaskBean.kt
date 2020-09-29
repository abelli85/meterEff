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
import com.abel.bigwater.model.eff.EffMeter
import com.abel.bigwater.model.eff.EffMeterPoint
import com.abel.bigwater.model.eff.EffTask
import com.abel.bigwater.model.eff.VcMeterVerifyPoint
import com.abel.bigwater.model.zone.ZoneMeter
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

    @Scheduled(cron = "0 15 0/2 * * ?")
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
            taskStart = DUMMY_START.toDate()
            taskEnd = DUMMY_END.toDate()

            effMapper!!.createEffTask(this)
        }

        meterMapper!!.selectMeterDma(MeterParam().apply {
            firmId = firm.firmId
        }).forEach {
            fillPointList(it)

            if (it.pointList.isNullOrEmpty()) {
                lgr.warn("no point for {}/{}", it.meterId, it.meterName)
                return@forEach
            }

            val effList = effMeter(it, task, 31)
            lgr.info("analyze {} eff for {}/{} in {}", effList.size,
                    it.meterId, it.meterName, firm.title)
        }
    }

    /**
     * 填充计量点
     */
    fun fillPointList(it: ZoneMeter) {
        it.pointList = meterMapper!!.listVerifyPointLast(MeterParam().apply {
            meterId = it.meterId
        })

        val ptList = arrayListOf<VcMeterVerifyPoint>()
        if (it.q1 > 1E-3) {
            ptList.add(VcMeterVerifyPoint().apply {
                meterId = it.meterId
                verifyDate = LocalDate(2010, 1, 1).toDate()
                pointNo = 1
                pointName = "Q1"
                pointFlow = it.q1
                pointDev = it.q1r
            })
        }

        if (it.q2 > 1E-3) {
            ptList.add(VcMeterVerifyPoint().apply {
                meterId = it.meterId
                verifyDate = LocalDate(2010, 1, 1).toDate()
                pointNo = 2
                pointName = "Q2"
                pointFlow = it.q2
                pointDev = it.q2r
            })
        }

        if (it.q3 > 1E-3) {
            ptList.add(VcMeterVerifyPoint().apply {
                meterId = it.meterId
                verifyDate = LocalDate(2010, 1, 1).toDate()
                pointNo = 3
                pointName = "Q3"
                pointFlow = it.q3
                pointDev = it.q3r
            })
        }

        if (it.q4 > 1E-3) {
            ptList.add(VcMeterVerifyPoint().apply {
                meterId = it.meterId
                verifyDate = LocalDate(2010, 1, 1).toDate()
                pointNo = 4
                pointName = "Q4"
                pointFlow = it.q4
                pointDev = it.q4r
            })
        }

        it.pointList = ptList.toList()
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
                taskStart = day1.toDate()
                taskEnd = day1.plusDays(1).toDate()

                sizeId = meter.sizeId ?: 0
                sizeName = meter.sizeName ?: "0"
                modelSize = meter.modelSize

                qr1 = meter.q1r
                qr2 = meter.q2r
                qr3 = meter.q3r
                qr4 = meter.q4r

                q3 = meter.q3
                q4 = meter.q4
                if (meter.q1 > 1E-3) {
                    q3toq1 = meter.q3 / meter.q1
                    q2toq1 = meter.q2 / meter.q1
                }
                if (meter.q3 > 1E-3) {
                    q4toq3 = meter.q4 / meter.q3
                }
            }

            if (effSingleMeterDay(meter, day1, dlist, eff)) {
                val param = EffParam().apply {
                    meterId = meter.meterId
                    taskId = task.taskId
                    taskStart = day1.toDate()
                    taskEnd = day1.plusDays(1).toDate()
                }
                lgr.info("remove eff meter: {}/{}",
                        effMapper!!.deleteEffPoint(param),
                        effMapper!!.deleteEffMeter(param)
                )

                lgr.info("insert eff meter: {}/{}",
                        effMapper!!.insertEffMeter(EffParam().apply {
                            meterList = listOf(eff)
                        }),
                        effMapper!!.insertEffPoint(EffParam().apply {
                            pointEffList = eff.pointEffList
                        })
                )

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
            if (meter.meterId.isNullOrBlank() || meter.pointList.isNullOrEmpty()) {
                throw IllegalArgumentException("计量点不存在: ${meter.meterId}")
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

            eff.runTime = Date()

            eff.pointEffList = eff.pointList!!.map {
                EffMeterPoint().apply {
                    taskId = eff.taskId
                    meterId = meter.meterId

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
                meterId = meter.meterId

                pointNo = eff.pointList!!.size
                pointName = "Q".plus(eff.pointList!!.size)
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

                val ep = eff.pointEffList!!.first { d1.avgFlow!!.absoluteValue <= it.pointFlow!! }
                ep.pointWater = ep.pointWater!!.plus(d1.forwardSum!!)

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
            eff.pointEffList!!.last().pointFlow = eff.pointEffList!!.get(eff.pointEffList!!.size - 2).pointFlow?.times(2.0)

            eff.apply {
                runDuration = Duration(DateTime(runTime!!), DateTime.now()).millis.toInt()
                meterWater = pointEffList!!.sumByDouble { it.pointWater!! }
                realWater = pointEffList!!.sumByDouble { it.realWater!! }
                meterEff = if (realWater!! > 1.0E-3) {
                    meterWater!!.div(realWater!!)
                } else 1.0

                q0v = pointEffList?.getOrNull(0)?.pointWater
                q1v = pointEffList?.getOrNull(1)?.pointWater
                q2v = pointEffList?.getOrNull(2)?.pointWater
                q3v = pointEffList?.getOrNull(3)?.pointWater
                q4v = pointEffList?.getOrNull(4)?.pointWater
                qtv = pointEffList?.drop(5)?.sumByDouble { it.pointWater!! }

                taskResult = ""
            }

            return true
        }
    }
}