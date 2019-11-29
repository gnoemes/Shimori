package com.gnoemes.shimori.search

import com.airbnb.mvrx.MvRxState
import com.gnoemes.shimori.model.anime.Anime

data class SearchViewState(
    val query : String? = null,
    val resuls : List<Anime>? = null
) : MvRxState