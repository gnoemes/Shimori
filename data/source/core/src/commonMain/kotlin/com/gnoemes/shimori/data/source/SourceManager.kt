package com.gnoemes.shimori.data.source

import com.gnoemes.shimori.data.app.SourceParams
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.source.Source
import com.gnoemes.shimori.source.model.SourceDataType

abstract class SourceManager<S : Source>(
    private val dao: SourceIdsSyncDao,
) {

    fun findRemoteId(sourceId: Long, localId: Long, type: SourceDataType) =
        dao.findRemoteId(sourceId, localId, type)

    protected suspend fun <DataSource, ResponseType> request(
        source: S,
        dataSource: DataSource,
        action: suspend DataSource.(S) -> ResponseType
    ) = wrapResponse(source) { action(dataSource, source) }


    private suspend fun <ResponseType> wrapResponse(
        source: S,
        block: suspend S.() -> ResponseType
    ) =
        SourceResponse(
            params = SourceParams(
                source.id,
                source.malIdsSupport
            ),
            data = block(source)
        )


}