package com.gnoemes.shimori.data_base.sources

import com.gnoemes.shimori.base.entities.Result
import com.gnoemes.shimori.model.rate.Rate

interface RateDataSource {

    suspend fun getRates(userId: Long): Result<List<Rate>>

    suspend fun createRate(rate: Rate): Result<Rate>

    suspend fun updateRate(rate: Rate): Result<Rate>

    suspend fun deleteRate(id: Long) : Result<Unit>
}