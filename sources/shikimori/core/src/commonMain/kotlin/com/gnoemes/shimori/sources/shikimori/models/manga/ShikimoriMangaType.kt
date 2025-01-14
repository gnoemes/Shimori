package com.gnoemes.shimori.sources.shikimori.models.manga

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
enum class ShikimoriMangaType(val type: String) {
    @SerialName("manga")
    MANGA("manga"),
    @SerialName("manhwa")
    MANHWA("manhwa"),
    @SerialName("manhua")
    MANHUA("manhua"),
    @SerialName("novel")
    NOVEL("novel"),
    @SerialName("light_novel")
    LIGHT_NOVEL("light_novel"),
    @SerialName("one_shot")
    ONE_SHOT("one_shot"),
    @SerialName("doujin")
    DOUJIN("doujin")
}