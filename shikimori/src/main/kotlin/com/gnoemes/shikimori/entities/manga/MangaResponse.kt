package com.gnoemes.shikimori.entities.manga

import com.gnoemes.shikimori.entities.common.ImageResponse
import com.gnoemes.shimori.model.common.ContentStatus
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

internal data class MangaResponse(
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("russian") val nameRu: String?,
    @field:SerializedName("image") val image: ImageResponse,
    @field:SerializedName("url") val url: String,
    @field:SerializedName("kind") val type: ShikimoriMangaType?,
    @field:SerializedName("score") val score: Double?,
    @field:SerializedName("status") val status: ContentStatus?,
    @field:SerializedName("volumes") val volumes: Int,
    @field:SerializedName("chapters") val chapters: Int,
    @field:SerializedName("aired_on") val dateAired: DateTime?,
    @field:SerializedName("released_on") val dateReleased: DateTime?
) 