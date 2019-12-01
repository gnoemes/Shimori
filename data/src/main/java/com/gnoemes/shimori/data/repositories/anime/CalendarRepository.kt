package com.gnoemes.shimori.data.repositories.anime

import com.gnoemes.shimori.base.extensions.asyncOrAwait
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarRepository @Inject constructor(
    private val animeStore: AnimeStore
) {

    fun observeCalendar(filter: String?) = animeStore.observeCalendar(filter)

    suspend fun updateCalendar() {
        asyncOrAwait("update_calendar") {
            //TODO need auth
        }
    }
}