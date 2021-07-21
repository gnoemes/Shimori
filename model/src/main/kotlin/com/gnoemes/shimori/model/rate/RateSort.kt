package com.gnoemes.shimori.model.rate

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.gnoemes.shimori.model.ShimoriEntity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
        tableName = "rate_sort",
        indices = [
            Index(value = ["type"], unique = true)
        ]
)
data class RateSort(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    val type: RateTargetType? = null,
    @SerializedName("sort") val sortOption: RateSortOption = RateSortOption.NAME,
    @SerializedName("descending") val isDescending: Boolean = false
) : ShimoriEntity, Serializable {

    companion object {

        val AnimeDefault = RateSort(
                type = RateTargetType.ANIME,
                sortOption = RateSortOption.PROGRESS,
                isDescending = false
        )

        val MangaDefault = RateSort(
                type = RateTargetType.MANGA,
                sortOption = RateSortOption.PROGRESS,
                isDescending = false
        )

        val RanobeDefault = RateSort(
                type = RateTargetType.RANOBE,
                sortOption = RateSortOption.PROGRESS,
                isDescending = false
        )


        fun defaultForType(type: RateTargetType): RateSort = when(type) {
            RateTargetType.ANIME -> AnimeDefault
            RateTargetType.MANGA -> MangaDefault
            RateTargetType.RANOBE -> RanobeDefault
        }
    }
}