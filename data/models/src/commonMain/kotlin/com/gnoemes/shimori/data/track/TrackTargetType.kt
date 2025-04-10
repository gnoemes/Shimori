package com.gnoemes.shimori.data.track

import com.gnoemes.shimori.source.model.SourceDataType

enum class TrackTargetType {
    ANIME, MANGA, RANOBE, ;

    val anime get() = this == ANIME
    val manga get() = this == MANGA
    val ranobe get() = this == RANOBE

    override fun toString(): String = name.lowercase()

    val sourceDataType
        get() = when (this) {
            ANIME -> SourceDataType.Anime
            MANGA -> SourceDataType.Manga
            RANOBE -> SourceDataType.Ranobe
        }

    companion object {
        fun find(type: SourceDataType) = when (type) {
            SourceDataType.Anime -> ANIME
            SourceDataType.Manga -> MANGA
            SourceDataType.Ranobe -> RANOBE
            else -> null
        }
    }
}