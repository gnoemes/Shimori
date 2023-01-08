package com.gnoemes.shimori.data.core.entities.track

@JvmInline
@kotlinx.serialization.Serializable
value class ListType private constructor(val type: Int) {
    companion object {
        /**
         * Can contain all [TrackTargetType] types
         */
        val Pinned = ListType(0)

        /**
         * Can contain only [TrackTargetType.ANIME] type
         */
        val Anime = ListType(1)

        /**
         * Can contain only [TrackTargetType.MANGA] type
         */
        val Manga = ListType(2)

        /**
         * Can contain only [TrackTargetType.RANOBE] type
         */
        val Ranobe = ListType(3)

        fun findOrDefault(type: Int) = when (type) {
            0 -> Pinned
            1 -> Anime
            2 -> Manga
            3 -> Ranobe
            else -> Anime
        }
    }

    val trackType: TrackTargetType?
        get() = when (this) {
            Anime -> TrackTargetType.ANIME
            Manga -> TrackTargetType.MANGA
            Ranobe -> TrackTargetType.RANOBE
            else -> null
        }
}