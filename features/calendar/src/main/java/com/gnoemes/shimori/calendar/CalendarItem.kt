package com.gnoemes.shimori.calendar

import com.gnoemes.shimori.model.anime.AnimeWithRate
import org.joda.time.DateTime

data class CalendarItem(
    val date: DateTime,
    val animes: List<AnimeWithRate>
) {

    override fun equals(other: Any?): Boolean = when {
        other === this -> true
        other is CalendarItem -> animes == other.animes && date == other.date
        else -> false
    }

    override fun hashCode(): Int = arrayOf(animes, date).contentHashCode()
}