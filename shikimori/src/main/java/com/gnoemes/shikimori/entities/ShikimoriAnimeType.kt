package com.gnoemes.shikimori.entities

import com.google.gson.annotations.SerializedName

internal enum class ShikimoriAnimeType {
    @SerializedName("tv")
    TV,
    @SerializedName("movie")
    MOVIE,
    @SerializedName("special")
    SPECIAL,
    @SerializedName("music")
    MUSIC,
    @SerializedName("ova")
    OVA,
    @SerializedName("ona")
    ONA,
    @SerializedName("tv_13")
    TV_13,
    @SerializedName("tv_24")
    TV_24,
    @SerializedName("tv_48")
    TV_48
}