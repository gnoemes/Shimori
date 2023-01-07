package com.gnoemes.shikimori.services

import com.gnoemes.shikimori.entities.roles.CharacterDetailsResponse
import com.gnoemes.shikimori.entities.roles.CharacterResponse

internal interface CharacterService {
    suspend fun search(filters: Map<String, String>): List<CharacterResponse>
    suspend fun getDetails(id: Long): CharacterDetailsResponse
}