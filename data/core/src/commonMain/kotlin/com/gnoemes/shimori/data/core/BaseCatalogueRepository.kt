package com.gnoemes.shimori.data.core

import com.gnoemes.shimori.base.extensions.inPast
import com.gnoemes.shimori.data.ShimoriEntity
import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.app.expiry
import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import com.gnoemes.shimori.data.lastrequest.EntityLastRequestStore
import com.gnoemes.shimori.data.source.catalogue.CatalogueManager
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.model.SourceDataType
import kotlin.time.Duration.Companion.minutes

abstract class BaseCatalogueRepository<E : ShimoriEntity>(
    protected val type: SourceDataType,
    protected val logger: Logger,
    protected val catalogue: CatalogueManager,
    private val entityLastRequest: EntityLastRequestStore,
    private val transactionRunner: DatabaseTransactionRunner
) {
    abstract fun queryById(id: Long): E?

    protected abstract fun <T> trySyncTransaction(data: SourceResponse<T>)

    protected inline fun <T> request(
        localId: Long,
        block:  CatalogueManager.(E) -> SourceResponse<T>
    ): SourceResponse<T> {
        log { "Catalogue request. Preparing entity with id: $localId" }
        val local =
            queryById(localId) ?: throw IllegalStateException("$type with id: $localId not found")
        log { "Catalogue request. Entity #$localId found" }
        return block(catalogue, local)
    }

    protected inline fun <T> request(block: CatalogueManager.() -> SourceResponse<T>): SourceResponse<T> {
        log { "Catalogue request" }
        return block(catalogue)
    }

    fun <T> trySync(data: SourceResponse<T>) {
        log { "Try sync from source ${data.sourceId}" }
        transactionRunner {
            trySyncTransaction(data)
        }
    }

    fun shouldUpdate(
        request: Request,
        id: Long,
    ): Boolean {
        val shouldUpdate = entityLastRequest.isRequestBefore(
            request,
            id,
            request.expiry.minutes.inPast
        )
        log { "Should update for $request with id: $id. Should update: $shouldUpdate" }
        return shouldUpdate
    }

    fun updated(request: Request, id: Long) {
        entityLastRequest.updateLastRequest(request, id)
        log { "Entity #$id was updated for $request" }
    }

    protected fun log(message: () -> String = { "" }) {
        logger.d(tag = "[${type}Repository]", message = message)
    }
}