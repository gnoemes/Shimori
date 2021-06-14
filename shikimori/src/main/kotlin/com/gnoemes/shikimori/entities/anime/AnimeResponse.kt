package com.gnoemes.shikimori.entities.anime

import com.gnoemes.shikimori.entities.common.ImageResponse
import com.gnoemes.shimori.model.common.ContentStatus
import com.google.gson.annotations.SerializedName
import org.threeten.bp.OffsetDateTime

internal data class AnimeResponse(
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("russian") val nameRu: String?,
    @field:SerializedName("image") val image: ImageResponse,
    @field:SerializedName("url") val url: String,
    @field:SerializedName("kind") val type: ShikimoriAnimeType?,
    @field:SerializedName("score") val score: Double?,
    @field:SerializedName("status") val status: ContentStatus?,
    @field:SerializedName("episodes") val episodes: Int,
    @field:SerializedName("episodes_aired") val episodesAired: Int,
    @field:SerializedName("aired_on") val dateAired: OffsetDateTime?,
    @field:SerializedName("released_on") val dateReleased: OffsetDateTime?
)