package com.gnoemes.shikimori.services

import com.gnoemes.shikimori.entities.anime.AnimeDetailsResponse
import com.gnoemes.shikimori.entities.anime.AnimeResponse
import com.gnoemes.shikimori.entities.anime.CalendarResponse
import com.gnoemes.shikimori.entities.anime.ScreenshotResponse
import com.gnoemes.shikimori.entities.common.LinkResponse
import com.gnoemes.shikimori.entities.common.RelatedResponse
import com.gnoemes.shikimori.entities.common.RolesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

internal interface AnimeService {

    @GET("/api/animes")
    suspend fun search(@QueryMap(encoded = true) filters: Map<String, String>): Response<MutableList<AnimeResponse>>

    @GET("/api/animes/{id}")
    suspend fun getDetails(@Path("id") id: Long): Response<AnimeDetailsResponse>

    @GET("/api/animes/{id}/external_links")
    suspend fun getLinks(@Path("id") id: Long): Response<MutableList<LinkResponse>>

    @GET("/api/animes/{id}/similar")
    suspend fun getSimilar(@Path("id") id: Long): Response<MutableList<AnimeResponse>>

    @GET("/api/animes/{id}/related")
    suspend fun getRelated(@Path("id") id: Long): Response<MutableList<RelatedResponse>>

//    @GET("/api/animes/{id}/franchise")
//    suspend fun getFranchise(@Path("id") id: Long): Response<FranchiseResponse>

    @GET("/api/animes/{id}/roles")
    suspend fun getRoles(@Path("id") animeId: Long): Response<MutableList<RolesResponse>>

    @GET("/api/animes/{id}/screenshots")
    suspend fun getScreenshots(@Path("id") animeId: Long): Response<MutableList<ScreenshotResponse>>

    @GET("/api/calendar")
    suspend fun getCalendar(): Response<List<CalendarResponse>>
}