package com.gnoemes.shimori.data.core.entities.titles.anime

@JvmInline
@kotlinx.serialization.Serializable
value class AnimeVideoType private constructor(val type: Int) {
    companion object {
        val Opening = AnimeVideoType(0)
        val Ending = AnimeVideoType(1)
        val Promo = AnimeVideoType(2)
        val Commercial = AnimeVideoType(3)
        val Other = AnimeVideoType(4)

        fun find(value: Int?) = when (value) {
            0 -> Opening
            1 -> Ending
            2 -> Promo
            3 -> Commercial
            else -> Other
        }
    }
}