package com.gnoemes.shimori.data.roles

import com.gnoemes.shimori.data.characters.Character

@kotlinx.serialization.Serializable
data class RolesInfo(
    val characters : List<Character>,
//    val persons : List<Person>,
)