package com.gnoemes.shimori.data_base.sources

import com.gnoemes.shimori.model.anime.Anime

interface AnimeDataSource {

    suspend fun search() : List<Anime>
}