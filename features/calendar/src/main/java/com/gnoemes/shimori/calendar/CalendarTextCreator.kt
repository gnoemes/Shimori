package com.gnoemes.shimori.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import androidx.core.graphics.ColorUtils
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.gnoemes.common.utils.SimpleDateTimeFormatter
import com.gnoemes.shimori.base.di.PerActivity
import com.gnoemes.shimori.base.extensions.firstUpperCase
import com.gnoemes.shimori.model.anime.Anime
import org.joda.time.DateTime
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import java.util.*
import javax.inject.Inject

internal class CalendarTextCreator @Inject constructor(
    @PerActivity private val context: Context,
    private val dateFormatter: SimpleDateTimeFormatter
) {

    //TODO dark/light
    private val dividerColor by lazy {
        Color.BLACK.let { ColorUtils.setAlphaComponent(it, 97) }
    }

    fun showDate(dateTime: DateTime?): String? {
        if (dateTime == null) return null

        return formatDaysRelativeTime(dateTime)
    }

    fun showDescription(item: Anime?): CharSequence? {
        if (item == null) return null

        val isAlreadyAired =
            item.nextEpisodeDate == null || item.nextEpisodeDate?.isBeforeNow ?: false

        return if (isAlreadyAired) context.getString(R.string.episode_format, item.nextEpisode).toUpperCase(Locale.getDefault())
        else {
            buildSpannedString {
                appendUpper("${item.nextEpisode} ${context.getString(R.string.episode_short)}")
                color(dividerColor) { appendUpper("  •  ") }
                color(dividerColor) { appendUpper(dateFormatter.formatRelativeTimeSimple(item.nextEpisodeDate)) }
            }
        }
    }

    private fun formatDaysRelativeTime(dateTime: DateTime?): String? {
        if (dateTime == null) return null

        val now = DateTime.now()

        if (dateTime.isBefore(now)) {
            return context.getString(R.string.calendar_aired_already)
        } else {
            val dateFormatter = DateTimeFormat.forPattern("d MMMM")
            val weekFormatter = DateTimeFormat.forPattern("EEEE")

            val isToday = now.toLocalDate() == LocalDate(dateTime)
            val isTomorrow = now.toLocalDate().plusDays(1) == LocalDate(dateTime)

            val calendarFormat = context.getString(R.string.calendar_date_format)

            return when {
                isToday -> calendarFormat.format(context.getString(R.string.common_today), dateFormatter.print(dateTime))
                isTomorrow -> calendarFormat.format(context.getString(R.string.common_tomorrow), dateFormatter.print(dateTime))
                else -> calendarFormat.format(weekFormatter.print(dateTime).firstUpperCase(), dateFormatter.print(dateTime).firstUpperCase())
            }
        }
    }

    //textAllCaps="true" doesn't work with color spans
    @SuppressLint("DefaultLocale")
    private fun SpannableStringBuilder.appendUpper(value: String?) {
        this.append(value?.toUpperCase())
    }
}