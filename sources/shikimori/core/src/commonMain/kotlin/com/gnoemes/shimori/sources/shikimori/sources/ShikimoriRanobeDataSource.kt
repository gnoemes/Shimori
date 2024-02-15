package com.gnoemes.shimori.sources.shikimori.sources

import com.apollographql.apollo3.api.Optional
import com.gnoemes.shimori.base.utils.forLists
import com.gnoemes.shimori.data.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.user.UserShort
import com.gnoemes.shimori.source.data.RanobeDataSource
import com.gnoemes.shimori.sources.shikimori.MangaTracksQuery
import com.gnoemes.shimori.sources.shikimori.Shikimori
import com.gnoemes.shimori.sources.shikimori.ShikimoriApi
import com.gnoemes.shimori.sources.shikimori.mappers.from
import com.gnoemes.shimori.sources.shikimori.mappers.ranobe.MangaTracksQueryToRanobeWithTrack
import com.gnoemes.shimori.sources.shikimori.type.UserRateStatusEnum
import me.tatarka.inject.annotations.Inject

@Inject
class ShikimoriRanobeDataSource(
    private val api: ShikimoriApi,
    private val mangaTracksQueryToRanobeWithTrack: MangaTracksQueryToRanobeWithTrack,
) : RanobeDataSource {

    override suspend fun getWithStatus(
        user: UserShort,
        status: TrackStatus?
    ): List<RanobeWithTrack> {
        return api.rate.graphql(
            MangaTracksQuery(
                limit = Optional.present(Shikimori.MAX_PAGE_SIZE),
                userId = Optional.present(user.remoteId.toString()),
                status = Optional.presentIfNotNull(UserRateStatusEnum.from(status))
            )
        ).dataAssertNoErrors
            .let {
                mangaTracksQueryToRanobeWithTrack
                    .forLists()
                    .invoke(it.userRates)
                    //filter ranobe
                    .filterNotNull()
            }
    }

    override suspend fun get(title: Ranobe): RanobeWithTrack {
        error("Not implemented")
    }

}