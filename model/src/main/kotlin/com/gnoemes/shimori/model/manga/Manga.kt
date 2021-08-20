package com.gnoemes.shimori.model.manga

import androidx.room.*
import com.gnoemes.shimori.model.ShikimoriContentEntity
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.common.*
import org.threeten.bp.LocalDate

@Entity(tableName = "mangas",
        indices = [
            Index(value = ["manga_shikimori_id"], unique = true),
        ]

)
data class Manga(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "manga_shikimori_id") override val shikimoriId: Long? = null,
    override val name: String = "",
    @ColumnInfo(name = "name_ru") override val nameRu: String? = null,
    @ColumnInfo(name = "name_eng") val nameEng: String? = null,
    @Embedded(prefix = "image_") override val image: ShimoriImage? = null,
    val url: String? = null,
    @ColumnInfo(name = "type") val _type: String? = null,
    @ColumnInfo(name = "rating") val score: Double? = null,
    @ColumnInfo(name = "manga_status") val status: ContentStatus? = null,
    @ColumnInfo(name = "volumes") val volumes: Int = 0,
    @ColumnInfo(name = "chapters") val chapters: Int = 0,
    @ColumnInfo(name = "date_aired") val dateAired: LocalDate? = null,
    @ColumnInfo(name = "date_released") val dateReleased: LocalDate? = null,
    @ColumnInfo(name = "age_rating") val ageRating: AgeRating? = null,
    val description: String? = null,
    @ColumnInfo(name = "description_html") val descriptionHtml: String? = null,
    val franchise: String? = null,
    val favorite: Boolean = false,
    @ColumnInfo(name = "topic_id") val topicId: Long? = null,
    val genres: List<Genre>? = null
) : ShimoriEntity, ShikimoriContentEntity {

    val type get() = MangaType.find(_type)

    @Ignore
    var rateScoresStats: List<Statistic>? = null

    @Ignore
    var rateStatusesStats: List<Statistic>? = null

    val isOngoing: Boolean
        get() = status != null && status == ContentStatus.ONGOING
}