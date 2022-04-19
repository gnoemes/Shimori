package com.gnoemes.shikimori.sources

import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shikimori.mappers.ranobe.RanobeResponseMapper
import com.gnoemes.shikimori.mappers.ranobe.RateResponseToRanobeMapper
import com.gnoemes.shikimori.mappers.rate.RateStatusMapper
import com.gnoemes.shimori.data.base.entities.rate.RateStatus
import com.gnoemes.shimori.data.base.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.base.entities.user.UserShort
import com.gnoemes.shimori.data.base.mappers.forLists
import com.gnoemes.shimori.data.base.sources.RanobeDataSource

internal class ShikimoriRanobeDataSource(
    private val shikimori: Shikimori,
    private val mapper: RanobeResponseMapper,
    private val ratedMapper: RateResponseToRanobeMapper,
    private val statusMapper: RateStatusMapper,
) : RanobeDataSource {

    override suspend fun getWithStatus(user: UserShort, status: RateStatus?): List<Ranobe> {
        return shikimori.ranobe.getUserRates(user.shikimoriId, statusMapper.mapInverse(status))
            .let { ratedMapper.forLists().invoke(it) }
            .filter { it.ranobeType != null }
    }
}