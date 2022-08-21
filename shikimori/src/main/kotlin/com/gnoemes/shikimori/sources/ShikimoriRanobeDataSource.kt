package com.gnoemes.shikimori.sources

import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shikimori.mappers.ranobe.RanobeResponseMapper
import com.gnoemes.shikimori.mappers.ranobe.RateResponseToRanobeWithRateMapper
import com.gnoemes.shikimori.mappers.rate.RateStatusMapper
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeWithRate
import com.gnoemes.shimori.data.core.entities.user.UserShort
import com.gnoemes.shimori.data.core.mappers.forLists
import com.gnoemes.shimori.data.core.sources.RanobeDataSource

internal class ShikimoriRanobeDataSource(
    private val shikimori: Shikimori,
    private val ratedMapper: RateResponseToRanobeWithRateMapper,
    private val statusMapper: RateStatusMapper,
) : RanobeDataSource {

    override suspend fun getWithStatus(user: UserShort, status: RateStatus?): List<RanobeWithRate> {
        return shikimori.ranobe.getUserRates(user.shikimoriId, statusMapper.mapInverse(status))
            .let { ratedMapper.forLists().invoke(it) }
            .filter { it.entity.ranobeType != null }
    }
}