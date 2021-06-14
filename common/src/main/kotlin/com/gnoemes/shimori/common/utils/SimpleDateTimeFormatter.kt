package com.gnoemes.shimori.common.utils

import android.content.Context
import com.gnoemes.shimori.common.R
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.Period
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SimpleDateTimeFormatter @Inject constructor(
    private val context: Context
) {

    fun formatRelativeTimeSimple(dateTime: OffsetDateTime?): String? {
        if (dateTime == null) return null

        val now = OffsetDateTime.now()

        if (dateTime.isBefore(now)) {
            val period = Period.between(dateTime.toLocalDate(), now.toLocalDate())
            val duration = Duration.between(dateTime, now)

            val years = period.years
            val months = period.months
            val days = period.days
            val hours = duration.toHours().toInt()
            val minutes = duration.toMinutes().toInt()

            return when {
                years > 0 -> context.resources.getQuantityString(R.plurals.years_ago, years, years)
                months > 0 -> context.resources.getQuantityString(R.plurals.months_ago, months, months)
                days > 0 -> context.resources.getQuantityString(R.plurals.days_ago, days, days)
                hours > 0 -> context.resources.getQuantityString(R.plurals.hours_ago, hours, hours)
                else -> context.resources.getQuantityString(R.plurals.minutes_ago, minutes, minutes)
            }
        } else {
            val period = Period.between(now.toLocalDate(), dateTime.toLocalDate())

            val days = period.days

            if (days > 0) {
                return context.resources.getQuantityString(R.plurals.days, days, days)
            } else {
                val duration = Duration.between(now, dateTime)

                val hours = duration.toHours().toInt()
                val minutes = duration.toMinutes().toInt() % 60

                return if (hours < 1) {
                    context.getString(R.string.time_minute_short_format, minutes.addZeroIfNeed())
                } else {
                    context.getString(
                            R.string.time_hour_and_minute_short_format,
                            hours.addZeroIfNeed(),
                            minutes.addZeroIfNeed()
                    )
                }
            }
        }
    }

    private fun Int.addZeroIfNeed(): String {
        return if (this < 10) "0$this"
        else this.toString()
    }
}