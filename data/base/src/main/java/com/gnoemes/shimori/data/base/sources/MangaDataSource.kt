package com.gnoemes.shimori.data.base.sources

import com.gnoemes.shimori.data.base.entities.rate.RateStatus
import com.gnoemes.shimori.data.base.entities.titles.manga.Manga
import com.gnoemes.shimori.data.base.entities.user.UserShort

interface MangaDataSource {
    suspend fun getWithStatus(user: UserShort, status: RateStatus?): List<Manga>
}