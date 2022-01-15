package com.gnoemes.shikimori.repositories

import com.gnoemes.shikimori.mappers.anime.AnimeResponseMapper
import com.gnoemes.shikimori.mappers.anime.CalendarMapper
import com.gnoemes.shikimori.mappers.anime.RateResponseToAnimeMapper
import com.gnoemes.shikimori.services.AnimeService
import com.gnoemes.shimori.base.extensions.bodyOrThrow
import com.gnoemes.shimori.base.extensions.withRetry
import com.gnoemes.shimori.data_base.mappers.forLists
import com.gnoemes.shimori.data_base.sources.AnimeDataSource
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.user.UserShort
import retrofit2.awaitResponse
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
        return service.search(filter)
            .awaitResponse()
            .let { animeMapper.forLists().invoke(it.bodyOrThrow()) }
    }

    override suspend fun getCalendar(): List<Anime> =
        withRetry {
            service.getCalendar()
                .awaitResponse()
                .let { calendarMapper.forLists().invoke(it.bodyOrThrow()) }
        }

    override suspend fun getAnimeWithStatus(
        user: UserShort,
        status: RateStatus?
    ): List<Anime> =
        withRetry {
            service.getUserAnimeRates(user.shikimoriId!!, status?.shikimoriValue)
                .awaitResponse()
                .let { rateToAnimeMapper.forLists().invoke(it.bodyOrThrow()) }
        }

}

