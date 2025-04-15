package com.gnoemes.shimori.data.source.mapper

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.data.common.Genre
import com.gnoemes.shimori.data.common.GenreType
import com.gnoemes.shimori.source.model.SGenre
import me.tatarka.inject.annotations.Inject

@Inject
class SourceGenreMapper : Mapper<SGenre, Genre> {
    override fun map(from: SGenre): Genre {
        return Genre(
            id = from.id,
            sourceId = -1,
            type = GenreType.find(from.type) ?: GenreType.Tag,
            name = from.name,
            nameRu = from.nameRu,
            description = from.description
        )
    }
}
