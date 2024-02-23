package com.gnoemes.shimori.data.titles.anime

import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.data.characters.CharacterRole
import com.gnoemes.shimori.data.track.Track

data class AnimeInfo(
    val entity: Anime,
    val track: Track?,
    val videos: List<AnimeVideo>?,
    val screenshots: List<AnimeScreenshot>?,
    val fanDubbers: List<String>,
    val fanSubbers: List<String>,
    val characters: List<Character>,
    val charactersRoles: List<CharacterRole>
    //TODO
//    val genres
//    val translators
//    val studioInfo
)