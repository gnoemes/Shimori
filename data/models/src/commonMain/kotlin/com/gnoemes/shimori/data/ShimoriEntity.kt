package com.gnoemes.shimori.data

import com.gnoemes.shimori.data.common.AgeRating
import com.gnoemes.shimori.data.common.ShimoriImage
import com.gnoemes.shimori.data.common.TitleStatus
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackTargetType
import kotlinx.datetime.LocalDate

interface ShimoriEntity {
    val id: Long
}

interface ShimoriContentEntity : ShimoriEntity {
    val image: ShimoriImage?
    val name: String
    val nameRu: String?
    val nameEn: String?

    val url: String?
}

interface ShimoriTitleEntity : ShimoriContentEntity {
    val size: Int?
    val type: TrackTargetType

    val rating: Double?
    val status: TitleStatus?
    val dateAired: LocalDate?
    val dateReleased: LocalDate?
    val ageRating: AgeRating
    val description: String?
    val descriptionHtml: String?
    val franchise: String?
    val favorite: Boolean
    val topicId: Long?

    val isOngoing get() = status != null && status == TitleStatus.ONGOING

    fun calculateProgressLimit(): Int {
        return when {
            //we have size, just return it
            size != null -> size!!
            else -> when (this) {
                is Anime -> if (episodes > 0) episodes else episodesAired
                is Manga -> chapters
                is Ranobe -> chapters
                else -> throw IllegalStateException("Unknown title entity: ${this::class}")
            }
        }
    }
}

interface TitleWithTrack<E : ShimoriTitleEntity> {
    val entity: E
    val track: Track?
    val pinned: Boolean

    val id: Long
    val type: TrackTargetType
}

typealias TitleWithTrackEntity = TitleWithTrack<out ShimoriTitleEntity>

interface PaginatedEntity : ShimoriEntity

