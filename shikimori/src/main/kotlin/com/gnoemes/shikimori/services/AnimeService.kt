package com.gnoemes.shikimori.services

import com.gnoemes.shikimori.MAX_PAGE_SIZE
import com.gnoemes.shikimori.entities.anime.AnimeDetailsResponse
import com.gnoemes.shikimori.entities.anime.AnimeResponse
import com.gnoemes.shikimori.entities.anime.CalendarResponse
import com.gnoemes.shikimori.entities.anime.ScreenshotResponse
import com.gnoemes.shikimori.entities.common.LinkResponse
import com.gnoemes.shikimori.entities.common.RolesResponse
import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shikimori.entities.rates.ShikimoriRateStatus

internal interface AnimeService {
    suspend fun search(filters: Map<String, String>): List<AnimeResponse>
    suspend fun getDetails(id: Long): AnimeDetailsResponse
    suspend fun getLinks(id: Long): List<LinkResponse>
    suspend fun getSimilar(id: Long): List<AnimeResponse>
    suspend fun getRelated(id: Long): List<AnimeResponse>
    suspend fun getRoles(id: Long): List<RolesResponse>
    suspend fun getScreenshots(id: Long): List<ScreenshotResponse>
    suspend fun calendar(): List<CalendarResponse>
    suspend fun getUserRates(
        id: Long,
        status: ShikimoriRateStatus?,
        page: Int = 1,
        limit: Int = MAX_PAGE_SIZE,
        censored: Boolean = true
    ): List<RateResponse>
}