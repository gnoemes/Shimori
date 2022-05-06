package com.gnoemes.shimori.data.core.entities

import com.gnoemes.shimori.data.core.entities.common.AgeRating
import com.gnoemes.shimori.data.core.entities.common.Genre
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.common.TitleStatus
import com.gnoemes.shimori.data.core.entities.rate.Rate
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.core.utils.Shikimori
import kotlinx.datetime.LocalDate

interface ShimoriEntity {
    val id: Long
}

@Shikimori
interface ShikimoriEntity {
    val shikimoriId: Long

    val hasShikimoriId get() = shikimoriId != 0L
}

interface ShimoriTitleEntity : ShimoriEntity, ShikimoriEntity {
    val image: ShimoriImage?
    val name: String
    val nameEn: String?
    val nameRu: String?
    val size: Int?
    val type: RateTargetType

    val url: String?
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

interface TitleWithRate<E : ShimoriTitleEntity> {
    val entity: E
    val rate: Rate?

    val id: Long
    val type: RateTargetType
}

