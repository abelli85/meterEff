package com.abel.bigwater.api

import org.apache.dubbo.rpc.protocol.rest.support.ContentType
import javax.ws.rs.Consumes
import javax.ws.rs.Path
import javax.ws.rs.Produces

@Path("/eff")
@Consumes(ContentType.APPLICATION_JSON_UTF_8)
@Produces(ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8)
interface EffService {

    companion object {
        const val BASE_PATH = "/eff"
    }
}