package com.gnoemes.shimori.model.anime

@JvmInline
value class AnimeType private constructor(val type: String) {
    companion object {
        val Tv = AnimeType("tv")
        val Movie = AnimeType("movie")
        val Special = AnimeType("special")
        val Music = AnimeType("music")
        val OVA = AnimeType("ova")
        val ONA = AnimeType("ona")
        val Tv_13 = AnimeType("tv_13")
        val Tv_24 = AnimeType("tv_24")
        val Tv_48 = AnimeType("tv_48")


        fun find(type : String?) = when(type) {
            "tv" -> Tv
            "movie" -> Movie
            "special" -> Special
            "music" -> Music
            "ova" -> OVA
            "ona" -> ONA
            "tv_13" -> Tv_13
            "tv_24" -> Tv_24
            "tv_48" -> Tv_48
            else -> null
        }
    }
}