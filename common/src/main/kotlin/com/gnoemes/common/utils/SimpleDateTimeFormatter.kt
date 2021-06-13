package com.gnoemes.common.utils

import android.content.Context
import com.gnoemes.common.R
import org.joda.time.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SimpleDateTimeFormatter @Inject constructor(
    private val context: Context
) {

    fun formatRelativeTimeSimple(dateTime: DateTime?): String? {
        if (dateTime == null) return null

        val now = DateTime.now()

        if (dateTime.isBefore(now)) {
            val years = Years.yearsBetween(now, dateTime).years.absoluteValue
            val months = Months.monthsBetween(now, dateTime).months.absoluteValue
            val days = Days.daysBetween(now, dateTime).days.absoluteValue
            val hours = Hours.hoursBetween(now, dateTime).hours.absoluteValue
            val minutes = Minutes.minutesBetween(now, dateTime).minutes.absoluteValue

            return when {
                years > 0 -> context.resources.getQuantityString(R.plurals.years_ago, years, years)
                months > 0 -> context.resources.getQuantityString(R.plurals.months_ago, months, months)
                days > 0 -> context.resources.getQuantityString(R.plurals.days_ago, days, days)
                hours > 0 -> context.resources.getQuantityString(R.plurals.hours_ago, hours, hours)
                else -> context.resources.getQuantityString(R.plurals.minutes_ago, minutes, minutes)
            }
        } else {
            val days = Days.daysBetween(now, dateTime).days

            if (days > 0) {
                return context.resources.getQuantityString(R.plurals.days, days, days)
            } else {
                val hours = Hours.hoursBetween(now, dateTime).hours
                val minutes = Minutes.minutesBetween(now, dateTime).minutes % 60

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