package com.gnoemes.shimori.data.characters

import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.manga.Manga

data class CharacterInfo(
    val entity : Character,
    val animes : List<Anime>? = null,
    val mangas : List<Manga>? = null,
    //TODO add
//    val seyu : List<Person>
)