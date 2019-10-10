package com.gnoemes.shimori.data.entities.network

import com.gnoemes.shimori.model.ContentStatus
import com.gnoemes.shimori.model.anime.AnimeType
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime

data class AnimeResponse(
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("russian") val nameRu: String?,
    @field:SerializedName("image") val image: ImageResponse,
    @field:SerializedName("url") val url: String,
    @field:SerializedName("kind") private val _type: AnimeType?,
    @field:SerializedName("score") val score : Double?,
    @field:SerializedName("status") private val _status: ContentStatus?,
    @field:SerializedName("episodes") val episodes: Int,
    @field:SerializedName("episodes_aired") val episodesAired: Int,
    @field:SerializedName("aired_on") val dateAired: DateTime?,
    @field:SerializedName("released_on") val dateReleased: DateTime?
)  {
    val status: ContentStatus
        get() = _status ?: ContentStatus.NONE

    val type: AnimeType
        get() = _type ?: AnimeType.NONE
}