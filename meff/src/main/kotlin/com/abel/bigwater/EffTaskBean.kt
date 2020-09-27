package com.abel.bigwater

import com.abel.bigwater.mapper.DataMapper
import com.abel.bigwater.mapper.EffMapper
import com.abel.bigwater.mapper.MeterMapper
import com.abel.bigwater.model.BwData
import com.abel.bigwater.model.BwMeter
import com.abel.bigwater.model.eff.EffMeter
import com.abel.bigwater.model.eff.EffMeterPoint
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

    companion object {
        private val lgr = LoggerFactory.getLogger(EffTaskBean::class.java)

        /**
         * analyze meter-eff for single day.
         * @param meter
         * @param day
         * @param eff
         */
        fun effSingleMeterDay(meter: BwMeter, day: DateTime, dlist: List<BwData>, eff: EffMeter) {
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

            if (dlist.size < 2) {
                lgr.warn("not enough data for ${meter.meterId} in ${day.toString(ISODateTimeFormat.basicDateTime())}")
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
            run breakDlist@{
                1.until(dlist.size).forEach { idx ->
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
                        return@breakDlist
                    }
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
        }
    }
}