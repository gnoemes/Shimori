package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.data.source.catalogue.CatalogueManager
import com.gnoemes.shimori.data.source.track.TrackManager
import com.gnoemes.shimori.domain.Interactor
import com.gnoemes.shimori.source.Source
import me.tatarka.inject.annotations.Inject

@Inject
class GetSource(
    private val trackManager: TrackManager,
    private val catalogueManager: CatalogueManager,
) : Interactor<GetSource.Params, Source?>() {
    override suspend fun doWork(params: Params): Source? {
        return (catalogueManager.catalogs + trackManager.trackers).find { it.id == params.sourceId }
    }

    data class Params(
        val sourceId: Long
    )
}