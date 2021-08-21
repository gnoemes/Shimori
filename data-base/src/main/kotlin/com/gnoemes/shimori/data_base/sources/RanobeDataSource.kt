package com.gnoemes.shimori.data_base.sources

import com.gnoemes.shimori.base.entities.Result
import com.gnoemes.shimori.model.ranobe.Ranobe
import com.gnoemes.shimori.model.rate.RateStatus

interface RanobeDataSource {
    suspend fun getRanobeWithStatus(userId : Long, status : RateStatus?) : Result<List<Ranobe>>
}