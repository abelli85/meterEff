package com.abel.bigwater.impl

import com.abel.bigwater.model.BwData
import com.abel.bigwater.model.BwMeter
import com.abel.bigwater.model.eff.EffMeter
import com.abel.bigwater.model.eff.VcMeterVerifyPoint
import com.alibaba.fastjson.JSON
import org.joda.time.LocalDateTime
import org.junit.Assert.*
import org.junit.Test
import org.slf4j.LoggerFactory

class EffTaskPojoTest {
    @Test
    fun testEffWithData() {
        val eff = EffMeter().apply {
            taskId = 1
            taskName = "task"
            meterId = "m1"
            meterName = "meter-1"
        }

        val meter = BwMeter().apply {
            meterId = "m1"
            meterName = "meter-1"

            pointList = listOf(VcMeterVerifyPoint().apply {
                pointId = 1
                pointNo = 1
                pointName = "Q1"
                pointFlow = 1.0
                pointDev = 0.05
            }, VcMeterVerifyPoint().apply {
                pointId = 2
                pointNo = 2
                pointName = "Q2"
                pointFlow = 1.5
                pointDev = 0.02
            }, VcMeterVerifyPoint().apply {
                pointId = 3
                pointNo = 3
                pointName = "Q3"
                pointFlow = 100.0
                pointDev = 0.01
            }, VcMeterVerifyPoint().apply {
                pointId = 4
                pointNo = 4
                pointName = "Q4"
                pointFlow = 120.0
                pointDev = 0.02
            })
        }

        val dlist = listOf(BwData().apply {
            meterId = "m1"
            sampleTime = LocalDateTime(2020, 9, 1, 10, 0).toDate()
            forwardDigits = 12345.0
            literPulse = 100
        }, BwData().apply {
            meterId = "m1"
            sampleTime = LocalDateTime(2020, 9, 1, 10, 15).toDate()
            forwardDigits = 12347.0
            literPulse = 100
        }, BwData().apply {
            meterId = "m1"
            sampleTime = LocalDateTime(2020, 9, 1, 10, 30).toDate()
            forwardDigits = 12350.0
            literPulse = 100
        }, BwData().apply {
            meterId = "m1"
            sampleTime = LocalDateTime(2020, 9, 1, 10, 45).toDate()
            forwardDigits = 12400.0
            literPulse = 100
        }, BwData().apply {
            meterId = "m1"
            sampleTime = LocalDateTime(2020, 9, 1, 11, 0).toDate()
            forwardDigits = 12700.0
            literPulse = 100
        })

        EffTaskBean.effSingleMeterDay(meter, LocalDateTime(2020, 9, 1, 0, 0).toDateTime(), dlist, eff)
        lgr.info("eff result: {}", JSON.toJSONString(eff, true))
    }

    @Test
    fun testEffWithDataTommorrow() {
        val eff = EffMeter().apply {
            taskId = 1
            taskName = "task"
            meterId = "m1"
            meterName = "meter-1"
        }

        val meter = BwMeter().apply {
            meterId = "m1"
            meterName = "meter-1"

            pointList = listOf(VcMeterVerifyPoint().apply {
                pointId = 1
                pointNo = 1
                pointName = "Q1"
                pointFlow = 1.0
                pointDev = 0.05
            }, VcMeterVerifyPoint().apply {
                pointId = 2
                pointNo = 2
                pointName = "Q2"
                pointFlow = 1.5
                pointDev = 0.02
            }, VcMeterVerifyPoint().apply {
                pointId = 3
                pointNo = 3
                pointName = "Q3"
                pointFlow = 100.0
                pointDev = 0.01
            }, VcMeterVerifyPoint().apply {
                pointId = 4
                pointNo = 4
                pointName = "Q4"
                pointFlow = 120.0
                pointDev = 0.02
            })
        }

        val dlist = listOf(BwData().apply {
            meterId = "m1"
            sampleTime = LocalDateTime(2020, 9, 1, 23, 0).toDate()
            forwardDigits = 12345.0
            literPulse = 100
        }, BwData().apply {
            meterId = "m1"
            sampleTime = LocalDateTime(2020, 9, 1, 23, 15).toDate()
            forwardDigits = 12347.0
            literPulse = 100
        }, BwData().apply {
            meterId = "m1"
            sampleTime = LocalDateTime(2020, 9, 1, 23, 30).toDate()
            forwardDigits = 12350.0
            literPulse = 100
        }, BwData().apply {
            meterId = "m1"
            sampleTime = LocalDateTime(2020, 9, 1, 23, 45).toDate()
            forwardDigits = 12400.0
            literPulse = 100
        }, BwData().apply {
            meterId = "m1"
            sampleTime = LocalDateTime(2020, 9, 2, 0, 0).toDate()
            forwardDigits = 12700.0
            literPulse = 100
        }, BwData().apply {
            meterId = "m1"
            sampleTime = LocalDateTime(2020, 9, 2, 0, 15).toDate()
            forwardDigits = 12710.0
            literPulse = 100
        })

        EffTaskBean.effSingleMeterDay(meter, LocalDateTime(2020, 9, 1, 0, 0).toDateTime(), dlist, eff)
        lgr.info("eff result: {}", JSON.toJSONString(eff, true))

        // compare
        val eff1 = EffMeter().apply {
            startFwd = 1234.5
            endFwd = 1270.0
            meterWater = 35.5
            realWater = 34.834
            meterEff = 1.019
        }
        assertEquals(eff.startFwd!!, eff1.startFwd!!, 1.0E-3)
        assertEquals(eff.endFwd!!, eff1.endFwd!!, 1.0E-3)
        assertEquals(eff.meterWater!!, eff1.meterWater!!, 1.0E-3)
        assertEquals(eff.realWater!!, eff1.realWater!!, 1.0E-3)
        assertEquals(eff.meterEff!!, eff1.meterEff!!, 1.0E-3)
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(EffTaskPojoTest::class.java)
    }
}