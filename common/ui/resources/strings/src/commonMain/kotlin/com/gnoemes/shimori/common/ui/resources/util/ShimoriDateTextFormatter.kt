package com.gnoemes.shimori.common.ui.resources.util

import com.gnoemes.shimori.base.extensions.toDateFormat
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

class ShimoriDateTextFormatter(
    private val monthsShort: MonthNames,
    private val monthsFull: MonthNames,
) {
    private val mediumPattern = "dd MMM yyyy"
    private val mediumFormat by lazy {
        LocalDate.Format {
            dayOfMonth()
            char(' ')
            monthName(monthsShort)
            char(' ')
            year()
        }
    }

    fun formatShortTime(instant: Instant): String = instant.toString()
    fun formatShortDate(date: LocalDate): String = date.toString()
    fun formatMediumDate(instant: Instant): String = instant.toDateFormat(mediumPattern)
    fun formatMediumDate(date: LocalDate): String = date.format(mediumFormat)
    fun formatMediumDateTime(instant: Instant): String = instant.toString()
    fun formatRelativeTime(
        instant: Instant,
        reference: Instant = Clock.System.now()
    ): CharSequence? = instant.toString()

    fun formatDayOfWeek(dayOfWeek: DayOfWeek): String = dayOfWeek.toString()
}