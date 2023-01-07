package com.gnoemes.shimori.data.core.entities.characters

import com.gnoemes.shimori.data.core.entities.titles.anime.Anime
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga

data class CharacterInfo(
    val character : Character,
    val animes : List<Anime>,
    val mangas : List<Manga>,
    //TODO add
//    val seyu : List<Person>
)