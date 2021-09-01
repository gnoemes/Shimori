package com.gnoemes.shimori.model.anime

import androidx.room.*
import com.gnoemes.shimori.model.ShikimoriContentEntity
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.common.*
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "animes",
        indices = [
            Index(value = ["anime_shikimori_id"], unique = true),
            Index(value = ["next_episode_date"])
        ]
)
data class Anime(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "anime_shikimori_id") override val shikimoriId: Long? = null,
    override val name: String = "",
    @ColumnInfo(name = "name_ru") override val nameRu: String? = null,
    @ColumnInfo(name = "name_eng") val nameEng: String? = null,
    @Embedded(prefix = "image_") override val image: ShimoriImage? = null,
    val url: String? = null,
    @ColumnInfo(name = "type") val _type: String? = null,
    @ColumnInfo(name = "rating") val score: Double? = null,
    @ColumnInfo(name = "anime_status") val status: ContentStatus? = null,
    @ColumnInfo(name = "episodes_size") val episodes: Int = 0,
    val episodesAired: Int = 0,
    @ColumnInfo(name = "date_aired") val dateAired: LocalDate? = null,
    @ColumnInfo(name = "date_released") val dateReleased: LocalDate? = null,
    @ColumnInfo(name = "next_episode") val nextEpisode: Int? = null,
    @ColumnInfo(name = "next_episode_date") val nextEpisodeDate: OffsetDateTime? = null,
    @ColumnInfo(name = "next_episode_end_date") val nextEpisodeEndDate: OffsetDateTime? = null,
    @ColumnInfo(name = "age_rating") val ageRating: AgeRating? = null,
    val duration: OffsetDateTime? = null,
    val description: String? = null,
    @ColumnInfo(name = "description_html") val descriptionHtml: String? = null,
    val franchise: String? = null,
    val favorite: Boolean = false,
    @ColumnInfo(name = "topic_id") val topicId: Long? = null,
    val genres: List<Genre>? = null
) : ShimoriEntity, ShikimoriContentEntity {

    val type get() = AnimeType.find(_type)

    //local
    @ColumnInfo(name = "name_ru_lower_case")
    var searchName: String? = nameRu?.lowercase()

    @Ignore
    var videos: List<AnimeVideo>? = null
    @Ignore
    var studio: String? = null
    @Ignore
    var rateScoresStats: List<Statistic>? = null
    @Ignore
    var rateStatusesStats: List<Statistic>? = null

    val isMovie: Boolean
        get() = type == AnimeType.Movie

    val isOngoing: Boolean
        get() = status != null && status == ContentStatus.ONGOING

    val episodesOrUnknown : String
        get() = episodes.let { if (it == 0) "?" else "$it" }

    override val size: Int?
        get() = episodesOrUnknown.toIntOrNull()
}