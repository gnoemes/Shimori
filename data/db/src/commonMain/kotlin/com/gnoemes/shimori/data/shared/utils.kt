package com.gnoemes.shimori.data.shared

internal typealias TrackDAO = comgnoemesshimoridatadb.data.Track
internal typealias UserDAO = comgnoemesshimoridatadb.data.User
internal typealias ListSortDAO = comgnoemesshimoridatadb.data.List_sort
internal typealias LastRequestDAO = comgnoemesshimoridatadb.data.Last_request
internal typealias TrackToSyncDAO = comgnoemesshimoridatadb.data.Track_to_sync


internal val Boolean.long: Long
    get() = if (this) 1L else 0L