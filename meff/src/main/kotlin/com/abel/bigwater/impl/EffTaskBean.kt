package com.abel.bigwater.impl

import com.abel.bigwater.api.DataParam
import com.abel.bigwater.api.EffParam
import com.abel.bigwater.api.MeterParam
import com.abel.bigwater.mapper.DataMapper
import com.abel.bigwater.mapper.EffMapper
import com.abel.bigwater.mapper.MeterMapper
import com.abel.bigwater.model.BwData
import com.abel.bigwater.model.BwFirm
import com.abel.bigwater.model.BwMeter
import com.abel.bigwater.model.eff.EffMeter
import com.abel.bigwater.model.eff.EffMeterPoint
import com.abel.bigwater.model.eff.EffTask
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.format.ISODateTimeFormat
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*
import kotlin.math.absoluteValue

@Component
class EffTaskBean {
    @Autowired
    private var meterMapper: MeterMapper? = null

    @Autowired
    private var dataMapper: DataMapper? = null

    @Autowired
    private var effMapper: EffMapper? = null

    fun effFirm(firm: BwFirm) {
        val meterList = meterMapper!!.selectMeterDma(MeterParam().apply {
            firmId = firm.firmId?.plus("%")
        }).forEach {
            it.pointList = meterMapper!!.listVerifyPointLast(MeterParam().apply {
                meterId = it.meterId
            })

            if (it.pointList.isNullOrEmpty()) {
                lgr.warn("no point for {}/{}", it.meterId, it.meterName)
                return@forEach
            }
            val effList = effMeter(it)
            lgr.info("analyze eff for {}/{} in {}", it.meterId, it.meterName,
                    firm.title)
        }
    }

    fun effMeter(meter: BwMeter, task: EffTask? = null, days: Int = 31): List<EffMeter> {
        val timeRange = dataMapper!!.realtimeDateRange(DataParam().apply {
            meterId = meter.meterId
        }).firstOrNull()?.also {
            if (it.maxDateTime?.isAfterNow == true) it.maxDateTime = DateTime.now()
            if (it.minDateTime?.isAfterNow == true) it.minDateTime = DateTime.now()
        } ?: return emptyList()

        val effRange = effMapper!!.listEffRange(EffParam().apply {
            meterId = meter.meterId
        }).firstOrNull() ?: timeRange

        effRange.also {
            if (it.maxDateTime?.isAfterNow == true) it.maxDateTime = DateTime.now()
            if (it.minDateTime?.isAfterNow == true) it.minDateTime = DateTime.now()

            it.minDateTime = it.maxDateTime!!.minusDays(1)
            it.maxDateTime = it.maxDateTime!!.plusDays(days)
            if (it.maxDateTime!!.isAfter(timeRange.maxDateTime!!)) {
                it.maxDateTime = timeRange.maxDateTime
            }
        }

        return effMeterRange(meter, effRange.minDateTime!!, effRange.maxDateTime!!, task)
    }

    fun effMeterRange(meter: BwMeter, startDay: DateTime, endDay: DateTime, task: EffTask? = null): List<EffMeter> {
        val dlist = dataMapper!!.selectMeterRealtime(DataParam().apply {
            meterId = meter.meterId
            sampleTime1 = startDay.toDate()
            sampleTime2 = endDay.toDate()
        })

        if (dlist.isEmpty()) {
            lgr.info("empty realtime for {}", meter.meterId)
        }

        val retList = arrayListOf<EffMeter>()
        var day1 = startDay.withTimeAtStartOfDay()
        while (day1.isBefore(endDay)) {
            val eff = EffMeter().apply {
                meterId = meter.meterId
                taskId = task?.taskId ?: 0
                taskStart = day1.toDate()
                taskEnd = day1.plusDays(1).toDate()
            }

            if (effSingleMeterDay(meter, day1, dlist, eff)) {
                val param = EffParam().apply {
                    meterId = meter.meterId
                    taskId = task?.taskId ?: 0
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

        /**
         * analyze meter-eff for single day.
         * @param meter
         * @param day
         * @param eff
         */
        fun effSingleMeterDay(meter: BwMeter, day: DateTime, dataList: List<BwData>, eff: EffMeter): Boolean {
            if (meter.meterId.isNullOrBlank() || meter.pointList.isNullOrEmpty()) {
                throw IllegalArgumentException("point list of meter should be populated: ${meter.meterId}")
            }
            if (day.withTimeAtStartOfDay() != day) {
                throw IllegalArgumentException("day must be at start of the day: ${day.toString(ISODateTimeFormat.basicDateTime())}")
            }
            eff.pointList = meter.pointList!!.sortedBy { it.pointFlow }
            eff.pointList!!.forEach {
                if (it.pointFlow == null) {
                    throw IllegalArgumentException("point-flow can't be null")
                }
            }

            val dlist = dataList.dropWhile { it.jodaSample?.withTimeAtStartOfDay() != day }
            if (dlist.size < 2) {
                lgr.warn("not enough data for ${meter.meterId} in ${day.toString(ISODateTimeFormat.basicDateTime())}")
                return false
            }
            dlist.forEach {
                if (it.sampleTime == null) {
                    throw IllegalArgumentException("sample-time can't be null: ${meter.meterId}")
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
                    break
                }
            }
            if (eff.endFwd == null) {
                eff.endFwd = dlist.last().forwardReading
            }

            eff.pointEffList!!.forEach {
                it.realWater = it.pointWater!! - it.pointWater!!.times(it.pointDev!!)
            }

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