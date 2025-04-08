package com.gnoemes.shimori.base.extensions

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.Duration


inline val Duration.inPast: Instant
    get() = Clock.System.now() - this

fun Instant.toDateFormat(pattern: String): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(Date(toEpochMilliseconds()))
}

