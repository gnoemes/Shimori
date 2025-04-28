package com.gnoemes.shimori.data.genre

import com.gnoemes.shimori.base.extensions.inPast
import com.gnoemes.shimori.data.app.ExpiryConstants
import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.common.Genre
import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import com.gnoemes.shimori.data.lastrequest.EntityLastRequestStore
import com.gnoemes.shimori.data.source.catalogue.CatalogueManager
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.minutes

@Inject
class GenreRepository(
    private val catalogue: CatalogueManager,
    private val store: SyncedGenreStore,
    private val relationsStore: GenreRelationStore,
    private val entityLastRequest: EntityLastRequestStore,
    private val logger: Logger,
    private val transactionRunner: DatabaseTransactionRunner
) {

    fun observeByTitle(
        id: Long,
        type: TrackTargetType
    ) = store.dao.observeByTitle(id, type, catalogue.current.id)

    suspend fun sync(): SourceResponse<List<Genre>> {
        return catalogue.genre { getAll() }
            .also {
                transactionRunner {
                    store.trySync(it)
                    genresUpdated(it.sourceId)
                }
            }
    }

    suspend fun <T> trySync(data: SourceResponse<T>) {
        transactionRunner {
            store.trySync(data)
            relationsStore.trySync(data)
        }
    }

    fun needUpdateGenresForSource(
        sourceId: Long = catalogue.current.id,
        expiry: Instant = ExpiryConstants.SOURCE_SYNC.minutes.inPast
    ) = entityLastRequest.isRequestBefore(
        Request.GENRES,
        sourceId,
        expiry
    )

    fun genresUpdated(sourceId: Long) =
        entityLastRequest.updateLastRequest(Request.GENRES, sourceId)
}