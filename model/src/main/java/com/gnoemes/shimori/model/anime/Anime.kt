package com.gnoemes.shimori.model.anime

import com.gnoemes.shimori.model.ContentStatus
import com.gnoemes.shimori.model.ShimoriImage
import org.joda.time.DateTime

data class Anime(
    val id: Long,
    val name: String,
    val nameRu: String?,
    val image: ShimoriImage,
    val url: String,
    val type: AnimeType,
    val score : Double?,
    val status: ContentStatus,
    val episodes: Int,
    val episodesAired: Int,
    val dateAired: DateTime?,
    val dateReleased: DateTime?
)