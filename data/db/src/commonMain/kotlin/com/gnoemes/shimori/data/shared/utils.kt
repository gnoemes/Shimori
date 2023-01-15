package com.gnoemes.shimori.data.shared

import com.gnoemes.shimori.data.core.entities.app.SourceDataType
import com.gnoemes.shimori.data.db.ShimoriDB
import comgnoemesshimoridatadb.data.Source_ids_sync

internal typealias TrackDAO = comgnoemesshimoridatadb.data.Track
internal typealias UserDAO = comgnoemesshimoridatadb.data.User
internal typealias ListSortDAO = comgnoemesshimoridatadb.data.List_sort
internal typealias LastRequestDAO = comgnoemesshimoridatadb.data.Last_request
internal typealias TrackToSyncDAO = comgnoemesshimoridatadb.data.Track_to_sync


internal val Boolean.long: Long
    get() = if (this) 1L else 0L

internal inline fun ShimoriDB.withTransaction(crossinline block: ShimoriDB.() -> Unit) = transaction {
    block()
}

internal inline fun ShimoriDB.syncRemoteIds(
    sourceId: Long,
    localId: Long,
    remoteId: Long,
    sourceDataType: SourceDataType
) {
    val dataType = sourceDataType.type
    sourceIdsSyncQueries
        .findIdByRemote(sourceId, remoteId, dataType)
        .executeAsOneOrNull()
        .let { id ->
            if (id == null) sourceIdsSyncQueries.insert(
                sourceId,
                localId,
                remoteId,
                dataType
            )
            else sourceIdsSyncQueries.update(
                Source_ids_sync(
                    id,
                    sourceId,
                    localId,
                    remoteId,
                    dataType
                )
            )
        }
}