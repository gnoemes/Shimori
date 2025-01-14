package com.gnoemes.shimori.data.characters

import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.manga.Manga

data class CharacterInfo(
    val entity : Character,
    val animes : List<Anime>,
    val mangas : List<Manga>,
    //TODO add
//    val seyu : List<Person>
)