package com.gnoemes.shimori.base.extensions

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus


fun instantInPast(days: Int = 0, hours: Int = 0, minutes: Int = 0): Instant {
    val instant = Clock.System.now()
    return when {
        days != 0 -> instant.minus(days, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
        hours != 0 -> instant.minus(hours, DateTimeUnit.HOUR, TimeZone.currentSystemDefault())
        minutes != 0 -> instant.minus(minutes, DateTimeUnit.MINUTE, TimeZone.currentSystemDefault())
        else -> instant
    }
}