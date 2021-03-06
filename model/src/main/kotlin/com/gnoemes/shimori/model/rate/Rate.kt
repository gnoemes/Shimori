package com.gnoemes.shimori.model.rate

import androidx.room.*
import com.gnoemes.shimori.model.ShikimoriEntity
import com.gnoemes.shimori.model.ShimoriEntity
import org.threeten.bp.OffsetDateTime

@Entity(tableName = "rates",
        indices = [
            Index(value = ["shikimori_id"], unique = true),
            Index(value = ["anime_id"], unique = true),
            Index(value = ["manga_id"], unique = true),
            Index(value = ["ranobe_id"], unique = true),
            Index(value = ["date_created"])
        ]
)
data class Rate(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "shikimori_id") override val shikimoriId: Long? = null,
    @ColumnInfo(name = "anime_id") val animeId: Long? = null,
    @ColumnInfo(name = "manga_id") val mangaId: Long? = null,
    @ColumnInfo(name = "ranobe_id") val ranobeId: Long? = null,
    @ColumnInfo(name = "target_type") val targetType: RateTargetType? = null,
    @ColumnInfo(name = "status") val status: RateStatus? = null,
    @ColumnInfo(name = "score") val score: Int? = null,
    @ColumnInfo(name = "comment") val comment: String? = null,
    @ColumnInfo(name = "episodes") val episodes: Int? = null,
    @ColumnInfo(name = "chapters") val chapters: Int? = null,
    @ColumnInfo(name = "volumes") val volumes: Int? = null,
    @ColumnInfo(name = "re_counter") val reCounter: Int? = null,
    @ColumnInfo(name = "date_created") val dateCreated: OffsetDateTime? = null,
    @ColumnInfo(name = "date_updated") val dateUpdated: OffsetDateTime? = null
) : ShimoriEntity, ShikimoriEntity {

    @Ignore
    var userId: Long? = null

    val progress: Int?
        get() = when (targetType) {
            RateTargetType.ANIME -> episodes
            RateTargetType.MANGA -> chapters
            RateTargetType.RANOBE -> chapters
            else -> null
        }

    companion object {
        val EMPTY = Rate()

        val DEFAULT_ANIME = Rate(targetType = RateTargetType.ANIME, status = RateStatus.WATCHING, episodes = 0)
        val DEFAULT_MANGA = Rate(targetType = RateTargetType.MANGA, status = RateStatus.WATCHING, chapters = 0)
        val DEFAULT_RANOBE = Rate(targetType = RateTargetType.RANOBE, status = RateStatus.WATCHING, chapters = 0)
    }
}

fun Rate.copyProgress(newProgress: Int): Rate {
    return when (targetType) {
        RateTargetType.ANIME -> copy(episodes = newProgress)
        RateTargetType.MANGA -> copy(chapters = newProgress)
        RateTargetType.RANOBE -> copy(chapters = newProgress)
        else -> this
    }
}

fun Rate.copyTargetId(newTargetId : Long?) : Rate {
    return when (targetType) {
        RateTargetType.ANIME -> copy(animeId = newTargetId)
        RateTargetType.MANGA -> copy(mangaId = newTargetId)
        RateTargetType.RANOBE -> copy(ranobeId = newTargetId)
        else -> this
    }
}