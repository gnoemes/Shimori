package com.gnoemes.shimori.sources.shikimori.models.anime

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
enum class ShikimoriAnimeType {
    @SerialName("tv")
    TV,
    @SerialName("movie")
    MOVIE,
    @SerialName("special")
    SPECIAL,
    @SerialName("music")
    MUSIC,
    @SerialName("ova")
    OVA,
    @SerialName("ona")
    ONA,
    @SerialName("tv_13")
    TV_13,
    @SerialName("tv_24")
    TV_24,
    @SerialName("tv_48")
    TV_48
}