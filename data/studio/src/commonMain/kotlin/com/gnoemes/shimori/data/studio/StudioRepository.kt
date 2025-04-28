package com.gnoemes.shimori.data.studio

import com.gnoemes.shimori.base.extensions.inPast
import com.gnoemes.shimori.data.app.ExpiryConstants
import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.common.Studio
import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import com.gnoemes.shimori.data.lastrequest.EntityLastRequestStore
import com.gnoemes.shimori.data.source.catalogue.CatalogueManager
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.minutes

@Inject
class StudioRepository(
    private val catalogue: CatalogueManager,
    private val store: SyncedStudioStore,
    private val relationsStore: StudioRelationStore,
    private val entityLastRequest: EntityLastRequestStore,
    private val logger: Logger,
    private val transactionRunner: DatabaseTransactionRunner
) {

    fun observeByTitle(
        id: Long,
    ) = store.dao.observeByTitle(id, sourceId = catalogue.current.id)

    suspend fun sync(): SourceResponse<List<Studio>> {
        return catalogue.studio { getAll() }
            .also {
                transactionRunner {
                    store.trySync(it)
                    studiosUpdated(it.sourceId)
                }
            }
    }

    suspend fun <T> trySync(data: SourceResponse<T>) {
        transactionRunner {
            store.trySync(data)
            relationsStore.trySync(data)
        }
    }

    fun needUpdateStudiosForSource(
        sourceId: Long = catalogue.current.id,
        expiry: Instant = ExpiryConstants.SOURCE_SYNC.minutes.inPast
    ) = entityLastRequest.isRequestBefore(
        Request.STUDIOS,
        sourceId,
        expiry
    )

    fun studiosUpdated(sourceId: Long) =
        entityLastRequest.updateLastRequest(Request.STUDIOS, sourceId)
}