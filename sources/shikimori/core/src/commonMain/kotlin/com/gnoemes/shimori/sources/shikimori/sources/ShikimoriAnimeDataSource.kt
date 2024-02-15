package com.gnoemes.shimori.sources.shikimori.sources


import com.apollographql.apollo3.api.Optional
import com.gnoemes.shimori.base.utils.forLists
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.data.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.user.UserShort
import com.gnoemes.shimori.source.data.AnimeDataSource
import com.gnoemes.shimori.sources.shikimori.AnimeDetailsQuery
import com.gnoemes.shimori.sources.shikimori.AnimeTracksQuery
import com.gnoemes.shimori.sources.shikimori.Shikimori
import com.gnoemes.shimori.sources.shikimori.ShikimoriApi
import com.gnoemes.shimori.sources.shikimori.mappers.anime.AnimeDetailsToAnimeInfoMapper
import com.gnoemes.shimori.sources.shikimori.mappers.anime.AnimeTracksQueryToAnimeWithTrack
import com.gnoemes.shimori.sources.shikimori.mappers.anime.CalendarMapper
import com.gnoemes.shimori.sources.shikimori.mappers.from
import com.gnoemes.shimori.sources.shikimori.type.UserRateStatusEnum
import me.tatarka.inject.annotations.Inject

@Inject
class ShikimoriAnimeDataSource(
    private val api: ShikimoriApi,
    private val calendarMapper: CalendarMapper,
    private val animeTracksQueryToAnimeWithTrack: AnimeTracksQueryToAnimeWithTrack,
    private val animeDetailsToAnimeInfoMapper: AnimeDetailsToAnimeInfoMapper,
) : AnimeDataSource {
    override suspend fun search(filters: Map<String, String>): List<Anime> {
        return emptyList()
    }

    override suspend fun getCalendar(): List<Anime> {
        return api.anime.calendar()
            .let { calendarMapper.forLists().invoke(it) }
    }

    override suspend fun getWithStatus(
        user: UserShort,
        status: TrackStatus?
    ): List<AnimeWithTrack> {
        return api.rate.graphql(
            AnimeTracksQuery(
                limit = Optional.present(Shikimori.MAX_PAGE_SIZE),
                userId = Optional.present(user.remoteId.toString()),
                status = Optional.presentIfNotNull(UserRateStatusEnum.from(status))
            )
        ).dataAssertNoErrors
            .let {
                animeTracksQueryToAnimeWithTrack.forLists().invoke(it.userRates)
            }
    }

    override suspend fun get(title: Anime): AnimeInfo {
        return api.anime.graphql(
            AnimeDetailsQuery(
                ids = Optional.present(title.id.toString())
            )
        ).dataAssertNoErrors
            .let {
                animeDetailsToAnimeInfoMapper.map(it.animes.first())
            }
    }
}


