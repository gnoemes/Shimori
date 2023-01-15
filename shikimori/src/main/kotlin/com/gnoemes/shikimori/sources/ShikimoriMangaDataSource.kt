package com.gnoemes.shikimori.sources

import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shikimori.mappers.manga.MangaDetailsMapper
import com.gnoemes.shikimori.mappers.manga.RateResponseToMangaWithRateMapper
import com.gnoemes.shikimori.mappers.rate.RateStatusMapper
import com.gnoemes.shikimori.mappers.roles.RolesMapper
import com.gnoemes.shimori.data.core.entities.roles.RolesInfo
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.entities.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.user.UserShort
import com.gnoemes.shimori.data.core.mappers.forLists
import com.gnoemes.shimori.data.core.sources.MangaDataSource

internal class ShikimoriMangaDataSource(
    private val shikimori: Shikimori,
    private val ratedMapper: RateResponseToMangaWithRateMapper,
    private val statusMapper: RateStatusMapper,
    private val detailsMapper: MangaDetailsMapper,
    private val rolesMapper: RolesMapper,
) : MangaDataSource {

    override suspend fun getWithStatus(
        user: UserShort,
        status: TrackStatus?
    ): List<MangaWithTrack> {
        return shikimori.manga.getUserRates(user.remoteId, statusMapper.mapInverse(status))
            .let { ratedMapper.forLists().invoke(it) }
            .filter { it.entity.mangaType != null }
    }

    override suspend fun get(title: Manga): MangaWithTrack {
        return shikimori.manga.getDetails(title.id)
            .let { detailsMapper.map(it) }
    }

    override suspend fun roles(title: Manga): RolesInfo {
        return shikimori.manga.getRoles(title.id)
            .let { rolesMapper.map(it) }
    }
}