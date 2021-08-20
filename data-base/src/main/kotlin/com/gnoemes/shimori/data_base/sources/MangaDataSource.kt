package com.gnoemes.shimori.data_base.sources

import com.gnoemes.shimori.base.entities.Result
import com.gnoemes.shimori.model.manga.Manga
import com.gnoemes.shimori.model.rate.RateStatus

interface MangaDataSource {

    suspend fun getMangaWithStatus(userId : Long, status : RateStatus?) : Result<List<Manga>>
}