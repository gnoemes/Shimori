package com.gnoemes.shimori.data.repositories.anime

import com.gnoemes.shimori.base.di.Shikimori
import com.gnoemes.shimori.base.entities.Success
import com.gnoemes.shimori.base.extensions.asyncOrAwait
import com.gnoemes.shimori.data_base.sources.AnimeDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarRepository @Inject constructor(
    private val animeStore: AnimeStore,
    @Shikimori private val animeDataSource: AnimeDataSource
) {

    fun observeCalendar(filter: String?) = animeStore.observeCalendar(filter)

    suspend fun updateCalendar() {
        asyncOrAwait("update_calendar") {
            val results = animeDataSource.getCalendar()
            if (results is Success && results.data.isNotEmpty()) {
                animeStore.updateAnimes(results.data)
                return@asyncOrAwait
            }
        }
    }
}