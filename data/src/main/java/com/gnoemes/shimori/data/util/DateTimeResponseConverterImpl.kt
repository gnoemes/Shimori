package com.gnoemes.shimori.data.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.lang.reflect.Type
import java.util.*
import javax.inject.Inject

class DateTimeResponseConverterImpl @Inject constructor() : DateTimeResponseConverter {
    override fun serialize(src: DateTime, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src.withZone(DateTimeZone.UTC).toLocalDateTime().toString())
    }

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext): DateTime =
        try {
             DateTime(json.asString, DateTimeZone.UTC).withZone(DateTimeZone.getDefault())
        } catch (e: IllegalArgumentException) {
            val date = context.deserialize<Date>(json, Date::class.java)
             DateTime(date)
        }

}