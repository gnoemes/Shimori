package com.gnoemes.shimori.data.base.entities.rate

import com.gnoemes.shimori.data.base.entities.ShimoriEntity

@kotlinx.serialization.Serializable
data class RateSort(
    override val id: Long = 0,
    val type: ListType = ListType.Anime,
    val sortOption: RateSortOption = RateSortOption.NAME,
    val isDescending: Boolean = false
) : ShimoriEntity {

    companion object {
        val AnimeDefault = RateSort(
            type = ListType.Anime,
            sortOption = RateSortOption.PROGRESS,
            isDescending = false
        )

        val MangaDefault = RateSort(
            type = ListType.Manga,
            sortOption = RateSortOption.PROGRESS,
            isDescending = false
        )

        val RanobeDefault = RateSort(
            type = ListType.Ranobe,
            sortOption = RateSortOption.PROGRESS,
            isDescending = false
        )

        val PinDefault = RateSort(
            type = ListType.Pinned,
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