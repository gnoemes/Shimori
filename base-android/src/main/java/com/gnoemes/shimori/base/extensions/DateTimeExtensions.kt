package com.gnoemes.shimori.base.extensions

import org.joda.time.Days
import org.joda.time.Hours
import org.joda.time.Instant
import org.joda.time.Minutes

fun instantInPast(days: Int = 0, hours: Int = 0, minutes: Int = 0): Instant {
    val instant = Instant.now()
    return when {
        days != 0 -> instant.minus(Days.days(days).toStandardDuration())
        hours != 0 -> instant.minus(Hours.hours(hours).toStandardDuration())
        minutes != 0 -> instant.minus(Minutes.minutes(minutes).toStandardDuration())
        else -> instant
    }
}