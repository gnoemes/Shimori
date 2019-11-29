package com.gnoemes.shikimori.services

import com.gnoemes.shikimori.entities.anime.AnimeDetailsResponse
import com.gnoemes.shikimori.entities.anime.AnimeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

internal interface AnimeService {

    @GET("/api/animes")
    suspend fun search(@QueryMap(encoded = true) filters: Map<String, String>): Response<MutableList<AnimeResponse>>

    @GET("/api/animes/{id}")
    suspend fun getDetails(@Path("id") id : Long) : Response<AnimeDetailsResponse>

}