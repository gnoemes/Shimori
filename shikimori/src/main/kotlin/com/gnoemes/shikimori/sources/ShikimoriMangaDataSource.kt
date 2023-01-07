package com.gnoemes.shikimori.sources

import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shikimori.mappers.manga.MangaDetailsMapper
import com.gnoemes.shikimori.mappers.manga.RateResponseToMangaWithRateMapper
import com.gnoemes.shikimori.mappers.rate.RateStatusMapper
import com.gnoemes.shikimori.mappers.roles.RolesMapper
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.roles.RolesInfo
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.entities.titles.manga.MangaWithRate
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

    override suspend fun getWithStatus(user: UserShort, status: RateStatus?): List<MangaWithRate> {
        return shikimori.manga.getUserRates(user.shikimoriId, statusMapper.mapInverse(status))
            .let { ratedMapper.forLists().invoke(it) }
            .filter { it.entity.mangaType != null }
    }

    override suspend fun get(title: Manga): MangaWithRate {
        return shikimori.manga.getDetails(title.shikimoriId)
            .let { detailsMapper.map(it) }
    }

    override suspend fun roles(title: Manga): RolesInfo {
        return shikimori.manga.getRoles(title.shikimoriId)
            .let { rolesMapper.map(it) }
    }
}