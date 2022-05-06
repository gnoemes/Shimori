package com.gnoemes.shimori.data.shared

internal typealias RateDAO = comgnoemesshimoridatadb.Rate
internal typealias UserDAO = comgnoemesshimoridatadb.User
internal typealias RateSortDAO = comgnoemesshimoridatadb.Rate_sort
internal typealias LastRequestDAO = comgnoemesshimoridatadb.Last_request
internal typealias AnimeDAO = comgnoemesshimoridatadb.Anime
internal typealias MangaDAO = comgnoemesshimoridatadb.Manga
internal typealias RanobeDAO = comgnoemesshimoridatadb.Ranobe


internal val Boolean.long: Long
    get() = if (this) 1L else 0L