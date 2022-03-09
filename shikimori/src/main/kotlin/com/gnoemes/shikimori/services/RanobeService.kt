package com.gnoemes.shikimori.services

import com.gnoemes.shikimori.entities.common.RelatedResponse
import com.gnoemes.shikimori.entities.common.RolesResponse
import com.gnoemes.shikimori.entities.manga.MangaResponse
import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shimori.model.ShimoriConstants
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

internal interface RanobeService {
    @GET("/api/ranobe")
    fun search(@QueryMap(encoded = true) filters: Map<String, String>): Call<MutableList<MangaResponse>>

//    @GET("/api/ranobe/{id}")
//    fun getDetails(@Path("id") id: Long): Call<MangaDetailsResponse>

    @GET("/api/ranobe/{id}/roles")
    fun getRoles(@Path("id") id: Long): Call<MutableList<RolesResponse>>

    @GET("/api/ranobe/{id}/similar")
    fun getSimilar(@Path("id") id: Long): Call<MutableList<MangaResponse>>

    @GET("/api/ranobe/{id}/related")
    fun getRelated(@Path("id") id: Long): Call<MutableList<RelatedResponse>>

//    @GET("/api/mangas/{id}/franchise")
//    fun getFranchise(@Path("id") id: Long): Call<FranchiseResponse>

    @GET("/api/users/{id}/manga_rates")
    fun getUserMangaRates(@Path("id") id: Long,
                                  @Query("status") status: String?,
                                  @Query("page") page: Int = 1,
                                  @Query("limit") limit: Int = ShimoriConstants.MAX_PAGE_SIZE,
                                  @Query("censored") censored: Boolean = true
    ): Call<List<RateResponse>>
}