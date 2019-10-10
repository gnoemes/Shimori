package com.gnoemes.shimori.model.anime

import com.google.gson.annotations.SerializedName

enum class AnimeType(val type: String) {
    @SerializedName("tv")
    TV("tv"),
    @SerializedName("movie")
    MOVIE("movie"),
    @SerializedName("special")
    SPECIAL("special"),
    @SerializedName("music")
    MUSIC("music"),
    @SerializedName("ova")
    OVA("ova"),
    @SerializedName("ona")
    ONA("ona"),
    @SerializedName("tv_13")
    TV_13("tv_13"),
    @SerializedName("tv_24")
    TV_24("tv_24"),
    @SerializedName("tv_48")
    TV_48("tv_48"),
    @SerializedName("")
    NONE("none");
}