package com.abel.bigwater.impl

import com.abel.bigwater.api.MeterParam
import com.abel.bigwater.mapper.MeterMapper
import com.abel.bigwater.model.BwFirm
import com.abel.bigwater.model.DataRange
import com.abel.bigwater.model.eff.EffTask
import org.apache.dubbo.remoting.http.servlet.ServletManager
import org.joda.time.LocalDate
import org.junit.BeforeClass
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import org.mybatis.spring.annotation.MapperScan
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockServletContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import kotlin.test.fail

@ContextConfiguration(locations = ["classpath:/spring/rest-provider.xml", "classpath:/spring-mybatis.xml"])
@RunWith(SpringJUnit4ClassRunner::class)
@FixMethodOrder(MethodSorters.DEFAULT)
@MapperScan(basePackages = ["com.abel.bigwater.mapper"])
class EffTaskBeanTest {

    @Autowired
    var bean: EffTaskBean? = null

    @Autowired
    var meterMapper: MeterMapper? = null

    @Test
    fun testEffFirm() {
        bean!!.effFirm(BwFirm().apply {
            firmId = "76%"
            firmName = "fuzhou-test"
        })
    }

    @Test
    fun effMeter() {
        val meter = meterMapper!!.selectMeter(MeterParam().apply {
            meterCode = "110111801101"
        }).firstOrNull() ?: fail("选择的水表不存在: 101B")

        if (!bean!!.fillPointList(meter)) fail("水表101B缺少检定点")

        val effList = bean!!.effMeterRange(meter, LocalDate(2020, 8, 1).toDateTimeAtStartOfDay(),
                LocalDate(2020, 11, 1).toDateTimeAtStartOfDay(),
                EffTask().apply {
                    taskId = 0
                    taskName = "test-0"
                })

        bean!!.buildMonthEff(DataRange().apply {
            meterId = meter.meterId
            minTime = effList.minOf { e1 -> e1.taskStart!! }
            maxTime = effList.maxOf { e1 -> e1.taskEnd!! }
        })
    }

    /**
     * meterId: 101
     *     extid     |          min           |          max
    --------------+------------------------+------------------------
    SS_SK_610066 | 2020-06-04 20:00:00+08 | 2020-10-25 02:00:00+08
     */
    @Test
    fun effMeterManual() {
        val mp = MeterParam().apply {
            meterCode = "113540524101"
        }
        val meter = meterMapper!!.selectMeter(mp).firstOrNull() ?: fail("选择的水表不存在: ${mp.meterCode}")

        if (!bean!!.fillPointList(meter)) fail("水表${mp.meterCode}缺少检定点")

        bean!!.effMeterRange(meter, LocalDate(2020, 10, 1).toDateTimeAtStartOfDay(),
                LocalDate(2020, 10, 31).toDateTimeAtStartOfDay(),
                EffTask().apply {
                    taskId = 0
                    taskName = "test-0"
                })

        bean!!.buildYearEff(DataRange().apply {
            meterId = meter.meterId
            minDateTime = LocalDate(2020, 10, 1).toDateTimeAtStartOfDay()
            maxDateTime = LocalDate(2020, 10, 31).toDateTimeAtStartOfDay()
        })
    }

    /**
     * meterId: 101
     *     extid     |          min           |          max
    --------------+------------------------+------------------------
    SS_SK_610066 | 2020-06-04 20:00:00+08 | 2020-10-25 02:00:00+08
     */
    @Test
    fun learnWeek() {
        val meter = meterMapper!!.selectMeter(MeterParam().apply {
            meterId = "164"
        }).firstOrNull() ?: fail("选择的水表不存在: 101")

        if (!bean!!.fillPointList(meter)) fail("水表101缺少检定点")

        bean!!.learnMeter(meter,
                EffTask().apply {
                    taskId = 0
                    taskName = "test-0"
                    taskStart = LocalDate(2020, 10, 1).toDate()
                    taskEnd = LocalDate(2020, 10, 25).toDate()
                })
    }

    @Test
    fun effMeterRange() {
    }

    @Test
    fun testEffAll() {
        bean!!.effAll()
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(EffTaskBeanTest::class.java)

        @BeforeClass
        @JvmStatic
        fun addServletContext() {
            lgr.warn("prepare servlet container...")
            ServletManager.getInstance().addServletContext(ServletManager.EXTERNAL_SERVER_PORT, MockServletContext())
        }
    }
}