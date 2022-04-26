package com.gnoemes.shimori.common.ui.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

expect class ShimoriDateTimeFormatter {
    fun formatShortTime(dateTime: LocalDateTime): String
    fun formatShortDate(date: LocalDate): String
    fun formatMediumDate(date: LocalDate): String
    fun formatMediumDateTime(dateTime: LocalDateTime): String
    fun formatRelativeTime(instant: Instant): CharSequence?
}