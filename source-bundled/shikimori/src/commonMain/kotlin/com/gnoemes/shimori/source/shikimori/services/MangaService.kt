package com.gnoemes.shimori.source.shikimori.services

import com.gnoemes.shimori.source.shikimori.models.manga.MangaResponse

internal interface MangaService {
    suspend fun getSimilar(id: Long): List<MangaResponse>
}