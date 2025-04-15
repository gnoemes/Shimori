package com.gnoemes.shimori.domain.interactors.source

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.source.catalogue.CatalogueManager
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class ChangeCatalogueSource(
    private val catalogueManager: CatalogueManager,
    private val syncCatalogueSource: SyncCatalogueSource,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<SignInSource.Params, Unit>() {

    override suspend fun doWork(params: SignInSource.Params) {
        withContext(dispatchers.io) {
            catalogueManager.setActiveCatalogue(params.sourceId)
            syncCatalogueSource(SyncCatalogueSource.Params.forceUpdate()).getOrThrow()
        }
    }

    data class Params(
        val sourceId: Long
    )
}