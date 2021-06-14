package com.gnoemes.shimori.base.extensions

import org.threeten.bp.Instant
import org.threeten.bp.temporal.ChronoUnit

fun instantInPast(days: Int = 0, hours: Int = 0, minutes: Int = 0): Instant {
    val instant = Instant.now()
    return when {
        days != 0 -> instant.minus(days.toLong(), ChronoUnit.DAYS)
        hours != 0 -> instant.minus(hours.toLong(), ChronoUnit.HOURS)
        minutes != 0 -> instant.minus(minutes.toLong(), ChronoUnit.MINUTES)
        else -> instant
    }
}