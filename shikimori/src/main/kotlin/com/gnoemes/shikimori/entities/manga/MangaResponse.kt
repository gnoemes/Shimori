package com.gnoemes.shikimori.entities.manga

import com.gnoemes.shikimori.entities.common.ContentStatus
import com.gnoemes.shikimori.entities.common.ImageResponse
import kotlinx.datetime.DatePeriod
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class MangaResponse(
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
    @SerialName("aired_on") val dateAired: DatePeriod?,
    @SerialName("released_on") val dateReleased: DatePeriod?
) 