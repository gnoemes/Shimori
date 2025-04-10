package com.gnoemes.shimori.data.titles.anime

@JvmInline
@kotlinx.serialization.Serializable
value class AnimeVideoType private constructor(val type: Int) {
    companion object {
        val Opening = AnimeVideoType(0)
        val Ending = AnimeVideoType(1)
        val Music = AnimeVideoType(2)
        val Clip = AnimeVideoType(3)
        val CharacterTrailer = AnimeVideoType(4)
        val Promo = AnimeVideoType(5)
        val Commercial = AnimeVideoType(6)
        val EpisodePreview = AnimeVideoType(7)
        val Other = AnimeVideoType(8)

        fun find(value: Int?) = when (value) {
            0 -> Opening
            1 -> Ending
            2 -> Music
            3 -> Clip
            4 -> CharacterTrailer
            5 -> Promo
            6 -> Commercial
            7 -> EpisodePreview
            else -> Other
        }

        fun find(value: String?) = when (value) {
            "op" -> Opening
            "ed" -> Ending
            "op_ed_clip" -> Music
            "clip" -> Clip
            "character_trailer" -> CharacterTrailer
            "pv" -> Promo
            "cm" -> Commercial
            "episode_preview" -> EpisodePreview
            else -> Other
        }
    }
}