package com.gnoemes.shikimori.util

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import java.lang.reflect.Type
import java.util.*
import javax.inject.Inject

internal class DateTimeResponseConverterImpl @Inject constructor() : DateTimeResponseConverter {
    override fun serialize(src: OffsetDateTime, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src.toZonedDateTime().withZoneSameInstant(ZoneId.systemDefault()).toString())
    }

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext): OffsetDateTime =
        try {
            OffsetDateTime.parse(json.asString)
        } catch (e: IllegalArgumentException) {
            val date = context.deserialize<Date>(json, Date::class.java)
            OffsetDateTime.parse(date.toString())
        }

}