package com.gnoemes.shikimori.mappers

import com.gnoemes.shikimori.entities.common.GenreResponse
import com.gnoemes.shimori.data_base.mappers.Mapper
import com.gnoemes.shimori.model.common.Genre
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class GenreMapper @Inject constructor() : Mapper<GenreResponse?, Genre?> {
    override suspend fun map(from: GenreResponse?): Genre? =
        from?.let { Genre.fromShikimori(it.name) }
}