package com.gnoemes.shimori.data.titles.manga

import com.gnoemes.shimori.data.ShimoriTitleEntity
import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.data.characters.CharacterRole
import com.gnoemes.shimori.data.track.Track

data class MangaInfo(
    val entity: ShimoriTitleEntity,
    val track: Track? = null,
    val characters: List<Character>? = null,
    val charactersRoles: List<CharacterRole>? = null,
)