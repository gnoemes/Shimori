package com.gnoemes.shimori.source.shikimori.mappers

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.source.model.SGenre
import com.gnoemes.shimori.source.shikimori.fragment.Genre
import com.gnoemes.shimori.source.shikimori.type.GenreKindEnum
import me.tatarka.inject.annotations.Inject

@Inject
class GenreResponseToSGenreMapper : Mapper<Genre, SGenre> {
    override fun map(from: Genre): SGenre {
        return SGenre(
            id = from.id.toLong(),
            name = from.name,
            nameRu = from.russian,
            type = when (from.kind) {
                GenreKindEnum.genre -> 0
                else -> 1
            },
            description = null
        )
    }


}