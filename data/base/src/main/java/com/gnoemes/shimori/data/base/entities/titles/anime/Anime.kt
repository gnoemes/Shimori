package com.gnoemes.shimori.data.base.entities.titles.anime

import com.gnoemes.shimori.data.base.entities.ShimoriEntity
import com.gnoemes.shimori.data.base.entities.ShimoriTitleEntity
import com.gnoemes.shimori.data.base.entities.common.AgeRating
import com.gnoemes.shimori.data.base.entities.common.Genre
import com.gnoemes.shimori.data.base.entities.common.ShimoriImage
import com.gnoemes.shimori.data.base.entities.common.TitleStatus
import com.gnoemes.shimori.data.base.entities.rate.RateTargetType
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimePeriod

@kotlinx.serialization.Serializable
data class Anime(
    override val id: Long = 0,
    override val shikimoriId: Long = 0,
    override val name: String = "",
    override val nameRu: String? = null,
    override val nameEn: String? = null,
    override val image: ShimoriImage? = null,
    override val type: RateTargetType = RateTargetType.ANIME,
    override val url: String? = null,
    val animeType: AnimeType? = null,
    override val rating: Double? = null,
    override val status: TitleStatus? = null,
    val episodes: Int = 0,
    val episodesAired: Int = 0,
    override val dateAired: DatePeriod? = null,
    override val dateReleased: DatePeriod? = null,
    val nextEpisode: Int? = null,
    val nextEpisodeDate: DateTimePeriod? = null,
    val nextEpisodeEndDate: DateTimePeriod? = null,
    override val ageRating: AgeRating = AgeRating.NONE,
    val duration: DateTimePeriod? = null,
    override val description: String? = null,
    override val descriptionHtml: String? = null,
    override val franchise: String? = null,
    override val favorite: Boolean = false,
    override val topicId: Long? = null,
    override val genres: List<Genre>? = null
) : ShimoriEntity, ShimoriTitleEntity {
    val episodesOrUnknown: String get() = episodes.let { if (it == 0) "?" else "$it" }
    val isMovie: Boolean get() = animeType == AnimeType.Movie
    override val size: Int? get() = episodesOrUnknown.toIntOrNull()
}