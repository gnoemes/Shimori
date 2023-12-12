package com.gnoemes.shimori.sources.shikimori.services

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Query
import com.gnoemes.shimori.sources.shikimori.models.manga.MangaResponse

internal interface MangaService {
    suspend fun <D : Query.Data> graphql(query: Query<D>): ApolloResponse<D>

    suspend fun getSimilar(id: Long): List<MangaResponse>
}