package com.abel.bigwater.model.stat

import com.alibaba.fastjson.JSON
import org.junit.Test

import org.junit.Assert.*
import org.slf4j.LoggerFactory
import java.lang.Exception

class StdErrHelperTest {

    @Test
    fun getErr() {
        val std1 = StdErrHelper(listOf(1.0, 2.0, 3.0, 4.0, 5.0))
        lgr.info("std error: {}", JSON.toJSONString(std1))

        kotlin.test.assertTrue {
            7.8 > std1.avg.toDouble().times(2)
                    && 4.8 > std1.err.toDouble().times(3.0)
        }

        val std2 = StdErrHelper(listOf(1.0, 2.0))
        lgr.info("std error: {}", JSON.toJSONString(std2))

        kotlin.test.assertTrue {
            3.7 > std2.avg.toDouble().times(2.0)
                    && 2.2 > std2.err.toDouble().times(3)
        }
    }

    @Test
    fun failGetErr() {
        try {
            val stdHelper = StdErrHelper(listOf(1.0))
            fail("should fail")
        } catch (ex: Exception) {
            // fine
            lgr.info(ex.message, ex)
        }
    }

    companion object {
        private val lgr = LoggerFactory.getLogger(StdErrHelperTest::class.java)
    }
}