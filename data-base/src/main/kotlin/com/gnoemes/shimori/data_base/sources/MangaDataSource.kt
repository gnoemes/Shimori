package com.gnoemes.shimori.data_base.sources

import com.gnoemes.shimori.model.manga.Manga
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.user.UserShort

interface MangaDataSource {
    suspend fun getMangaWithStatus(user: UserShort, status: RateStatus?): List<Manga>
}