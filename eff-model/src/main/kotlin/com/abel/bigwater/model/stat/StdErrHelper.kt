package com.abel.bigwater.model.stat

import java.lang.IllegalArgumentException
import kotlin.math.sqrt

/**
 * 标准差计算
 */
class StdErrHelper(lst: List<Number>) {
    val list: List<Number> = lst

    lateinit var avg: Number

    lateinit var err: Number

    init {
        if (list.size < 2) {
            throw IllegalArgumentException("at least 2 elements are required to calculate avg/err.")
        }

        avg = list.sumByDouble { it.toDouble() }.div(list.size)
        err = list.sumByDouble { it.toDouble().minus(avg.toDouble()).times(it.toDouble().minus(avg.toDouble())) }
        err = sqrt(err.toDouble().div(list.size - 1))
    }
}