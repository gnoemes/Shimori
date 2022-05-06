package com.gnoemes.shimori.data.shared

import com.squareup.sqldelight.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal typealias RateDAO = comgnoemesshimoridatadb.Rate
internal typealias UserDAO = comgnoemesshimoridatadb.User
internal typealias RateSortDAO = comgnoemesshimoridatadb.Rate_sort
internal typealias LastRequestDAO = comgnoemesshimoridatadb.Last_request
internal typealias AnimeDAO = comgnoemesshimoridatadb.Anime
internal typealias MangaDAO = comgnoemesshimoridatadb.Manga
internal typealias RanobeDAO = comgnoemesshimoridatadb.Ranobe

internal fun <T : Any> Flow<Query<T>>.singleResult(): Flow<T?> =
    this.map { it.executeAsOneOrNull() }


internal inline fun <T : Any, R> Flow<Query<T>>.singleResult(
    crossinline mapper: suspend (value: T?) -> R?
): Flow<R?> =
    this.map { it.executeAsOneOrNull() }
        .map(mapper)

internal inline fun <T : Any, R> Flow<Query<T>>.listResult(
    crossinline mapper: suspend (value: T?) -> R
): Flow<List<R>> =
    this.map { it.executeAsList().map { item -> mapper.invoke(item) } }

internal val Boolean.long: Long
    get() = if (this) 1L else 0L