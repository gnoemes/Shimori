package com.gnoemes.shikimori.repositories

import com.gnoemes.shikimori.mappers.ranobe.RanobeResponseMapper
import com.gnoemes.shikimori.mappers.ranobe.RateResponseToRanobeMapper
import com.gnoemes.shikimori.services.MangaService
import com.gnoemes.shimori.base.extensions.bodyOrThrow
import com.gnoemes.shimori.base.extensions.withRetry
import com.gnoemes.shimori.data_base.mappers.forLists
import com.gnoemes.shimori.data_base.sources.RanobeDataSource
import com.gnoemes.shimori.model.ranobe.Ranobe
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.user.UserShort
import retrofit2.awaitResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ShikimoriRanobeDataSource @Inject constructor(
    private val service: MangaService,
    private val ranobeMapper: RanobeResponseMapper,
    private val rateToRanobeMapper: RateResponseToRanobeMapper
) : RanobeDataSource {

    override suspend fun getRanobeWithStatus(user: UserShort, status: RateStatus?): List<Ranobe> =
        withRetry {
            service.getUserMangaRates(user.shikimoriId!!, status?.shikimoriValue)
                .awaitResponse()
                .let { rateToRanobeMapper.forLists().invoke(it.bodyOrThrow()) }
        }
}