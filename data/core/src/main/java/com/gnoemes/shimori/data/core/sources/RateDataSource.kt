package com.gnoemes.shimori.data.core.sources

import com.gnoemes.shimori.data.core.entities.rate.Rate
import com.gnoemes.shimori.data.core.entities.user.UserShort

interface RateDataSource {

    suspend fun getRates(user: UserShort): List<Rate>

    suspend fun createRate(rate: Rate): Rate

    suspend fun updateRate(rate: Rate): Rate

    suspend fun deleteRate(id: Long)

}