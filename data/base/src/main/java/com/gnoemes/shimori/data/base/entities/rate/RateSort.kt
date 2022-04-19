package com.gnoemes.shimori.data.base.entities.rate

import com.gnoemes.shimori.data.base.entities.ShimoriEntity

@kotlinx.serialization.Serializable
data class RateSort(
    override val id: Long = 0,
    val type: Int? = null,
    val sortOption: RateSortOption = RateSortOption.NAME,
    val isDescending: Boolean = false
) : ShimoriEntity {

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