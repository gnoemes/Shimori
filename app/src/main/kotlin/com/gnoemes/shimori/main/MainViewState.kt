package com.gnoemes.shimori.main

import androidx.compose.runtime.Immutable
import com.gnoemes.shimori.data.base.entities.rate.ListType

@Immutable
data class MainViewState(
    val listType: ListType = ListType.Anime
) {

    companion object {
        val Empty = MainViewState()
    }
}