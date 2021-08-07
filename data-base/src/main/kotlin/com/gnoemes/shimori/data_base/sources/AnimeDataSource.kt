package com.gnoemes.shimori.data_base.sources

import com.gnoemes.shimori.base.entities.Result
import com.gnoemes.shimori.model.anime.Anime
import com.gnoemes.shimori.model.rate.RateStatus

interface AnimeDataSource {

    suspend fun search(): List<Anime>

    suspend fun getCalendar(): Result<List<Anime>>

    suspend fun getAnimeWithStatus(userId : Long, status: RateStatus?): Result<List<Anime>>
}