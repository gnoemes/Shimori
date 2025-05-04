package com.gnoemes.shimori.source.model

data class SPersonSeyuRole(
    val id: Long = 0,
    val personId: Long,
    val character: SCharacter,
    val anime: SAnime
) {
    val characterId get() = character.id
    val titleId get() = anime.id
}