package com.gnoemes.shikimori.services

import com.gnoemes.shikimori.MAX_PAGE_SIZE
import com.gnoemes.shikimori.entities.common.LinkResponse
import com.gnoemes.shikimori.entities.common.RolesResponse
import com.gnoemes.shikimori.entities.manga.MangaResponse
import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shikimori.entities.rates.ShikimoriRateStatus

internal interface RanobeService {
    suspend fun search(filters: Map<String, String>): List<MangaResponse>
    suspend fun getLinks(id: Long): List<LinkResponse>
    suspend fun getSimilar(id: Long): List<MangaResponse>
    suspend fun getRelated(id: Long): List<MangaResponse>
    suspend fun getRoles(id: Long): List<RolesResponse>
    suspend fun getUserRates(
        id: Long,
        status: ShikimoriRateStatus?,
        page: Int = 1,
        limit: Int = MAX_PAGE_SIZE,
        censored: Boolean = true
    ): List<RateResponse>
}