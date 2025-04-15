package com.gnoemes.shimori.source.shikimori.services

import com.apollographql.apollo.api.ApolloResponse
import com.apollographql.apollo.api.Query

internal interface GraphQlService {
    suspend fun <D : Query.Data> query(query: Query<D>): ApolloResponse<D>
}