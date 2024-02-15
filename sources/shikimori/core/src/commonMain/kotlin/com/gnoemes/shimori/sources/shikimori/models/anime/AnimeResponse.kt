package com.gnoemes.shimori.sources.shikimori.models.anime

import com.gnoemes.shimori.sources.shikimori.models.common.ContentStatus
import com.gnoemes.shimori.sources.shikimori.models.common.ImageResponse
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class AnimeResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("russian") val nameRu: String?,
    @SerialName("image") val image: ImageResponse,
    @SerialName("url") val url: String,
    @SerialName("kind") val type: ShikimoriAnimeType?,
    @SerialName("score") val score: Double?,
    @SerialName("status") val status: ContentStatus?,
    @SerialName("episodes") val episodes: Int,
    @SerialName("episodes_aired") val episodesAired: Int,
    @SerialName("aired_on") val dateAired: LocalDate?,
    @SerialName("released_on") val dateReleased: LocalDate?
)