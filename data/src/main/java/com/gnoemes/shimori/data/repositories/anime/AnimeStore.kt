package com.gnoemes.shimori.data.repositories.anime

import com.gnoemes.shimori.data.daos.AnimeDao
import com.gnoemes.shimori.data.util.DatabaseTransactionRunner
import com.gnoemes.shimori.model.anime.CalendarItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AnimeStore @Inject constructor(
    private val runner: DatabaseTransactionRunner,
    private val animeDao: AnimeDao
) {

    fun observeCalendar(filter: String?): Flow<List<CalendarItem>> = when {
        filter.isNullOrBlank() -> animeDao.observeCalendar()
        else -> animeDao.observeCalendarFilter(filter)
    }
}