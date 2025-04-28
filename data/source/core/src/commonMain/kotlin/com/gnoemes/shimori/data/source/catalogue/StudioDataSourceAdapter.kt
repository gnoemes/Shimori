package com.gnoemes.shimori.data.source.catalogue

import com.gnoemes.shimori.base.utils.forLists
import com.gnoemes.shimori.data.common.Studio
import com.gnoemes.shimori.data.source.mapper.SourceRequestMapper
import com.gnoemes.shimori.data.source.mapper.SourceStudioMapper
import com.gnoemes.shimori.source.Source
import com.gnoemes.shimori.source.catalogue.StudioDataSource
import me.tatarka.inject.annotations.Inject

@Inject
class StudioDataSourceAdapter(
    private val requestMapper: SourceRequestMapper,
    private val mapper: SourceStudioMapper,
) {

    suspend inline operator fun <ResponseType> invoke(
        crossinline action: suspend StudioDataSourceAdapter.() -> suspend (StudioDataSource.(Source) -> ResponseType)
    ): suspend StudioDataSource.(Source) -> ResponseType {
        val wrap: suspend StudioDataSourceAdapter.() -> suspend (StudioDataSource.(Source) -> ResponseType) =
            { action() }
        return wrap()
    }

    fun getAll(): suspend StudioDataSource.(Source) -> List<Studio> = { source ->
        getAll().let {
            mapper.forLists().invoke(it)
                .map { studio -> studio.copy(sourceId = source.id) }
        }
    }


}
