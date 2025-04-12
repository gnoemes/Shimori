package com.gnoemes.shimori.data.util

import com.gnoemes.shimori.data.ShimoriDB

internal typealias TrackDAO = migrations.Track
internal typealias UserDAO = migrations.User
internal typealias ListSortDAO = migrations.List_sort
internal typealias LastRequestDAO = migrations.Last_request
internal typealias TrackToSyncDAO = migrations.Track_to_sync


internal val Boolean.long: Long
    get() = if (this) 1L else 0L

internal inline fun ShimoriDB.withTransaction(crossinline block: ShimoriDB.() -> Unit) = transaction {
    block()
}