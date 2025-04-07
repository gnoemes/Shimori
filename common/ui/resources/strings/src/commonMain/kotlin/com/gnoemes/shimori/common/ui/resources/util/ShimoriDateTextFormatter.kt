package com.gnoemes.shimori.common.ui.resources.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.MonthNames

class ShimoriDateTextFormatter(
    private val monthsShort: MonthNames,
    private val monthsFull: MonthNames,
) {
    fun formatShortTime(instant: Instant): String = instant.toString()
    fun formatShortDate(date: LocalDate): String = date.toString()
    fun formatMediumDate(instant: Instant): String = instant.toString()
    fun formatMediumDate(date: LocalDate): String = date.toString()
    fun formatMediumDateTime(instant: Instant): String = instant.toString()
    fun formatRelativeTime(
        instant: Instant,
        reference: Instant = Clock.System.now()
    ): CharSequence? = instant.toString()

    fun formatDayOfWeek(dayOfWeek: DayOfWeek): String = dayOfWeek.toString()
}