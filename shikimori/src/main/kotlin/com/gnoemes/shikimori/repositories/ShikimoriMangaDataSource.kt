package com.gnoemes.shikimori.repositories

import com.gnoemes.shikimori.mappers.manga.MangaResponseMapper
import com.gnoemes.shikimori.mappers.manga.RateResponseToMangaMapper
import com.gnoemes.shikimori.services.MangaService
import com.gnoemes.shimori.base.extensions.bodyOrThrow
import com.gnoemes.shimori.base.extensions.withRetry
import com.gnoemes.shimori.data_base.mappers.forLists
import com.gnoemes.shimori.data_base.sources.MangaDataSource
import com.gnoemes.shimori.model.manga.Manga
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.user.UserShort
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ShikimoriMangaDataSource @Inject constructor(
    private val service: MangaService,
    private val mangaMapper: MangaResponseMapper,
    private val rateToMangaMapper: RateResponseToMangaMapper
) : MangaDataSource {

    override suspend fun getMangaWithStatus(user: UserShort, status: RateStatus?): List<Manga> =
        withRetry {
            service.getUserMangaRates(user.shikimoriId!!, status?.shikimoriValue)
                .awaitResponse()
                .let { rateToMangaMapper.forLists().invoke(it.bodyOrThrow()) }
        }
}