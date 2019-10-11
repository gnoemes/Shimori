package com.gnoemes.shimori.data.remote

import com.gnoemes.shimori.data.entities.network.AnimeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface AnimeApi {

    @GET("/api/animes")
    suspend fun search(@QueryMap(encoded = true) filters: Map<String, String>): Response<MutableList<AnimeResponse>>

}