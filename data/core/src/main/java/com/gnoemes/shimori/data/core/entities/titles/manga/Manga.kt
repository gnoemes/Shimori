package com.gnoemes.shimori.data.core.entities.titles.manga

import com.gnoemes.shimori.data.core.entities.ShikimoriEntity
import com.gnoemes.shimori.data.core.entities.ShimoriTitleEntity
import com.gnoemes.shimori.data.core.entities.common.AgeRating
import com.gnoemes.shimori.data.core.entities.common.Genre
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.common.TitleStatus
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import kotlinx.datetime.LocalDate

@kotlinx.serialization.Serializable
data class Manga(
    override val id: Long = 0,
    override val shikimoriId: Long = 0,
    override val name: String = "",
    override val nameRu: String? = null,
    override val nameEn: String? = null,
    override val image: ShimoriImage? = null,
    override val type: RateTargetType = RateTargetType.MANGA,
    override val url: String? = null,
    val mangaType: MangaType? = null,
    override val rating: Double? = null,
    override val status: TitleStatus? = null,
    val volumes: Int = 0,
    val chapters: Int = 0,
    override val dateAired: LocalDate? = null,
    override val dateReleased: LocalDate? = null,
    override val ageRating: AgeRating = AgeRating.NONE,
    override val description: String? = null,
    override val descriptionHtml: String? = null,
    override val franchise: String? = null,
    override val favorite: Boolean = false,
    override val topicId: Long? = null,
    override val genres: List<Genre>? = null
) : ShimoriTitleEntity, ShikimoriEntity {
    val chaptersOrUnknown: String get() = chapters.let { if (it == 0) "?" else "$it" }
    override val size: Int? get() = chaptersOrUnknown.toIntOrNull()
}