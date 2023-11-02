package com.gnoemes.shimori.data.app

data class SourceResponse<T>(
    val sourceId: Long,
    val data: T
)
