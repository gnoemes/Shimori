package com.gnoemes.shimori.data.source

import com.gnoemes.shimori.data.app.SourceDataType
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.source.mapper.SourceRequestMapper
import com.gnoemes.shimori.source.Source

abstract class SourceManager<S : Source>(
    protected val mapper: SourceRequestMapper,
) {

    fun findRemoteId(sourceId: Long, localId: Long, type: SourceDataType) =
        mapper.findRemoteId(sourceId, localId, type)

    protected suspend fun <DataSource, ResponseType> request(
        source: S,
        dataSource: DataSource,
        action: suspend DataSource.() -> ResponseType
    ) = wrapResponse(source) { action(dataSource) }

    protected suspend fun <DataSource, RequestType, ResponseType> request(
        source: S,
        dataSource: DataSource,
        mapper: SourceRequestMapper,
        type: SourceDataType,
        data: RequestType,
        action: suspend DataSource.(RequestType) -> ResponseType
    ) = wrapResponse(source) {
        val preparedData = mapper(id, type, data)
        action(dataSource, preparedData)
    }

    private suspend fun <T> wrapResponse(
        source: S,
        block: suspend S.() -> T
    ) =
        SourceResponse(
            sourceId = source.id,
            data = block(source)
        )


}