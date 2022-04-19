package com.gnoemes.shikimori.mappers

import com.gnoemes.shikimori.entities.common.GenreResponse
import com.gnoemes.shimori.data.base.entities.common.Genre
import com.gnoemes.shimori.data.base.mappers.Mapper

internal class GenreMapper : Mapper<GenreResponse?, Genre?> {
    override suspend fun map(from: GenreResponse?): Genre? =
        from?.let { Genre.fromShikimori(it.name) }
}