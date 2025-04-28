package com.gnoemes.shimori.source.shikimori.sources

import com.gnoemes.shimori.source.catalogue.StudioDataSource
import com.gnoemes.shimori.source.model.SStudio
import com.gnoemes.shimori.source.shikimori.ShikimoriApi
import com.gnoemes.shimori.source.shikimori.mappers.StudioResponseMapper
import me.tatarka.inject.annotations.Inject

@Inject
class ShikimoriStudioDataSource(
    private val api: ShikimoriApi,
    private val mapper: StudioResponseMapper
) : StudioDataSource {
    
    override suspend fun getAll(): List<SStudio> {
        return api.studio.getAll().map { mapper.map(it) }
    }
}