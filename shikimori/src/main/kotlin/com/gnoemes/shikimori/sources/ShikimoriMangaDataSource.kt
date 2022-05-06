package com.gnoemes.shikimori.sources

import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shikimori.mappers.manga.MangaResponseMapper
import com.gnoemes.shikimori.mappers.manga.RateResponseToMangaMapper
import com.gnoemes.shikimori.mappers.rate.RateStatusMapper
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.entities.user.UserShort
import com.gnoemes.shimori.data.core.mappers.forLists
import com.gnoemes.shimori.data.core.sources.MangaDataSource

internal class ShikimoriMangaDataSource(
    private val shikimori: Shikimori,
    private val mapper: MangaResponseMapper,
    private val ratedMapper: RateResponseToMangaMapper,
    private val statusMapper: RateStatusMapper,
) : MangaDataSource {

    override suspend fun getWithStatus(user: UserShort, status: RateStatus?): List<Manga> {
        return shikimori.manga.getUserRates(user.shikimoriId, statusMapper.mapInverse(status))
            .let { ratedMapper.forLists().invoke(it) }
            .filter { it.mangaType != null }
    }
}