package com.gnoemes.shimori.data.base.entities

import com.gnoemes.shimori.data.base.entities.common.AgeRating
import com.gnoemes.shimori.data.base.entities.common.Genre
import com.gnoemes.shimori.data.base.entities.common.ShimoriImage
import com.gnoemes.shimori.data.base.entities.common.TitleStatus
import com.gnoemes.shimori.data.base.entities.rate.Rate
import com.gnoemes.shimori.data.base.entities.rate.RateTargetType
import com.gnoemes.shimori.data.base.utils.Shikimori
import kotlinx.datetime.DatePeriod

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
    val dateAired: DatePeriod?
    val dateReleased: DatePeriod?
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

