package com.gnoemes.shimori.data.core.entities.app

data class SourceResponse<T>(
    val sourceId: Long,
    val data: T
)
