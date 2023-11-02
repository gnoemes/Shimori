package com.gnoemes.shimori.data.titles.anime

import com.gnoemes.shimori.data.track.Track

data class AnimeInfo(
    val entity: Anime,
    val track: Track?,
    val videos: List<AnimeVideo>?,
    val screenshots: List<AnimeScreenshot>?,
    //TODO
//    val genres
//    val translators
//    val studioInfo
)