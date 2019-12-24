package com.gnoemes.shimori.model.anime

import androidx.room.*
import com.gnoemes.shimori.model.ShikimoriContentEntity
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.common.*
import com.gnoemes.shimori.model.rate.Rate
import org.joda.time.DateTime

@Entity(tableName = "animes",
        indices = [
            Index(value = ["shikimori_id"], unique = true),
            Index(value = ["rate_id", "next_episode_date"])
        ],
        foreignKeys = [
            ForeignKey(entity = Rate::class,
                    parentColumns = ["shikimori_id"],
                    childColumns = ["rate_id"])
        ]
)
data class Anime(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "shikimori_id") override val shikimoriId: Long? = null,
    override val name: String = "",
    @ColumnInfo(name = "name_ru") override val nameRu: String? = null,
    @Embedded(prefix = "image_") override val image: ShimoriImage? = null,
    val url: String? = null,
    val type: AnimeType? = null,
    val score: Double? = null,
    val status: ContentStatus? = null,
    val episodes: Int = 0,
    val episodesAired: Int = 0,
    @ColumnInfo(name = "date_aired") val dateAired: DateTime? = null,
    @ColumnInfo(name = "date_released") val dateReleased: DateTime? = null,
    @ColumnInfo(name = "next_episode") val nextEpisode: Int? = null,
    @ColumnInfo(name = "next_episode_date") val nextEpisodeDate: DateTime? = null,
    @ColumnInfo(name = "next_episode_end_date") val nextEpisodeEndDate: DateTime? = null,
    @ColumnInfo(name = "age_rating") val ageRating: AgeRating? = null,
    val duration: DateTime? = null,
    val description: String? = null,
    @ColumnInfo(name = "description_html") val descriptionHtml: String? = null,
    val franchise: String? = null,
    val favorite: Boolean = false,
    @ColumnInfo(name = "topic_id") val topicId: Long? = null,
    val genres: List<Genre>? = null,
    @ColumnInfo(name = "rate_id") val rateId: Long? = null
) : ShimoriEntity, ShikimoriContentEntity {

    //local
    @ColumnInfo(name = "name_ru_lower_case")
    var searchName: String? = nameRu?.toLowerCase()

    @Ignore
    var videos: List<AnimeVideo>? = null
    @Ignore
    var studio: String? = null
    @Ignore
    var rateScoresStats: List<Statistic>? = null
    @Ignore
    var rateStatusesStats: List<Statistic>? = null

    @get:Ignore
    override val contentType: ContentType
        get() = ContentType.ANIME

    val isMovie: Boolean
        get() = type != null && type == AnimeType.MOVIE
}