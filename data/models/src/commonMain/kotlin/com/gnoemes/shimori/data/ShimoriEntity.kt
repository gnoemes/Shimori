package com.gnoemes.shimori.data

import com.gnoemes.shimori.data.common.AgeRating
import com.gnoemes.shimori.data.common.Genre
import com.gnoemes.shimori.data.common.ShimoriImage
import com.gnoemes.shimori.data.common.TitleStatus
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
    val genres: List<Genre>?

    val isOngoing get() = status != null && status == TitleStatus.ONGOING
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

