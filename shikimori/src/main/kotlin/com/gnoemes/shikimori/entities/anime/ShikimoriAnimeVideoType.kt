package com.gnoemes.shikimori.entities.anime

import kotlinx.serialization.SerialName


@kotlinx.serialization.Serializable
internal enum class ShikimoriAnimeVideoType {
    @SerialName("op")
    OPENING,
    @SerialName("ed")
    ENDING,
    @SerialName("pv")
    PROMO,
    @SerialName("other")
    OTHER;
}