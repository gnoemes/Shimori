package com.gnoemes.shimori.source.shikimori.sources

import com.apollographql.apollo.api.Optional
import com.gnoemes.shimori.base.utils.forLists
import com.gnoemes.shimori.source.catalogue.RanobeDataSource
import com.gnoemes.shimori.source.model.MalIdArgument
import com.gnoemes.shimori.source.model.SManga
import com.gnoemes.shimori.source.model.STrackStatus
import com.gnoemes.shimori.source.model.SourceIdArgument
import com.gnoemes.shimori.source.shikimori.MangaTracksQuery
import com.gnoemes.shimori.source.shikimori.Shikimori
import com.gnoemes.shimori.source.shikimori.ShikimoriApi
import com.gnoemes.shimori.source.shikimori.mappers.from
import com.gnoemes.shimori.source.shikimori.mappers.manga.MangaOrRanobeTracksQueryToMangaWithTrack
import com.gnoemes.shimori.source.shikimori.type.UserRateStatusEnum
import me.tatarka.inject.annotations.Inject

@Inject
class ShikimoriRanobeDataSource(
    private val api: ShikimoriApi,
    private val mangaTracksQueryToMangaWithTrack: MangaOrRanobeTracksQueryToMangaWithTrack,
) : RanobeDataSource {

    override suspend fun get(id: MalIdArgument): SManga = get(SourceIdArgument(id))
    override suspend fun get(id: SourceIdArgument): SManga {
        error("Not implemented")
    }

    override suspend fun getWithStatus(
        userId: SourceIdArgument,
        status: STrackStatus?
    ): List<SManga> {
        return api.rate.graphql(
            MangaTracksQuery(
                limit = Optional.present(Shikimori.MAX_PAGE_SIZE),
                userId = Optional.present(userId.id.toString()),
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

}