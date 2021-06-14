package com.gnoemes.shikimori.util

import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import org.threeten.bp.OffsetDateTime

internal interface DateTimeResponseConverter : JsonSerializer<OffsetDateTime>, JsonDeserializer<OffsetDateTime>