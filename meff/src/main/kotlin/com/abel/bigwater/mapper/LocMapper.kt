package com.abel.bigwater.mapper

import com.abel.bigwater.api.LocParam
import com.abel.bigwater.model.BwDmaLoc
import org.apache.ibatis.annotations.Mapper

@Mapper
interface LocMapper {

    fun selectDmaLocById(lp: LocParam): BwDmaLoc?

    fun selectDmaLocAll(lp: LocParam): List<BwDmaLoc>?

    fun selectDmaLocAllLast(lp: LocParam): List<BwDmaLoc>?

    fun updateDmaLoc(dl: BwDmaLoc): Int
}