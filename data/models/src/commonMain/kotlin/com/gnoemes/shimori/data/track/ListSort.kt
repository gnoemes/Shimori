package com.gnoemes.shimori.data.track

import com.gnoemes.shimori.data.ShimoriEntity

@kotlinx.serialization.Serializable
data class ListSort(
    override val id: Long = 0,
    val type: TrackTargetType = TrackTargetType.ANIME,
    val sortOption: ListSortOption = ListSortOption.NAME,
    val isDescending: Boolean = false
) : ShimoriEntity {

    companion object {
        val AnimeDefault = ListSort(
            type = TrackTargetType.ANIME,
            sortOption = ListSortOption.PROGRESS,
            isDescending = false
        )

        val MangaDefault = ListSort(
            type = TrackTargetType.MANGA,
            sortOption = ListSortOption.PROGRESS,
            isDescending = false
        )

        val RanobeDefault = ListSort(
            type = TrackTargetType.RANOBE,
            sortOption = ListSortOption.PROGRESS,
            isDescending = false
        )

        fun defaultForType(type: TrackTargetType): ListSort = when (type) {
            TrackTargetType.ANIME -> AnimeDefault
            TrackTargetType.MANGA -> MangaDefault
            TrackTargetType.RANOBE -> RanobeDefault
        }
    }
}