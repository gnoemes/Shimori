package com.gnoemes.shimori.source.shikimori.services

import com.gnoemes.shimori.source.shikimori.models.anime.AnimeResponse
import com.gnoemes.shimori.source.shikimori.models.anime.CalendarResponse

internal interface AnimeService {
    suspend fun getSimilar(id: Long): List<AnimeResponse>

    suspend fun calendar(): List<CalendarResponse>
}