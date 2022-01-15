package com.gnoemes.shikimori.services

import com.gnoemes.shikimori.entities.anime.AnimeDetailsResponse
import com.gnoemes.shikimori.entities.anime.AnimeResponse
import com.gnoemes.shikimori.entities.anime.CalendarResponse
import com.gnoemes.shikimori.entities.anime.ScreenshotResponse
import com.gnoemes.shikimori.entities.common.LinkResponse
import com.gnoemes.shikimori.entities.common.RelatedResponse
import com.gnoemes.shikimori.entities.common.RolesResponse
import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shimori.model.ShimoriConstants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

internal interface AnimeService {

    @GET("/api/animes")
    suspend fun search(@QueryMap(encoded = true) filters: Map<String, String>): Call<MutableList<AnimeResponse>>

    @GET("/api/animes/{id}")
    suspend fun getDetails(@Path("id") id: Long): Call<AnimeDetailsResponse>

    @GET("/api/animes/{id}/external_links")
    suspend fun getLinks(@Path("id") id: Long): Call<MutableList<LinkResponse>>

    @GET("/api/animes/{id}/similar")
    suspend fun getSimilar(@Path("id") id: Long): Call<MutableList<AnimeResponse>>

    @GET("/api/animes/{id}/related")
    suspend fun getRelated(@Path("id") id: Long): Call<MutableList<RelatedResponse>>

//    @GET("/api/animes/{id}/franchise")
//    suspend fun getFranchise(@Path("id") id: Long): Call<FranchiseResponse>

    @GET("/api/animes/{id}/roles")
    suspend fun getRoles(@Path("id") animeId: Long): Call<MutableList<RolesResponse>>

    @GET("/api/animes/{id}/screenshots")
    suspend fun getScreenshots(@Path("id") animeId: Long): Call<MutableList<ScreenshotResponse>>

    @GET("/api/calendar")
    suspend fun getCalendar(): Call<List<CalendarResponse>>

    @GET("/api/users/{id}/anime_rates")
    suspend fun getUserAnimeRates(@Path("id") id: Long,
                          @Query("status") status: String?,
                          @Query("page") page: Int = 1,
                          @Query("limit") limit: Int = ShimoriConstants.MAX_PAGE_SIZE,
                          @Query("censored") censored: Boolean = true
    ): Call<List<RateResponse>>
}