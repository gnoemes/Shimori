package com.gnoemes.shimori.data.core.entities.roles

import com.gnoemes.shimori.data.core.entities.characters.Character

@kotlinx.serialization.Serializable
data class RolesInfo(
    val characters : List<Character>,
//    val persons : List<Person>,
)