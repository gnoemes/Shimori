package com.gnoemes.shimori.data.repositories.calendar

import com.gnoemes.shimori.base.di.Shikimori
import com.gnoemes.shimori.base.entities.Success
import com.gnoemes.shimori.base.extensions.instantInPast
import com.gnoemes.shimori.data.repositories.anime.AnimeStore
import com.gnoemes.shimori.data_base.sources.AnimeDataSource
import org.threeten.bp.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarRepository @Inject constructor(
    private val animeStore: AnimeStore,
    @Shikimori private val animeDataSource: AnimeDataSource,
    private val lastRequestStore: CalendarLastRequestStore
) {

    fun observeCalendar(filter: String?) = animeStore.observeCalendar(filter)

    suspend fun updateCalendar() {
        val results = animeDataSource.getCalendar()
        if (results is Success && results.data.isNotEmpty()) {
            animeStore.updateAnimes(results.data)
            lastRequestStore.updateLastRequest()
            return
        }
    }

    suspend fun needUpdateCalendar(expiry: Instant = instantInPast(hours = 2)): Boolean {
        return lastRequestStore.isRequestBefore(expiry)
    }
}