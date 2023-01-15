package com.gnoemes.shikimori.mappers

import com.gnoemes.shikimori.entities.common.GenreResponse
import com.gnoemes.shimori.data.core.entities.common.Genre
import com.gnoemes.shimori.data.core.mappers.Mapper

internal class GenreMapper : Mapper<GenreResponse?, Genre?> {
    override suspend fun map(from: GenreResponse?): Genre? {
        if (from == null) return null

        return Genre(
            //TODO restore
//            id = from.id,
            name = from.name,
            nameRu = from.nameRu,
        )
    }
}