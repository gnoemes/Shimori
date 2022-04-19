package com.gnoemes.shimori.data.base.entities.titles.manga

import com.gnoemes.shimori.data.base.entities.ShikimoriEntity
import com.gnoemes.shimori.data.base.entities.ShimoriTitleEntity
import com.gnoemes.shimori.data.base.entities.common.AgeRating
import com.gnoemes.shimori.data.base.entities.common.Genre
import com.gnoemes.shimori.data.base.entities.common.ShimoriImage
import com.gnoemes.shimori.data.base.entities.common.TitleStatus
import com.gnoemes.shimori.data.base.entities.rate.RateTargetType
import kotlinx.datetime.DatePeriod

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
    private val manga_type: String? = null,
    override val rating: Double? = null,
    override val status: TitleStatus? = null,
    val volumes: Int = 0,
    val chapters: Int = 0,
    override val dateAired: DatePeriod? = null,
    override val dateReleased: DatePeriod? = null,
    override val ageRating: AgeRating = AgeRating.NONE,
    override val description: String? = null,
    override val descriptionHtml: String? = null,
    override val franchise: String? = null,
    override val favorite: Boolean = false,
    override val topicId: Long? = null,
    override val genres: List<Genre>? = null
) : ShimoriTitleEntity, ShikimoriEntity {
    val mangaType = MangaType.find(manga_type)
    val chaptersOrUnknown: String get() = chapters.let { if (it == 0) "?" else "$it" }
    override val size: Int? get() = chaptersOrUnknown.toIntOrNull()
}