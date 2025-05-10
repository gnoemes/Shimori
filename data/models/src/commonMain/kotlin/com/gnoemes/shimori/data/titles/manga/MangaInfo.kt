package com.gnoemes.shimori.data.titles.manga

import com.gnoemes.shimori.data.ShimoriTitleEntity
import com.gnoemes.shimori.data.characters.CharacterInfo
import com.gnoemes.shimori.data.characters.CharacterRole
import com.gnoemes.shimori.data.common.Genre
import com.gnoemes.shimori.data.common.RelatedInfo
import com.gnoemes.shimori.data.person.PersonInfo
import com.gnoemes.shimori.data.person.PersonRole
import com.gnoemes.shimori.data.track.Track

data class MangaInfo(
    val entity: ShimoriTitleEntity,
    val track: Track? = null,
    val characters: List<CharacterInfo>? = null,
    val charactersRoles: List<CharacterRole>? = null,
    val genres: List<Genre>? = null,
    val persons: List<PersonInfo>? = null,
    val personsRoles: List<PersonRole>? = null,
    val related: List<RelatedInfo>? = null,
)