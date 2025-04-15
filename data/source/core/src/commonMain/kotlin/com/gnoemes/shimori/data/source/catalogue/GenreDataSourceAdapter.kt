package com.gnoemes.shimori.data.source.catalogue

import com.gnoemes.shimori.base.utils.forLists
import com.gnoemes.shimori.data.common.Genre
import com.gnoemes.shimori.data.source.mapper.SourceGenreMapper
import com.gnoemes.shimori.source.Source
import com.gnoemes.shimori.source.catalogue.GenreDataSource
import me.tatarka.inject.annotations.Inject

@Inject
class GenreDataSourceAdapter(
    private val mapper: SourceGenreMapper,
) {

    suspend inline operator fun <ResponseType> invoke(
        crossinline action: suspend GenreDataSourceAdapter.() -> suspend (GenreDataSource.(Source) -> ResponseType)
    ): suspend GenreDataSource.(Source) -> ResponseType {
        val wrap: suspend GenreDataSourceAdapter.() -> suspend (GenreDataSource.(Source) -> ResponseType) =
            { action() }
        return wrap()
    }

    fun getAll(): suspend GenreDataSource.(Source) -> List<Genre> = { source ->
        getAll().let {
            mapper.forLists().invoke(it)
                .map { genre -> genre.copy(sourceId = source.id) }
        }
    }
}