package com.gnoemes.shimori.common.ui.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

//TODO formatters
actual class ShimoriDateTimeFormatter {
    actual fun formatShortTime(dateTime: LocalDateTime): String {
        return dateTime.toString()
    }

    actual fun formatShortDate(date: LocalDate): String = date.toString()
    actual fun formatMediumDate(date: LocalDate): String = date.toString()
    actual fun formatMediumDateTime(dateTime: LocalDateTime): String = dateTime.toString()
    actual fun formatRelativeTime(instant: Instant): CharSequence? = instant.toString()
}