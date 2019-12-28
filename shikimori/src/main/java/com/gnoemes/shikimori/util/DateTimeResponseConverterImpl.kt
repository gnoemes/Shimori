package com.gnoemes.shikimori.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.lang.reflect.Type
import java.util.*
import javax.inject.Inject

internal class DateTimeResponseConverterImpl @Inject constructor() : DateTimeResponseConverter {
    override fun serialize(src: DateTime, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src.withZone(DateTimeZone.getDefault()).toString())
    }

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext): DateTime =
        try {
            DateTime(json.asString, DateTimeZone.getDefault()).withMillisOfSecond(0)
        } catch (e: IllegalArgumentException) {
            val date = context.deserialize<Date>(json, Date::class.java)
            DateTime(date).withMillisOfSecond(0)
        }

}