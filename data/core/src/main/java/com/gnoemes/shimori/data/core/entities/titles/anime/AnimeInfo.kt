package com.gnoemes.shimori.data.core.entities.titles.anime

import com.gnoemes.shimori.data.core.entities.track.Track

data class AnimeInfo(
    val entity : Anime,
    val track: Track?,
    val videos : List<AnimeVideo>?,
    //TODO
//    val screenshots
//    val genres
//    val translators
//    val studioInfo
)