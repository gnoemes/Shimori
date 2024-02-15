package com.gnoemes.shimori.data.syncer

import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.logging.api.Logger

abstract class EntityStore(
    protected val logger: Logger,
) {
    protected val tag get() = "EntityStore: $type"
    protected abstract val type: String
    abstract fun <T> trySync(response: SourceResponse<T>)
    abstract fun <E> trySync(sourceId: Long, data: List<E>)
}