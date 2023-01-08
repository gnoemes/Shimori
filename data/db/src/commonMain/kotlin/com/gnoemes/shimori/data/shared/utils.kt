package com.gnoemes.shimori.data.shared

internal typealias RateDAO = comgnoemesshimoridatadb.data.Rate
internal typealias UserDAO = comgnoemesshimoridatadb.data.User
internal typealias RateSortDAO = comgnoemesshimoridatadb.data.Rate_sort
internal typealias LastRequestDAO = comgnoemesshimoridatadb.data.Last_request
internal typealias RateToSyncDAO = comgnoemesshimoridatadb.data.Rate_to_sync


internal val Boolean.long: Long
    get() = if (this) 1L else 0L