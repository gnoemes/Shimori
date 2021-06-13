package com.gnoemes.shikimori.entities.common

import com.gnoemes.shikimori.entities.anime.AnimeResponse
import com.gnoemes.shikimori.entities.manga.MangaResponse
import com.google.gson.annotations.SerializedName

internal data class RelatedResponse(
    @field:SerializedName("relation") val relation: String,
    @field:SerializedName("relation_russian") val relationRu: String?,
    @field:SerializedName("anime") val anime: AnimeResponse?,
    @field:SerializedName("manga") val manga: MangaResponse?
)