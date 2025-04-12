package com.gnoemes.shimori.data.titles.manga

import com.gnoemes.shimori.data.ShimoriTitleEntity
import com.gnoemes.shimori.data.common.AgeRating
import com.gnoemes.shimori.data.common.ShimoriImage
import com.gnoemes.shimori.data.common.TitleStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import kotlinx.datetime.LocalDate

@kotlinx.serialization.Serializable
data class Manga(
    override val id: Long = 0,
    override val name: String = "",
    override val nameRu: String? = null,
    override val nameEn: String? = null,
    override val image: ShimoriImage? = null,
    override val type: TrackTargetType = TrackTargetType.MANGA,
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
) : ShimoriTitleEntity {
    val chaptersOrUnknown: String get() = chapters.let { if (it == 0) "?" else "$it" }
    override val size: Int? get() = chaptersOrUnknown.toIntOrNull()
}