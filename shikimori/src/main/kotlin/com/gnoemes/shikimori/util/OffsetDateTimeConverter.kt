package com.gnoemes.shikimori.util

import com.google.gson.*
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneId
import java.lang.reflect.Type
import java.util.*

internal class OffsetDateTimeConverter : JsonSerializer<OffsetDateTime>, JsonDeserializer<OffsetDateTime> {
    override fun serialize(src: OffsetDateTime, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src.toZonedDateTime().withZoneSameInstant(ZoneId.systemDefault())
            .toString())
    }

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext): OffsetDateTime =
        try {
            OffsetDateTime.parse(json.asString)
        } catch (e: IllegalArgumentException) {
            val date = context.deserialize<Date>(json, Date::class.java)
            OffsetDateTime.parse(date.toString())
        }

}