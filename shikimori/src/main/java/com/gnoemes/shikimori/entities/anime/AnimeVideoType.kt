package com.gnoemes.shikimori.entities.anime

import com.google.gson.annotations.SerializedName

internal enum class AnimeVideoType {
    @SerializedName("op")
    OPENING,
    @SerializedName("ed")
    ENDING,
    @SerializedName("pv")
    PROMO,
    @SerializedName("other")
    OTHER;
}