package com.gnoemes.shikimori.sources

import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shikimori.mappers.anime.AnimeResponseMapper
import com.gnoemes.shikimori.mappers.anime.CalendarMapper
import com.gnoemes.shikimori.mappers.anime.RateResponseToAnimeMapper
import com.gnoemes.shikimori.mappers.rate.RateStatusMapper
import com.gnoemes.shimori.data.base.entities.rate.RateStatus
import com.gnoemes.shimori.data.base.entities.titles.anime.Anime
import com.gnoemes.shimori.data.base.entities.user.UserShort
import com.gnoemes.shimori.data.base.mappers.forLists
import com.gnoemes.shimori.data.base.sources.AnimeDataSource

internal class ShikimoriAnimeDataSource(
    private val shikimori: Shikimori,
    private val mapper: AnimeResponseMapper,
    private val ratedMapper: RateResponseToAnimeMapper,
    private val calendarMapper: CalendarMapper,
    private val statusMapper: RateStatusMapper,
) : AnimeDataSource {
    override suspend fun search(filters: Map<String, String>): List<Anime> {
        return shikimori.anime.search(filters)
            .let { mapper.forLists().invoke(it) }
    }

    override suspend fun getCalendar(): List<Anime> {
        return shikimori.anime.calendar()
            .let { calendarMapper.forLists().invoke(it) }
    }

    override suspend fun getWithStatus(user: UserShort, status: RateStatus?): List<Anime> {
        return shikimori.anime.getUserRates(user.shikimoriId, statusMapper.mapInverse(status))
            .let { ratedMapper.forLists().invoke(it) }
    }
}