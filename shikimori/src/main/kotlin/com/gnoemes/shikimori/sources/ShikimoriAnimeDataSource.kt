package com.gnoemes.shikimori.sources

import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shikimori.mappers.anime.AnimeDetailsMapper
import com.gnoemes.shikimori.mappers.anime.AnimeResponseMapper
import com.gnoemes.shikimori.mappers.anime.CalendarMapper
import com.gnoemes.shikimori.mappers.anime.RateResponseToAnimeWithRateMapper
import com.gnoemes.shikimori.mappers.rate.RateStatusMapper
import com.gnoemes.shikimori.mappers.roles.RolesMapper
import com.gnoemes.shimori.data.core.entities.roles.RolesInfo
import com.gnoemes.shimori.data.core.entities.titles.anime.Anime
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.user.UserShort
import com.gnoemes.shimori.data.core.mappers.forLists
import com.gnoemes.shimori.data.core.sources.AnimeDataSource

internal class ShikimoriAnimeDataSource(
    private val shikimori: Shikimori,
    private val mapper: AnimeResponseMapper,
    private val detailsMapper: AnimeDetailsMapper,
    private val ratedMapper: RateResponseToAnimeWithRateMapper,
    private val calendarMapper: CalendarMapper,
    private val statusMapper: RateStatusMapper,
    private val rolesMapper: RolesMapper,
) : AnimeDataSource {
    override suspend fun search(filters: Map<String, String>): List<Anime> {
        return shikimori.anime.search(filters)
            .let { mapper.forLists().invoke(it) }
    }

    override suspend fun getCalendar(): List<Anime> {
        return shikimori.anime.calendar()
            .let { calendarMapper.forLists().invoke(it) }
    }

    override suspend fun getWithStatus(user: UserShort, status: TrackStatus?): List<AnimeWithTrack> {
        return shikimori.anime.getUserRates(user.remoteId, statusMapper.mapInverse(status))
            .let { ratedMapper.forLists().invoke(it) }
    }

    override suspend fun get(title: Anime): AnimeWithTrack {
        return shikimori.anime.getDetails(title.id)
            .let { detailsMapper.map(it) }
    }

    override suspend fun roles(title: Anime): RolesInfo {
        return shikimori.anime.getRoles(title.id)
            .let { rolesMapper.map(it) }
    }
}