package com.gnoemes.shimori.data.studio

import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.common.Studio
import com.gnoemes.shimori.data.core.BaseCatalogueRepository
import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import com.gnoemes.shimori.data.lastrequest.EntityLastRequestStore
import com.gnoemes.shimori.data.source.catalogue.CatalogueManager
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.model.SourceDataType
import me.tatarka.inject.annotations.Inject

@Inject
class StudioRepository(
    logger: Logger,
    catalogue: CatalogueManager,
    entityLastRequest: EntityLastRequestStore,
    private val store: SyncedStudioStore,
    private val relationsStore: StudioRelationStore,
    private val transactionRunner: DatabaseTransactionRunner
) : BaseCatalogueRepository<Studio>(
    SourceDataType.Studio,
    logger,
    catalogue,
    entityLastRequest,
    transactionRunner
) {
    override fun queryById(id: Long) = store.dao.queryById(id)
    fun observeByTitle(
        id: Long,
    ) = store.dao.observeByTitle(id, sourceId = catalogue.current.id)

    suspend fun sync() = request {
        studio { getAll() }
    }.also {
        transactionRunner {
            store.trySync(it)
            studiosUpdated(it.sourceId)
        }
    }

    override fun <T> trySyncTransaction(data: SourceResponse<T>) {
        transactionRunner {
            store.trySync(data)
            relationsStore.trySync(data)
        }
    }

    fun shouldUpdateStudiosForSource(
        sourceId: Long = catalogue.current.id,
    ) = shouldUpdate(
        Request.STUDIOS,
        sourceId,
    )

    private fun studiosUpdated(sourceId: Long) = updated(Request.STUDIOS, sourceId)
}