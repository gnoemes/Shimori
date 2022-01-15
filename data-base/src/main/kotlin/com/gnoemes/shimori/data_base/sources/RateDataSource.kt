package com.gnoemes.shimori.data_base.sources

import com.gnoemes.shimori.model.rate.Rate
import com.gnoemes.shimori.model.user.UserShort

interface RateDataSource {

    suspend fun getRates(user: UserShort): List<Rate>

    suspend fun createRate(rate: Rate): Rate

    suspend fun updateRate(rate: Rate): Rate

    suspend fun deleteRate(id: Long)

}