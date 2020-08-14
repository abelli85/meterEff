package com.abel.bigwater.model

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class JsonDateSerializer : JsonSerializer<Date>() {
    companion object {
        const val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    }

    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(date: Date, jgen: JsonGenerator,
                           provider: SerializerProvider) {
        val value = SimpleDateFormat(dateFormat).format(date)
        jgen.writeString(value)
    }

}
