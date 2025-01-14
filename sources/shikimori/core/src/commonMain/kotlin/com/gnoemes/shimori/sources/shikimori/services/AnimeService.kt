package com.gnoemes.shimori.sources.shikimori.services

import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Query
import com.gnoemes.shimori.sources.shikimori.models.anime.AnimeResponse
import com.gnoemes.shimori.sources.shikimori.models.anime.CalendarResponse

internal interface AnimeService {
    suspend fun <D : Query.Data> graphql(query: Query<D>): ApolloResponse<D>

    suspend fun getSimilar(id: Long): List<AnimeResponse>

    suspend fun calendar(): List<CalendarResponse>
}