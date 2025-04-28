package com.gnoemes.shimori.source.shikimori.services

import com.gnoemes.shimori.source.shikimori.models.anime.StudioResponse

interface StudioService {
    suspend fun getAll(): List<StudioResponse>
}