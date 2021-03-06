package com.gnoemes.shikimori.repositories

import com.gnoemes.shikimori.mappers.anime.AnimeResponseMapper
import com.gnoemes.shikimori.mappers.anime.CalendarMapper
import com.gnoemes.shikimori.mappers.anime.RateResponseToAnimeMapper
import com.gnoemes.shikimori.services.AnimeService
import com.gnoemes.shimori.base.entities.Result
import com.gnoemes.shimori.base.entities.Success
import com.gnoemes.shimori.base.extensions.toResult
import com.gnoemes.shimori.data_base.mappers.toListMapper
import com.gnoemes.shimori.data_base.sources.AnimeDataSource
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.rate.RateStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ShikimoriAnimeDataSource @Inject constructor(
    private val service: AnimeService,
    private val animeMapper: AnimeResponseMapper,
    private val calendarMapper: CalendarMapper,
    private val rateToAnimeMapper: RateResponseToAnimeMapper
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

    override suspend fun getAnimeWithStatus(userId: Long, status: RateStatus): Result<List<Anime>> =
        service.getUserAnimeRates(userId, status.shikimoriValue)
            .toResult(rateToAnimeMapper.toListMapper())
}