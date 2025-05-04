package com.gnoemes.shimori.data.genre

import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.common.Genre
import com.gnoemes.shimori.data.core.BaseCatalogueRepository
import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import com.gnoemes.shimori.data.lastrequest.EntityLastRequestStore
import com.gnoemes.shimori.data.source.catalogue.CatalogueManager
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.model.SourceDataType
import me.tatarka.inject.annotations.Inject

@Inject
class GenreRepository(
    logger: Logger,
    catalogue: CatalogueManager,
    entityLastRequest: EntityLastRequestStore,
    private val store: SyncedGenreStore,
    private val relationsStore: GenreRelationStore,
    private val transactionRunner: DatabaseTransactionRunner
) : BaseCatalogueRepository<Genre>(
    SourceDataType.Genre,
    logger,
    catalogue,
    entityLastRequest,
    transactionRunner
) {
    override fun queryById(id: Long) = store.dao.queryById(id)

    fun observeByTitle(
        id: Long,
        type: TrackTargetType
    ) = store.dao.observeByTitle(id, type, catalogue.current.id)

    suspend fun sync() = request {
        genre { getAll() }
    }.also {
        transactionRunner {
            store.trySync(it)
            genresUpdated(it.sourceId)
        }
    }

    override fun <T> trySyncTransaction(data: SourceResponse<T>) {
        store.trySync(data)
        relationsStore.trySync(data)
    }

    fun shouldUpdateGenresForSource(
        sourceId: Long = catalogue.current.id,
    ) = shouldUpdate(
        Request.GENRES,
        sourceId,
    )

    private fun genresUpdated(sourceId: Long) = updated(Request.GENRES, sourceId)
}