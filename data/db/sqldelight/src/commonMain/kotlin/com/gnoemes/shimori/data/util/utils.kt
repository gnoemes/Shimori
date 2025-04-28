package com.gnoemes.shimori.data.util

import com.gnoemes.shimori.ShimoriDB
import comgnoemesshimori.data.Last_request
import comgnoemesshimori.data.List_sort
import comgnoemesshimori.data.Track
import comgnoemesshimori.data.Track_to_sync
import comgnoemesshimori.data.User

internal typealias TrackDAO = Track
internal typealias UserDAO = User
internal typealias ListSortDAO = List_sort
internal typealias LastRequestDAO = Last_request
internal typealias TrackToSyncDAO = Track_to_sync


internal val Boolean.long: Long
    get() = if (this) 1L else 0L

internal inline fun ShimoriDB.withTransaction(crossinline block: ShimoriDB.() -> Unit) = transaction {
    block()
}