package com.gnoemes.shimori.data.base.sources

import com.gnoemes.shimori.data.base.entities.rate.Rate
import com.gnoemes.shimori.data.base.entities.user.UserShort

interface RateDataSource {

    suspend fun getRates(user: UserShort): List<Rate>

    suspend fun createRate(rate: Rate): Rate

    suspend fun updateRate(rate: Rate): Rate

    suspend fun deleteRate(id: Long)

}