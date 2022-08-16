package com.gnoemes.shimori.data.core.sources

import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeWithRate
import com.gnoemes.shimori.data.core.entities.user.UserShort


interface RanobeDataSource {
    suspend fun getWithStatus(user: UserShort, status: RateStatus?): List<RanobeWithRate>
}