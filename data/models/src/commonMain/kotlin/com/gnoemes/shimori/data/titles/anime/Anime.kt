package com.gnoemes.shimori.data.titles.anime

import com.gnoemes.shimori.data.ShimoriTitleEntity
import com.gnoemes.shimori.data.common.AgeRating
import com.gnoemes.shimori.data.common.ShimoriImage
import com.gnoemes.shimori.data.common.TitleStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

@kotlinx.serialization.Serializable
data class Anime(
    override val id: Long = 0,
    override val name: String = "",
    override val nameRu: String? = null,
    override val nameEn: String? = null,
    override val image: ShimoriImage? = null,
    override val type: TrackTargetType = TrackTargetType.ANIME,
    override val url: String? = null,
    val animeType: AnimeType? = null,
    override val rating: Double? = null,
    override val status: TitleStatus? = null,
    val episodes: Int = 0,
    val episodesAired: Int = 0,
    override val dateAired: LocalDate? = null,
    override val dateReleased: LocalDate? = null,
    val nextEpisode: Int? = null,
    val nextEpisodeDate: Instant? = null,
    override val ageRating: AgeRating = AgeRating.NONE,
    //minutes
    val duration: Int? = null,
    override val description: String? = null,
    override val descriptionHtml: String? = null,
    override val franchise: String? = null,
    override val favorite: Boolean = false,
    override val topicId: Long? = null,
    val dubbers: List<String>? = null,
    val subbers: List<String>? = null,
) : ShimoriTitleEntity {
    val episodesOrUnknown: String get() = episodes.let { if (it == 0) "?" else "$it" }
    val isMovie: Boolean get() = animeType == AnimeType.Movie
    override val size: Int? get() = episodesOrUnknown.toIntOrNull()
    val commonTranslators: List<String>
        get() = dubbers
            .orEmpty()
            .intersect(subbers.orEmpty().toSet())
            .toList()
}