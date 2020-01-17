package com.gnoemes.shimori.rates

import com.gnoemes.shimori.data_base.mappers.Mapper
import com.gnoemes.shimori.model.rate.Rate
import com.gnoemes.shimori.model.rate.RateStatus
import javax.inject.Inject

internal class RateCategoryMapper @Inject constructor() : Mapper<List<Rate>, List<RateCategory>> {

    override suspend fun map(from: List<Rate>): List<RateCategory> {
        return RateStatus
            .watchingFirstValues()
            .asSequence()
            .associateWith { status -> countStatusSize(from, status) }
            .filter { it.value > 0 }
            .map { RateCategory(it.key, it.value) }
    }

    private fun countStatusSize(rates: List<Rate>, rateStatus: RateStatus): Int =
        rates
            .filter { it.status == rateStatus }
            .size
}