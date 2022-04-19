package com.gnoemes.shimori.data.base.sources

import com.gnoemes.shimori.data.base.entities.rate.RateStatus
import com.gnoemes.shimori.data.base.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.base.entities.user.UserShort


interface RanobeDataSource {
    suspend fun getWithStatus(user: UserShort, status: RateStatus?): List<Ranobe>
}