package com.gnoemes.shikimori.entities.manga

import com.google.gson.annotations.SerializedName

internal enum class ShikimoriMangaType(val type: String) {
    @SerializedName("manga")
    MANGA("manga"),
    @SerializedName("manhwa")
    MANHWA("manhwa"),
    @SerializedName("manhua")
    MANHUA("manhua"),
    @SerializedName("novel")
    NOVEL("novel"),
    @SerializedName("light_novel")
    LIGHT_NOVEL("light_novel"),
    @SerializedName("one_shot")
    ONE_SHOT("one_shot"),
    @SerializedName("doujin")
    DOUJIN("doujin")
}