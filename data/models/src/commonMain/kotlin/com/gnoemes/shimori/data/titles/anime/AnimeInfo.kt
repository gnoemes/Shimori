package com.gnoemes.shimori.data.titles.anime

import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.data.characters.CharacterRole
import com.gnoemes.shimori.data.track.Track

data class AnimeInfo(
    val entity: Anime,
    val track: Track? = null,
    val videos: List<AnimeVideo>? = null,
    val screenshots: List<AnimeScreenshot>? = null,
    val fanDubbers: List<String>? = null,
    val fanSubbers: List<String>? = null,
    val characters: List<Character>? = null,
    val charactersRoles: List<CharacterRole>? = null,
    //TODO
//    val genres
//    val translators
//    val studioInfo
)