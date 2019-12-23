package com.gnoemes.shikimori.repositories

import com.gnoemes.shikimori.mappers.AnimeResponseMapper
import com.gnoemes.shikimori.mappers.CalendarMapper
import com.gnoemes.shikimori.services.AnimeService
import com.gnoemes.shimori.base.entities.Result
import com.gnoemes.shimori.base.entities.Success
import com.gnoemes.shimori.base.extensions.toResult
import com.gnoemes.shimori.data_base.mappers.toListMapper
import com.gnoemes.shimori.data_base.sources.AnimeDataSource
import com.gnoemes.shimori.model.anime.Anime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ShikimoriAnimeDataSource @Inject constructor(
    private val service: AnimeService,
    private val animeMapper: AnimeResponseMapper,
    private val calendarMapper: CalendarMapper
) : AnimeDataSource {

    //TODO filters
    override suspend fun search(): List<Anime> {
        val filter = mapOf(
                "page" to "1",
                "limit" to "50"
        )
        val result = service.search(filter)
            .toResult(animeMapper.toListMapper())

        if (result is Success) {
            return result.data
        }
        return emptyList()
    }

    override suspend fun getCalendar(): Result<List<Anime>> =
        service.getCalendar()
            .toResult(calendarMapper.toListMapper())
}