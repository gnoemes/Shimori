package com.gnoemes.shimori.model.rate

import androidx.room.*
import com.gnoemes.shimori.model.ShikimoriEntity
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.anime.Anime
import org.joda.time.DateTime

@Entity(tableName = "rates",
        indices = [
            Index(value = ["target_id", "target_type"], unique = true),
            Index(value = ["shikimori_id"], unique = true),
            Index(value = ["date_created"])
        ],
        foreignKeys = [
            ForeignKey(entity = Anime::class,
                    parentColumns = ["shikimori_id"],
                    childColumns = ["target_id"])
        ]
)
data class Rate (
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "shikimori_id") override val shikimoriId: Long? = null,
    @ColumnInfo(name = "target_id") var targetId: Long? = null,
    @ColumnInfo(name = "target_type") val targetType: RateTargetType? = null,
    @ColumnInfo(name = "status") val status: RateStatus? = null,
    @ColumnInfo(name = "comment") val comment: String? = null,
    @ColumnInfo(name = "episodes") val episodes: Int? = null,
    @ColumnInfo(name = "chapters") val chapters: Int? = null,
    @ColumnInfo(name = "volumes") val volumes: Int? = null,
    @ColumnInfo(name = "re_counter") val reCounter: Int? = null,
    @ColumnInfo(name = "date_created") val dateCreated: DateTime? = null,
    @ColumnInfo(name = "date_updated") val dateUpdated: DateTime? = null
) : ShimoriEntity, ShikimoriEntity {

    @Ignore
    var userId: Long? = null

    companion object {
        val EMPTY = Rate()
    }
}
