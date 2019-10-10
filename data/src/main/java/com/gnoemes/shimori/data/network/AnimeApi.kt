package com.gnoemes.shimori.data.network

import com.gnoemes.shimori.data.entities.network.AnimeResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface AnimeApi {

    @GET("/api/animes")
    fun search(@QueryMap(encoded = true) filters: Map<String, String>): Deferred<MutableList<AnimeResponse>>

}