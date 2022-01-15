package com.gnoemes.shimori.data_base.sources

import com.gnoemes.shimori.model.ranobe.Ranobe
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.user.UserShort

interface RanobeDataSource {
    suspend fun getRanobeWithStatus(user: UserShort, status: RateStatus?): List<Ranobe>
}