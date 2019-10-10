package com.gnoemes.shimori.data.util

import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import org.joda.time.DateTime

interface DateTimeResponseConverter : JsonSerializer<DateTime>, JsonDeserializer<DateTime>