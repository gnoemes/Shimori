package com.gnoemes.shimori.model.anime

import com.gnoemes.shimori.model.ContentStatus
import com.gnoemes.shimori.model.ContentType
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.ShimoriImage
import org.joda.time.DateTime

data class Anime(
    override val id: Long,
    override val name: String,
    override val nameRu: String?,
    override val image: ShimoriImage,
    val url: String,
    val type: AnimeType,
    val score: Double?,
    val status: ContentStatus,
    val episodes: Int,
    val episodesAired: Int,
    val dateAired: DateTime?,
    val dateReleased: DateTime?
) : ShimoriEntity {

    override val contentType: ContentType
        get() = ContentType.ANIME
}