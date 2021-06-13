package com.gnoemes.shimori.base.util

import android.text.format.DateUtils
import com.gnoemes.shimori.base.di.MediumDate
import com.gnoemes.shimori.base.di.MediumDateTime
import com.gnoemes.shimori.base.di.ShortDate
import com.gnoemes.shimori.base.di.ShortTime
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShimoriDateTimeFormatter @Inject constructor(
    @ShortTime private val shortTimeFormatter: DateTimeFormatter,
    @ShortDate private val shortDateFormatter: DateTimeFormatter,
    @MediumDate private val mediumDateFormatter: DateTimeFormatter,
    @MediumDateTime private val mediumDateTimeFormatter: DateTimeFormatter
) {

    fun formatShortTime(dateTime: DateTime): String = shortTimeFormatter.print(dateTime)

    fun formatShortDate(dateTime: DateTime): String = shortDateFormatter.print(dateTime)

    fun formatMediumDate(dateTime: DateTime): String = mediumDateFormatter.print(dateTime)

    fun formatMediumDateTime(dateTime: DateTime): String = mediumDateTimeFormatter.print(dateTime)

    fun formatRelativeTime(dateTime: DateTime?): CharSequence? {
        if (dateTime == null) return null

        val now = DateTime.now()

        return if (dateTime.isBefore(now)) {
            if (dateTime.year == now.year || dateTime.isAfter(now.minusDays(1))) {
                DateUtils.getRelativeTimeSpanString(
                        dateTime.millis,
                        now.millis,
                        DateUtils.MINUTE_IN_MILLIS,
                        DateUtils.FORMAT_SHOW_DATE
                )
            } else {
                formatShortDate(dateTime)
            }
        } else {
            if (dateTime.year == now.year || dateTime.isBefore(now.plusDays(7))) {
                DateUtils.getRelativeTimeSpanString(
                        dateTime.millis,
                        System.currentTimeMillis(),
                        DateUtils.MINUTE_IN_MILLIS,
                        DateUtils.FORMAT_SHOW_DATE)
            } else {
                formatShortDate(dateTime)
            }
        }
    }
}