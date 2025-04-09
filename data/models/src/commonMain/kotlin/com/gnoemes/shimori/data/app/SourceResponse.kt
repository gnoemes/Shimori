package com.gnoemes.shimori.data.app

import com.gnoemes.shimori.source.model.SourceDataType

data class SourceResponse<T>(
    val params: SourceParams,
    val data: T
) {
    val sourceId get() = params.sourceId
}

data class SourceParams(
    val sourceId: Long,
    val malIdsSupport: List<SourceDataType>,
)


inline fun <E, T> SourceResponse<E>.newData(map: (E) -> T) =
    SourceResponse(
        params,
        map(data)
    )