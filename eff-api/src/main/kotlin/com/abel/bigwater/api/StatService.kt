package com.abel.bigwater.api

import com.abel.bigwater.model.stat.MeterStat
import org.apache.dubbo.rpc.protocol.rest.support.ContentType
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("/stat")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8)
interface StatService {
    companion object {
        const val BASE_PATH = "/stat"

        const val PATH_STAT_METER = "/meter"
    }

    /**
     * 统计水表
     * @return 口径统计; 机构统计.
     */
    @POST
    @Path(PATH_STAT_METER)
    fun statMeter(holder: BwHolder<MeterParam>): BwResult<MeterStat>
}