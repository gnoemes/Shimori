package com.gnoemes.shimori.common.ui.resources.util

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

//TODO formatters
actual class ShimoriDateTextFormatter {
    actual fun formatShortTime(instant: Instant): String = instant.toString()
    actual fun formatShortDate(date: LocalDate): String = date.toString()
    actual fun formatMediumDate(instant: Instant): String = instant.toString()
    actual fun formatMediumDateTime(instant: Instant): String = instant.toString()
    actual fun formatRelativeTime(
        instant: Instant,
        reference: Instant
    ): CharSequence? = instant.toString()

    actual fun formatDayOfWeek(dayOfWeek: DayOfWeek): String = dayOfWeek.name
}