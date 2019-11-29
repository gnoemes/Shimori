package com.gnoemes.shikimori.util

import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import org.joda.time.DateTime

internal interface DateTimeResponseConverter : JsonSerializer<DateTime>, JsonDeserializer<DateTime>