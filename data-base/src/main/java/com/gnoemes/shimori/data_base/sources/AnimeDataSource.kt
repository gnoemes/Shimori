package com.gnoemes.shimori.data_base.sources

import com.gnoemes.shimori.base.entities.Result
import com.gnoemes.shimori.model.anime.Anime

interface AnimeDataSource {

    suspend fun search(): List<Anime>

    suspend fun getCalendar(): Result<List<Anime>>
}