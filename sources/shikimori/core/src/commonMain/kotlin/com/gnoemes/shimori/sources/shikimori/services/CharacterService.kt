package com.gnoemes.shimori.sources.shikimori.services

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Query

internal interface CharacterService {
    suspend fun <D : Query.Data> graphql(query: Query<D>): ApolloResponse<D>
}