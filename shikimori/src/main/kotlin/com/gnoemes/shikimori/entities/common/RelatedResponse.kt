package com.gnoemes.shikimori.entities.common

import com.gnoemes.shikimori.entities.anime.AnimeResponse
import com.gnoemes.shikimori.entities.manga.MangaResponse
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class RelatedResponse(
    @SerialName("relation") val relation: String,
    @SerialName("relation_russian") val relationRu: String?,
    @SerialName("anime") val anime: AnimeResponse?,
    @SerialName("manga") val manga: MangaResponse?
)