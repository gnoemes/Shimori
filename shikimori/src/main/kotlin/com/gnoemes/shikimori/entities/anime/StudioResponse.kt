package com.gnoemes.shikimori.entities.anime

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class StudioResponse(
        @SerialName("id") val id: Long,
        @SerialName("name") val name: String,
        @SerialName("filtered_name") val nameFiltered: String,
        @SerialName("real") val isReal: Boolean,
        @SerialName("image") val imageUrl: String?
)