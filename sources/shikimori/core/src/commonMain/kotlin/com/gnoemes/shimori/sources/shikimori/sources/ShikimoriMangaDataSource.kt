package com.gnoemes.shimori.sources.shikimori.sources

import com.apollographql.apollo3.api.Optional
import com.gnoemes.shimori.base.utils.forLists
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.user.UserShort
import com.gnoemes.shimori.source.data.MangaDataSource
import com.gnoemes.shimori.sources.shikimori.MangaTracksQuery
import com.gnoemes.shimori.sources.shikimori.Shikimori
import com.gnoemes.shimori.sources.shikimori.ShikimoriApi
import com.gnoemes.shimori.sources.shikimori.mappers.from
import com.gnoemes.shimori.sources.shikimori.mappers.manga.MangaTracksQueryToMangaWithTrack
import com.gnoemes.shimori.sources.shikimori.type.UserRateStatusEnum
import me.tatarka.inject.annotations.Inject

@Inject
class ShikimoriMangaDataSource(
    private val api: ShikimoriApi,
    private val mangaTracksQueryToMangaWithTrack: MangaTracksQueryToMangaWithTrack,
) : MangaDataSource {

    override suspend fun getWithStatus(
        user: UserShort,
        status: TrackStatus?
    ): List<MangaWithTrack> {
        return api.rate.graphql(
            MangaTracksQuery(
                limit = Optional.present(Shikimori.MAX_PAGE_SIZE),
                userId = Optional.present(user.remoteId.toString()),
                status = Optional.presentIfNotNull(UserRateStatusEnum.from(status))
            )
        ).dataAssertNoErrors
            .let {
                mangaTracksQueryToMangaWithTrack
                    .forLists()
                    .invoke(it.userRates)
                    //filter ranobe
                    .filterNotNull()
            }
    }

    override suspend fun get(title: Manga): MangaWithTrack {
        error("Not implemented")
//        return api.anime.graphql(
//            MangaDetailsQuery(
//                ids = Optional.present(title.id.toString())
//            )
//        ).dataAssertNoErrors
//            .let {
//                animeDetailsToAnimeInfoMapper.map(it.animes.first())
//            }
    }
}