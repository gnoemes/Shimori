package com.gnoemes.shimori.sources.shikimori.models.manga

import com.gnoemes.shimori.sources.shikimori.models.common.ContentStatus
import com.gnoemes.shimori.sources.shikimori.models.common.ImageResponse
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class MangaResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("russian") val nameRu: String?,
    @SerialName("image") val image: ImageResponse,
    @SerialName("url") val url: String,
    @SerialName("kind") val type: ShikimoriMangaType?,
    @SerialName("score") val score: Double?,
    @SerialName("status") val status: ContentStatus?,
    @SerialName("volumes") val volumes: Int,
    @SerialName("chapters") val chapters: Int,
    @SerialName("aired_on") val dateAired: LocalDate?,
    @SerialName("released_on") val dateReleased: LocalDate?
) 