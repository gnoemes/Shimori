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
    val type: Int? = null,
    @SerializedName("sort") val sortOption: RateSortOption = RateSortOption.NAME,
    @SerializedName("descending") val isDescending: Boolean = false
) : ShimoriEntity, Serializable {

    val listType get() = type?.let { ListType.findOrDefault(it) }

    companion object {

        val AnimeDefault = RateSort(
                type = ListType.Anime.type,
                sortOption = RateSortOption.PROGRESS,
                isDescending = false
        )

        val MangaDefault = RateSort(
                type = ListType.Manga.type,
                sortOption = RateSortOption.PROGRESS,
                isDescending = false
        )

        val RanobeDefault = RateSort(
                type = ListType.Ranobe.type,
                sortOption = RateSortOption.PROGRESS,
                isDescending = false
        )

        val PinDefault = RateSort(
                type = ListType.Pinned.type,
                sortOption = RateSortOption.PROGRESS,
                isDescending = false
        )


        fun defaultForType(type: ListType): RateSort = when (type) {
            ListType.Anime -> AnimeDefault
            ListType.Manga -> MangaDefault
            ListType.Ranobe -> RanobeDefault
            else -> PinDefault
        }
    }
}