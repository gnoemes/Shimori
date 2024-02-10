package com.gnoemes.shimori.common.ui.resources.util

import com.gnoemes.shimori.base.inject.ActivityScope
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import me.tatarka.inject.annotations.Inject


//TODO formatters
@ActivityScope
@Inject
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