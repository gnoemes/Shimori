package com.gnoemes.shimori.source.model


interface SourceArgument

/**
 * MyAnimeList ID often appears in various sources. Using this argument,
 * we can identify such requests and ensure that we obtain data by utilizing the MAL ID.
 */
data class MalIdArgument(val id: Long) : SourceArgument

/**
 * Use id, which is 'local' for this source.
 * shikimori id, anilist id, etc
 */
data class SourceIdArgument(val id: Long) : SourceArgument {
    constructor(argument : MalIdArgument) : this(id = argument.id)
}