package com.gnoemes.shimori.source.shikimori.services

import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Query
import com.gnoemes.shimori.source.shikimori.models.manga.MangaResponse

internal interface RanobeService {
    suspend fun <D : Query.Data> graphql(query: Query<D>): ApolloResponse<D>

    suspend fun getSimilar(id: Long): List<MangaResponse>
}