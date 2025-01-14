package com.gnoemes.shimori.data.track

import com.gnoemes.shimori.data.ShimoriEntity

@kotlinx.serialization.Serializable
data class ListSort(
    override val id: Long = 0,
    val type: ListType = ListType.Anime,
    val sortOption: ListSortOption = ListSortOption.NAME,
    val isDescending: Boolean = false
) : ShimoriEntity {

    companion object {
        val AnimeDefault = ListSort(
            type = ListType.Anime,
            sortOption = ListSortOption.PROGRESS,
            isDescending = false
        )

        val MangaDefault = ListSort(
            type = ListType.Manga,
            sortOption = ListSortOption.PROGRESS,
            isDescending = false
        )

        val RanobeDefault = ListSort(
            type = ListType.Ranobe,
            sortOption = ListSortOption.PROGRESS,
            isDescending = false
        )

        val PinDefault = ListSort(
            type = ListType.Pinned,
            sortOption = ListSortOption.PROGRESS,
            isDescending = false
        )

        fun defaultForType(type: ListType): ListSort = when (type) {
            ListType.Anime -> AnimeDefault
            ListType.Manga -> MangaDefault
            ListType.Ranobe -> RanobeDefault
            else -> PinDefault
        }
    }
}