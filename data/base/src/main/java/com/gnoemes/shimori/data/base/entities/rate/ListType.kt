package com.gnoemes.shimori.data.base.entities.rate

@JvmInline
@kotlinx.serialization.Serializable
value class ListType private constructor(val type: Int) {
    companion object {
        /**
         * Can contain all [RateTargetType] types
         */
        val Pinned = ListType(0)

        /**
         * Can contain only [RateTargetType.ANIME] type
         */
        val Anime = ListType(1)

        /**
         * Can contain only [RateTargetType.MANGA] type
         */
        val Manga = ListType(2)

        /**
         * Can contain only [RateTargetType.RANOBE] type
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

    val rateType: RateTargetType?
        get() = when (this) {
            Anime -> RateTargetType.ANIME
            Manga -> RateTargetType.MANGA
            Ranobe -> RateTargetType.RANOBE
            else -> null
        }
}