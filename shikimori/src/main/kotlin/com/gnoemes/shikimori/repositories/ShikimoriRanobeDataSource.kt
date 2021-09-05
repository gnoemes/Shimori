package com.gnoemes.shikimori.repositories

import com.gnoemes.shikimori.mappers.ranobe.RanobeResponseMapper
import com.gnoemes.shikimori.mappers.ranobe.RateResponseToRanobeMapper
import com.gnoemes.shikimori.services.MangaService
import com.gnoemes.shimori.base.entities.Result
import com.gnoemes.shimori.base.extensions.toResult
import com.gnoemes.shimori.data_base.mappers.toListMapper
import com.gnoemes.shimori.data_base.sources.RanobeDataSource
import com.gnoemes.shimori.model.ranobe.Ranobe
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.user.UserShort
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ShikimoriRanobeDataSource @Inject constructor(
    private val service: MangaService,
    private val ranobeMapper: RanobeResponseMapper,
    private val rateToRanobeMapper: RateResponseToRanobeMapper
) : RanobeDataSource {

    override suspend fun getRanobeWithStatus(user: UserShort, status: RateStatus?): Result<List<Ranobe>> =
        service.getUserMangaRates(user.shikimoriId!!, status?.shikimoriValue)
            .toResult(rateToRanobeMapper.toListMapper())
}