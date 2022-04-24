package com.gnoemes.shikimori.entities.rates

import com.gnoemes.shikimori.entities.anime.AnimeResponse
import com.gnoemes.shikimori.entities.manga.MangaResponse
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class RateResponse(
    @SerialName("id") val id: Long,
    @SerialName("score") val score: Int,
    @SerialName("status") val status: ShikimoriRateStatus,
    @SerialName("text") val text: String?,
    @SerialName("text_html") val textHtml: String?,
    @SerialName("episodes") val episodes: Int?,
    @SerialName("chapters") val chapters: Int?,
    @SerialName("volumes") val volumes: Int?,
    @SerialName("rewatches") val rewatches: Int?,
    @SerialName("created_at") val createdDateTime: Instant?,
    @SerialName("updated_at") val updatedDateTime: Instant?,
    @SerialName("anime") val anime: AnimeResponse?,
    @SerialName("manga") val manga: MangaResponse?
)