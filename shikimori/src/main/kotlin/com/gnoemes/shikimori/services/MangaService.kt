package com.gnoemes.shikimori.services

import com.gnoemes.shikimori.entities.common.RelatedResponse
import com.gnoemes.shikimori.entities.common.RolesResponse
import com.gnoemes.shikimori.entities.manga.MangaResponse
import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shimori.model.ShimoriConstants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

internal interface MangaService {

    @GET("/api/mangas")
    suspend fun search(@QueryMap(encoded = true) filters: Map<String, String>): Call<MutableList<MangaResponse>>

//    @GET("/api/mangas/{id}")
//    fun getDetails(@Path("id") id: Long): Call<MangaDetailsResponse>

    @GET("/api/mangas/{id}/roles")
    suspend fun getRoles(@Path("id") id: Long): Call<MutableList<RolesResponse>>

    @GET("/api/mangas/{id}/similar")
    suspend fun getSimilar(@Path("id") id: Long): Call<MutableList<MangaResponse>>

    @GET("/api/mangas/{id}/related")
    suspend fun getRelated(@Path("id") id: Long): Call<MutableList<RelatedResponse>>

//    @GET("/api/mangas/{id}/franchise")
//    fun getFranchise(@Path("id") id: Long): Call<FranchiseResponse>

    @GET("/api/users/{id}/manga_rates")
    suspend fun getUserMangaRates(@Path("id") id: Long,
                                  @Query("status") status: String?,
                                  @Query("page") page: Int = 1,
                                  @Query("limit") limit: Int = ShimoriConstants.MAX_PAGE_SIZE,
                                  @Query("censored") censored: Boolean = true
    ): Call<List<RateResponse>>
}