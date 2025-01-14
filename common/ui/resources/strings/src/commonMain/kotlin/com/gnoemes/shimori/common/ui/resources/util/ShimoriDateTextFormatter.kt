package com.gnoemes.shimori.common.ui.resources.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

expect class ShimoriDateTextFormatter {
    fun formatShortTime(instant: Instant): String
    fun formatShortDate(date: LocalDate): String
    fun formatMediumDate(instant: Instant): String
    fun formatMediumDateTime(instant: Instant): String
    fun formatRelativeTime(
        instant: Instant,
        reference: Instant = Clock.System.now()
    ): CharSequence?

    fun formatDayOfWeek(dayOfWeek: DayOfWeek): String
}