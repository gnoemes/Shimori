package com.gnoemes.shimori.data.app

data class SourceResponse<T>(
    val sourceId: Long,
    val data: T
)


inline fun <E, T> SourceResponse<E>.newData(map: (E) -> T) = SourceResponse(
    sourceId,
    map(data)
)