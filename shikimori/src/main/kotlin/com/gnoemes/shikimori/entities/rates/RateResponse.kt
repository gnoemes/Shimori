package com.gnoemes.shikimori.entities.rates

import com.gnoemes.shikimori.entities.anime.AnimeResponse
import com.gnoemes.shikimori.entities.manga.MangaResponse
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

internal data class RateResponse(
        @field:SerializedName("id") val id: Long,
        @field:SerializedName("score") val score: Int,
        @field:SerializedName("status") val status: ShikimoriRateStatus,
        @field:SerializedName("text") val text: String?,
        @field:SerializedName("text_html") val textHtml: String?,
        @field:SerializedName("episodes") val episodes: Int?,
        @field:SerializedName("chapters") val chapters: Int?,
        @field:SerializedName("volumes") val volumes: Int?,
        @field:SerializedName("rewatches") val rewatches: Int?,
        @field:SerializedName("created_at") val createdDateTime: DateTime?,
        @field:SerializedName("updated_at") val updatedDateTime: DateTime?,
        @field:SerializedName("anime") val anime: AnimeResponse?,
        @field:SerializedName("manga") val manga: MangaResponse?
)