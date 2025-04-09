package com.gnoemes.shimori.source.shikimori.models.common

import com.gnoemes.shimori.source.model.SourceDataType
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
enum class ShikimoriTargetType {
    @SerialName("anime")
    ANIME,

    @SerialName("manga")
    MANGA,

    ;

    companion object {
        fun fromSourceType(type: SourceDataType) = when (type) {
            SourceDataType.Anime -> ShikimoriTargetType.ANIME
            SourceDataType.Manga, SourceDataType.Ranobe -> ShikimoriTargetType.MANGA
            else -> null
        }
    }
}